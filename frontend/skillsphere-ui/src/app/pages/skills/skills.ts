import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';

import { KeycloakAuthService } from '../../auth/keycloak.service';
import { SkillProfileService } from '../../services/skill-profile.service';

@Component({
  selector: 'app-skills',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './skills.html',
  styleUrl: './skills.scss',
})
export class SkillsComponent implements OnInit {

  profile: any = null;

  skills: any[] = [];

  catalog: any[] = [];

  username = '';

  loading = true;

  error = false;

  /*
   * Role flags
   */
  isEmployee = false;

  isAdmin = false;

  isTrainingManager = false;

  isHr = false;

  /*
   * Create Skill State
   */
  showCreateSkillModal = false;
  
  newSkill = {
    name: '',
    category: ''
  };


  constructor(
    private skillProfileService: SkillProfileService,
    private auth: KeycloakAuthService,
    private cdr: ChangeDetectorRef,
  ) {}


  ngOnInit(): void {

    console.log('SkillsComponent initialized');

    this.username =
      this.auth.getUsername() ?? 'User';


    /*
     * Determine the user's role.
     */
    this.isAdmin =
      this.auth.isAdmin();

    this.isTrainingManager =
      this.auth.isTrainingManager();

    this.isHr =
      this.auth.isHr();

    this.isEmployee =
      this.auth.isEmployee();


    console.log('Skills roles:', {
      roles: this.auth.getRoles(),
      isEmployee: this.isEmployee,
      isAdmin: this.isAdmin,
      isTrainingManager: this.isTrainingManager,
      isHr: this.isHr
    });


    this.loadSkills();

  }


  private loadSkills(): void {

    this.loading = true;

    this.error = false;


    /*
     * ==========================================
     * EMPLOYEE
     * ==========================================
     *
     * Employees should see only their
     * own skill profile.
     */

    if (this.isEmployee) {

      this.loadEmployeeSkills();

      return;
    }


    /*
     * ==========================================
     * MANAGEMENT
     * ==========================================
     *
     * Admin / Training Manager / HR should
     * see the company-wide skill catalog.
     */

    if (
      this.isAdmin ||
      this.isTrainingManager ||
      this.isHr
    ) {

      this.loadManagementSkills();

      return;
    }


    /*
     * Unknown role
     */

    console.error(
      'No supported role found for Skills page.'
    );

    this.loading = false;

    this.error = true;

    this.cdr.detectChanges();

  }


  /*
   * ==========================================
   * EMPLOYEE SKILLS
   * ==========================================
   */

  private loadEmployeeSkills(): void {

    const empId =
      this.getEmployeeId();


    if (!empId) {

      console.error(
        'Employee ID could not be obtained from Keycloak.'
      );

      this.loading = false;

      this.error = true;

      this.cdr.detectChanges();

      return;
    }


    console.log(
      'Loading skill profile for employee:',
      empId
    );


    this.skillProfileService
      .getProfile(empId)
      .subscribe({

        next: (response: any) => {

          console.log(
            'Employee skill profile:',
            response
          );


          this.profile = response;


          this.skills =
            Array.isArray(response?.skills)
              ? response.skills
              : [];


          /*
           * Load the company catalog for the employee
           * so they can opt into new skills.
           */
          this.skillProfileService.getCatalog().subscribe({
            next: (catalogRes: any) => {
              this.catalog = Array.isArray(catalogRes) ? catalogRes : [];
              this.loading = false;
              this.cdr.detectChanges();
            },
            error: (err) => {
              console.error('Failed to load company catalog for employee', err);
              this.catalog = [];
              this.loading = false;
              this.cdr.detectChanges();
            }
          });

        },


        error: (err) => {
          if (err.status === 404) {
            console.log('No skill profile found for employee, initializing empty profile.');
            this.profile = { employee: { name: this.username } };
            this.skills = [];
            this.skillProfileService.getCatalog().subscribe({
              next: (catalogRes: any) => {
                this.catalog = Array.isArray(catalogRes) ? catalogRes : [];
                this.loading = false;
                this.cdr.detectChanges();
              },
              error: () => {
                this.catalog = [];
                this.loading = false;
                this.cdr.detectChanges();
              }
            });
          } else {
            console.error(
              'Employee skill profile loading failed:',
              err
            );
            this.loading = false;
            this.error = true;
            this.cdr.detectChanges();
          }
        }

      });

  }


  /*
   * ==========================================
   * MANAGEMENT SKILLS
   * ==========================================
   */

  private loadManagementSkills(): void {

    console.log(
      'Loading company skill catalog.'
    );


    this.skillProfileService
      .getCatalog()
      .subscribe({

        next: (response: any) => {

          console.log(
            'Company skill catalog:',
            response
          );


          this.catalog =
            Array.isArray(response)
              ? response
              : [];


          /*
           * Management users do not need
           * an individual employee profile
           * on this page.
           */

          this.profile = null;

          this.skills = [];


          this.loading = false;

          this.cdr.detectChanges();

        },


        error: (err) => {

          console.error(
            'Skill catalog loading failed:',
            err
          );


          this.loading = false;

          this.error = true;

          this.cdr.detectChanges();

        }

      });

  }


  /*
   * ==========================================
   * EMPLOYEE ID
   * ==========================================
   */

  private getEmployeeId(): string | null {

    const userId =
      this.auth.getUserId();


    if (!userId) {
      return null;
    }


    return userId;

  }


  /*
   * ==========================================
   * INITIALS
   * ==========================================
   */

  getInitials(): string {

    const username =
      this.username.trim();


    if (!username) {
      return 'U';
    }


    const parts =
      username.split(/\s+/);


    if (parts.length === 1) {

      return parts[0]
        .substring(0, 2)
        .toUpperCase();

    }


    return (
      parts[0].charAt(0) +
      parts[parts.length - 1].charAt(0)
    ).toUpperCase();

  }


  /*
   * ==========================================
   * CAN CREATE SKILL
   * ==========================================
   *
   * Only Admin and Training Manager can create
   * new skills in the company catalog.
   * HR is view-only.
   */

  canCreateSkill(): boolean {
    return this.isAdmin || this.isTrainingManager;
  }


  /*
   * ==========================================
   * CREATE SKILL LOGIC
   * ==========================================
   */

  openCreateSkillModal(): void {
    if (!this.canCreateSkill()) return;
    this.newSkill = { name: '', category: '' };
    this.showCreateSkillModal = true;
  }

  closeCreateSkillModal(): void {
    this.showCreateSkillModal = false;
  }

  createSkill(): void {
    if (!this.newSkill.name || !this.newSkill.category) {
      alert('Please fill out all required fields.');
      return;
    }

    this.loading = true;
    this.skillProfileService.createSkill(this.newSkill).subscribe({
      next: (res) => {
        console.log('Skill created', res);
        this.closeCreateSkillModal();
        this.loadManagementSkills(); // Refresh catalog
      },
      error: (err) => {
        console.error('Failed to create skill', err);
        alert('Failed to create skill. Please try again.');
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  /*
   * ==========================================
   * EMPLOYEE SKILL OPT-IN & UPDATE
   * ==========================================
   */

  optInSkill(skillId: string): void {
    const empId = this.getEmployeeId();
    if (!empId) return;
    
    this.loading = true;
    this.skillProfileService.addSkillToProfile(empId, skillId).subscribe({
      next: () => {
        alert('Skill added to your profile!');
        this.loadEmployeeSkills();
      },
      error: (err) => {
        console.error('Failed to add skill', err);
        alert('Failed to add skill. Please try again.');
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  hasSkill(skillId: string): boolean {
    return this.skills.some((s: any) => s.skill?.skillId === skillId || s.skill?.id === skillId || s.id === skillId);
  }

  updateProficiency(skill: any): void {
    const empId = this.getEmployeeId();
    if (!empId) return;

    let skillId = skill.skill?.skillId || skill.skill?.id || skill.skillId || skill.id;

    if (!skillId) {
      alert("Invalid skill reference");
      return;
    }

    this.loading = true;
    this.skillProfileService.updateSkillProficiency(empId, skillId, skill.proficiency).subscribe({
      next: () => {
        alert('Proficiency updated!');
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to update proficiency', err);
        alert('Failed to update proficiency. Please try again.');
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  /*
   * ==========================================
   * RETRY
   * ==========================================
   */

  retry(): void {

    this.loadSkills();

  }

}