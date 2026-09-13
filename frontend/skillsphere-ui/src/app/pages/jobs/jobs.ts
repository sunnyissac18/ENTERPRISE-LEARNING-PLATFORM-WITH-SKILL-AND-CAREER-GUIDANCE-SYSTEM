import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CareerService } from '../../services/career';
import { KeycloakAuthService } from '../../auth/keycloak.service';

@Component({
  selector: 'app-jobs',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './jobs.html',
  styleUrl: './jobs.scss',
})
export class Jobs implements OnInit {
  jobs: any[] = [];
  
  isEmployee = false;
  isManagement = false;
  
  showCreateModal = false;
  submitting = false;
  
  newJob = {
    title: '',
    department: '',
    requiredSkills: '',
    minimumExperience: 0
  };

  constructor(
    private careerService: CareerService,
    private auth: KeycloakAuthService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.isEmployee = this.auth.isEmployee();
    this.isManagement = this.auth.hasManagementAccess();
    this.loadJobs();
  }

  loadJobs(): void {
    this.careerService.getJobs().subscribe({
      next: (data) => {
        console.log('Jobs:', data);
        this.jobs = data;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error loading jobs:', error);
      },
    });
  }
  
  openCreateModal(): void {
    this.newJob = {
      title: '',
      department: '',
      requiredSkills: '',
      minimumExperience: 0
    };
    this.showCreateModal = true;
  }
  
  closeCreateModal(): void {
    this.showCreateModal = false;
  }
  
  createJob(): void {
    this.submitting = true;
    this.careerService.createJob(this.newJob).subscribe({
      next: () => {
        this.submitting = false;
        this.showCreateModal = false;
        this.cdr.detectChanges();
        this.loadJobs();
      },
      error: (err) => {
        console.error('Failed to create job', err);
        this.submitting = false;
        window.alert('Failed to create job. Please try again.');
        this.cdr.detectChanges();
      }
    });
  }
}
