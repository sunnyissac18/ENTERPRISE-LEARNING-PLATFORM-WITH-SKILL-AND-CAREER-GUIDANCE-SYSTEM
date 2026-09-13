import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CareerService } from '../../services/career';
import { KeycloakAuthService } from '../../auth/keycloak.service';

@Component({
  selector: 'app-career',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './career.html',
  styleUrl: './career.scss',
})
export class Career implements OnInit {
  isEmployee = false;
  isManagement = false;

  // Management view
  plans: any[] = [];
  loadingPlans = true;

  // Employee view
  myPlan: any = null;
  careerForm!: FormGroup;
  isSaving = false;
  isEditing = false;
  successMessage = '';
  errorMessage = '';

  constructor(
    private careerService: CareerService,
    private auth: KeycloakAuthService,
    private fb: FormBuilder,
    private cdr: ChangeDetectorRef,
  ) {
    this.initForm();
  }

  ngOnInit(): void {
    this.isEmployee = this.auth.isEmployee();
    this.isManagement = this.auth.hasManagementAccess();

    if (this.isEmployee) {
      this.loadMyPlan();
    } else {
      this.loadAllPlans();
    }
  }

  private initForm(): void {
    this.careerForm = this.fb.group({
      currentRole: ['', Validators.required],
      targetRole: ['', Validators.required],
      progress: [0, [Validators.required, Validators.min(0), Validators.max(100)]],
      mentor: [''],
      skillGaps: [''],
      trainingPlan: ['']
    });
  }

  // --- EMPLOYEE METHODS ---

  loadMyPlan(): void {
    const empId = this.auth.getUserId();
    if (!empId) return;

    this.careerService.getCareerPlanByEmployee(empId).subscribe({
      next: (data) => {
        if (data && data.length > 0) {
          this.myPlan = data[0]; // Assuming one plan per employee for simplicity
          this.careerForm.patchValue({
            currentRole: this.myPlan.currentRole,
            targetRole: this.myPlan.targetRole,
            progress: this.myPlan.progress,
            mentor: this.myPlan.mentor,
            skillGaps: this.myPlan.skillGaps,
            trainingPlan: this.myPlan.trainingPlan
          });
          this.isEditing = false;
        } else {
          this.isEditing = true;
        }
        this.loadingPlans = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error loading my plan:', error);
        this.loadingPlans = false;
        this.cdr.detectChanges();
      }
    });
  }

  editPlan(): void {
    this.isEditing = true;
    this.cdr.detectChanges();
  }

  cancelEdit(): void {
    if (this.myPlan) {
      this.careerForm.patchValue({
        currentRole: this.myPlan.currentRole,
        targetRole: this.myPlan.targetRole,
        progress: this.myPlan.progress,
        mentor: this.myPlan.mentor,
        skillGaps: this.myPlan.skillGaps,
        trainingPlan: this.myPlan.trainingPlan
      });
      this.careerForm.markAsPristine();
      this.isEditing = false;
    }
    this.cdr.detectChanges();
  }

  savePlan(): void {
    if (this.careerForm.invalid || this.careerForm.pristine) {
      return;
    }

    this.isSaving = true;
    this.successMessage = '';
    this.errorMessage = '';

    const empId = this.auth.getUserId();
    const employeeName = this.auth.getUsername();
    const formData = this.careerForm.value;

    const payload = {
      empId,
      employeeName,
      ...formData
    };

    if (this.myPlan && this.myPlan.planId) {
      // Update existing
      this.careerService.updateCareerPlan(this.myPlan.planId, payload).subscribe({
        next: (res) => {
          this.myPlan = res;
          this.isSaving = false;
          this.isEditing = false;
          this.careerForm.markAsPristine();
          this.successMessage = 'Career plan updated successfully!';
          this.cdr.detectChanges();
          this.hideMessageAfterDelay();
        },
        error: (err) => {
          console.error(err);
          this.isSaving = false;
          this.errorMessage = 'Failed to update career plan.';
          this.cdr.detectChanges();
        }
      });
    } else {
      // Create new
      this.careerService.createCareerPlan(payload).subscribe({
        next: (res) => {
          this.myPlan = res;
          this.isSaving = false;
          this.isEditing = false;
          this.careerForm.markAsPristine();
          this.successMessage = 'Career plan created successfully!';
          this.cdr.detectChanges();
          this.hideMessageAfterDelay();
        },
        error: (err) => {
          console.error(err);
          this.isSaving = false;
          this.errorMessage = 'Failed to create career plan.';
          this.cdr.detectChanges();
        }
      });
    }
  }

  private hideMessageAfterDelay(): void {
    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
      this.cdr.detectChanges();
    }, 4000);
  }


  // --- MANAGEMENT METHODS ---

  loadAllPlans(): void {
    this.careerService.getCareerPlans().subscribe({
      next: (data) => {
        this.plans = data;
        this.loadingPlans = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error loading career plans:', error);
        this.loadingPlans = false;
        this.cdr.detectChanges();
      },
    });
  }
}
