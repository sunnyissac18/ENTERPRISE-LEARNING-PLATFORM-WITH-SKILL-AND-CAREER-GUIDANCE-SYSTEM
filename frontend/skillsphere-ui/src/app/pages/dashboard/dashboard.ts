import { CommonModule } from '@angular/common';

import {
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';

import {
  DashboardData,
  DashboardService,
  EmployeeDashboardData
} from '../../services/dashboard.service';

import { EnrollmentService } from '../../services/enrollment.service';
import { KeycloakAuthService } from '../../auth/keycloak.service';
import { SkillProfileService } from '../../services/skill-profile.service';
import { CertificationService } from '../../certifications/certification.service';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class DashboardComponent implements OnInit {

  employeeCount = 0;

  /*
   * Role
   */
  isEmployee = false;

  isManagement = false;


  /*
   * Company dashboard data
   */
  data: DashboardData = {
    skills: 0,
    courses: 0,
    completion: 0,
    careerPlans: 0,
    promotions: 0,
    activeCertifications: 0,
    expiringCertifications: 0,
    skillGaps: []
  };


  /*
   * Employee dashboard data
   */
  employeeData: EmployeeDashboardData = {
    username: '',
    courses: 0,
    completedCourses: 0,
    inProgressCourses: 0,
    completion: 0
  };


  /*
   * Employee personal counters
   */
  mySkillsCount = 0;

  myCertificationsCount = 0;


  loading = true;

  error = false;


  constructor(
    private dashboardService: DashboardService,
    private enrollmentService: EnrollmentService,
    private skillProfileService: SkillProfileService,
    private employeeService: EmployeeService,
    private certificationService: CertificationService,
    private auth: KeycloakAuthService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {

    console.log(
      'DashboardComponent initialized'
    );

    /*
     * Determine role before loading data.
     */
    this.isEmployee =
      this.auth.isEmployee();

    this.isManagement =
      this.auth.hasManagementAccess();


    console.log(
      'Dashboard roles:',
      this.auth.getRoles()
    );

    console.log(
      'Is employee:',
      this.isEmployee
    );

    console.log(
      'Is management:',
      this.isManagement
    );


    if (this.isEmployee) {

      this.loadEmployeeDashboard();

    } else {

      this.loadManagementDashboard();
        this.loadEmployeeCount();

    }

  }


  /*
   * ==========================================
   * MANAGEMENT DASHBOARD
   * ==========================================
   */

  private loadEmployeeCount(): void {
    this.employeeService.getAllEmployees().subscribe({
      next: (data: any[]) => {
        this.employeeCount = data.length;
        this.cdr.detectChanges();
      },
      error: (err: any) => console.error(err)
    });
  }

  loadManagementDashboard(): void {

    this.loading = true;
    this.error = false;


    this.dashboardService
      .getDashboardData()
      .subscribe({

        next: (data) => {

          console.log(
            'Management dashboard data received:',
            data
          );


          this.data = data;

          this.loading = false;

          this.cdr.detectChanges();

        },


        error: (err) => {

          console.error(
            'Management dashboard loading failed:',
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
   * EMPLOYEE DASHBOARD
   * ==========================================
   */

  private loadEmployeeDashboard(): void {

    const empId = this.auth.getUserId();

    if (!empId) {
      console.error('Employee ID could not be obtained from Keycloak');
      this.loading = false;
      this.error = true;
      this.cdr.detectChanges();
      return;
    }

    this.loading = true;
    this.error = false;

    const username = this.auth.getUsername() || 'Employee';

    /*
     * Load enrollments, skill profile, and certifications
     * in parallel. Skills and certifications failures are
     * non-fatal — we default to zero counts.
     */
    forkJoin({
      enrollments: this.enrollmentService
        .getEmployeeEnrollments(empId)
        .pipe(catchError(() => of([]))),

      skills: this.skillProfileService
        .getProfile(empId)
        .pipe(catchError(() => of(null))),

      certifications: this.certificationService
        .getEmployeeCertifications(empId)
        .pipe(catchError(() => of([]))),

    }).subscribe({

      next: ({ enrollments, skills, certifications }) => {

        const employeeEnrollments =
          Array.isArray(enrollments) ? enrollments : [];

        const total = employeeEnrollments.length;

        const completed = employeeEnrollments.filter(
          e => e.completed === true || Number(e.progress ?? 0) >= 100
        ).length;

        const inProgress = employeeEnrollments.filter(
          e => !(e.completed === true || Number(e.progress ?? 0) >= 100)
        ).length;

        let completion = 0;
        if (total > 0) {
          completion = Math.round(
            employeeEnrollments.reduce(
              (sum, e) => sum + Number(e.progress ?? 0), 0
            ) / total
          );
        }

        this.employeeData = { username, courses: total, completedCourses: completed, inProgressCourses: inProgress, completion };

        /* Skill count */
        const skillList = (skills as any)?.skills;
        this.mySkillsCount = Array.isArray(skillList) ? skillList.length : 0;

        /* Certification count */
        this.myCertificationsCount = Array.isArray(certifications)
          ? certifications.length
          : 0;

        this.loading = false;
        this.cdr.detectChanges();

      },

      error: (err) => {
        console.error('Employee dashboard loading failed:', err);
        this.loading = false;
        this.error = true;
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

    if (this.isEmployee) {

      this.loadEmployeeDashboard();

    } else {

      this.loadManagementDashboard();
        this.loadEmployeeCount();

    }

  }

}

