import { CommonModule } from '@angular/common';

import {
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';

import {
  ActivatedRoute,
  RouterLink
} from '@angular/router';

import { FormsModule } from '@angular/forms';

import { EnrollmentService } from '../../services/enrollment.service';
import { KeycloakAuthService } from '../../auth/keycloak.service';
import { LearningService } from '../learning';

@Component({
  selector: 'app-enrollment',
  standalone: true,

  imports: [
    CommonModule,
    FormsModule,
    RouterLink
  ],

  templateUrl: './enrollment.html',
  styleUrl: './enrollment.scss'
})
export class Enrollment implements OnInit {

  enrollment: any = null;

  loading = true;

  error = false;

  isEmployee = false;
  isManagement = false;
  isAdmin = false;
  isTrainingManager = false;

  /*
   * HR can view enrollment details read-only.
   * They cannot update progress, score, or complete the course.
   */
  isHr = false;


  // Admin / Training Manager update values

  progressValue = 0;

  scoreValue = 0;


  // Button states

  updatingProgress = false;

  submittingScore = false;

  completingCourse = false;

  generatingCertificate = false;



  constructor(
    private route: ActivatedRoute,
    private enrollmentService: EnrollmentService,
    private learningService: LearningService,
    private auth: KeycloakAuthService,
    private cdr: ChangeDetectorRef
  ) {}


  ngOnInit(): void {
    this.isEmployee = this.auth.isEmployee();
    this.isManagement = this.auth.hasManagementAccess();
    this.isAdmin = this.auth.isAdmin();
    this.isTrainingManager = this.auth.isTrainingManager();
    this.isHr = this.auth.isHr();

    this.loadEnrollment();
  }


  /*
   * ==========================================
   * ROLE HELPERS
   * ==========================================
   */

  /**
   * Only Admin and Training Manager can set progress / score / complete.
   */
  canManageProgress(): boolean {
    return this.isAdmin || this.isTrainingManager;
  }

  /**
   * Employee can generate certificate after:
   *   - course is completed
   *   - score > 0 (set by management)
   *
   * Admin / Training Manager can also generate it.
   */
  canGenerateCertificate(): boolean {
    return (
      !!this.enrollment?.completed &&
      (this.enrollment?.score ?? 0) > 0 &&
      (this.isEmployee || this.isAdmin || this.isTrainingManager)
    );
  }


  private loadEnrollment(): void {

    const enrollmentId =
      this.route.snapshot.paramMap.get(
        'enrollmentId'
      );

    const empId =
      this.auth.getUserId();


    console.log(
      'Enrollment ID from URL:',
      enrollmentId
    );

    console.log(
      'Employee ID from Keycloak:',
      empId
    );


    if (!enrollmentId || !empId) {

      console.error(
        'Missing enrollmentId or employeeId'
      );

      this.loading = false;
      this.error = true;

      this.cdr.detectChanges();

      return;
    }


    this.loading = true;
    this.error = false;

    const fetchEnrollments$ = this.isManagement 
      ? this.enrollmentService.getAllEnrollments() 
      : this.enrollmentService.getEmployeeEnrollments(empId);

    fetchEnrollments$.subscribe({

        next: (enrollments) => {

          console.log(
            'Enrollments received:',
            enrollments
          );


          const selectedEnrollment =
            enrollments.find(
              item =>
                String(item.enrollmentId) ===
                String(enrollmentId)
            );


          if (!selectedEnrollment) {

            console.error(
              'Enrollment not found:',
              enrollmentId
            );

            this.loading = false;
            this.error = true;

            this.cdr.detectChanges();

            return;
          }


          console.log(
            'Selected enrollment:',
            selectedEnrollment
          );


          const courseId =
            selectedEnrollment.courseId;


          if (!courseId) {

            console.error(
              'Course ID is missing from enrollment'
            );

            this.loading = false;
            this.error = true;

            this.cdr.detectChanges();

            return;
          }


          /*
           * Load complete course information.
           */

          this.learningService
            .getCourse(courseId)
            .subscribe({

              next: (course) => {

                console.log(
                  'Course loaded:',
                  course
                );


                /*
                 * Combine enrollment and course.
                 */

                this.enrollment = {
                  ...selectedEnrollment,
                  course: course
                };


                /*
                 * Initialize Admin / Training Manager update fields.
                 */

                this.progressValue =
                  this.enrollment.progress ?? 0;

                this.scoreValue =
                  this.enrollment.score ?? 0;

                this.loading = false;
                this.error = false;


                console.log(
                  'Final enrollment:',
                  this.enrollment
                );


                this.cdr.detectChanges();

              },


              error: (err) => {

                console.error(
                  'Course loading failed:',
                  err
                );

                this.loading = false;
                this.error = true;

                this.cdr.detectChanges();

              }

            });

        },


        error: (err) => {

          console.error(
            'Enrollment loading failed:',
            err
          );

          this.loading = false;
          this.error = true;

          this.cdr.detectChanges();

        }

      });

  }


  // ==========================================
  // UPDATE PROGRESS (Admin / Training Manager only)
  // ==========================================


  updateProgress(): void {

    if (!this.enrollment?.enrollmentId) {

      return;

    }


    const progress =
      Number(this.progressValue);


    if (
      isNaN(progress) ||
      progress < 0 ||
      progress > 100
    ) {

      window.alert(
        'Progress must be between 0 and 100.'
      );

      return;

    }


    this.updatingProgress = true;

    this.cdr.detectChanges();


    this.learningService
      .updateProgress(
        this.enrollment.enrollmentId,
        progress
      )
      .subscribe({

        next: (response) => {

          console.log(
            'Progress updated:',
            response
          );


          /*
           * Update UI immediately.
           */

          this.enrollment.progress =
            progress;


          this.progressValue =
            progress;


          this.updatingProgress = false;


          /*
           * If progress reaches 100,
           * refresh enrollment from backend.
           */

          if (progress === 100) {

            this.loadEnrollment();

          }


          this.cdr.detectChanges();


          window.alert(
            'Progress updated successfully.'
          );

        },


        error: (err) => {

          console.error(
            'Progress update failed:',
            err
          );

          this.updatingProgress = false;

          this.cdr.detectChanges();


          window.alert(
            'Unable to update progress. Please try again.'
          );

        }

      });

  }


  // ==========================================
  // SUBMIT ASSESSMENT
  // ==========================================

  submitAssessment(): void {

    if (!this.enrollment?.enrollmentId) {

      return;

    }


    const score =
      Number(this.scoreValue);


    if (
      isNaN(score) ||
      score < 0 ||
      score > 100
    ) {

      window.alert(
        'Assessment score must be between 0 and 100.'
      );

      return;

    }


    this.submittingScore = true;

    this.cdr.detectChanges();


    this.learningService
      .submitAssessment(
        this.enrollment.enrollmentId,
        score
      )
      .subscribe({

        next: (response) => {

          console.log(
            'Assessment submitted:',
            response
          );


          this.enrollment.score =
            score;


          this.scoreValue =
            score;


          this.submittingScore = false;

          this.cdr.detectChanges();


          window.alert(
            'Assessment score submitted successfully.'
          );

        },


        error: (err) => {

          console.error(
            'Assessment submission failed:',
            err
          );

          this.submittingScore = false;

          this.cdr.detectChanges();


          window.alert(
            'Unable to submit assessment score. Please try again.'
          );

        }

      });

  }


  // ==========================================
  // COMPLETE COURSE
  // ==========================================

  completeCourse(): void {

    if (!this.enrollment?.enrollmentId) {

      return;

    }


    const confirmed =
      window.confirm(
        'Are you sure you want to mark this course as completed?'
      );


    if (!confirmed) {

      return;

    }


    this.completingCourse = true;

    this.cdr.detectChanges();


    this.learningService
      .completeCourse(
        this.enrollment.enrollmentId
      )
      .subscribe({

        next: (response) => {

          console.log(
            'Course completed:',
            response
          );


          this.completingCourse = false;


          /*
           * Reload the complete enrollment
           * so completedAt and other backend
           * fields are refreshed.
           */

          this.loadEnrollment();


          window.alert(
            'Course completed successfully.'
          );

        },


        error: (err) => {

          console.error(
            'Course completion failed:',
            err
          );

          this.completingCourse = false;

          this.cdr.detectChanges();


          window.alert(
            'Unable to complete course. Please try again.'
          );

        }

      });

  }


  // ==========================================
  // GENERATE CERTIFICATE
  // ==========================================

  generateCertificate(): void {

    if (!this.enrollment?.enrollmentId) {

      return;

    }


    this.generatingCertificate = true;

    this.cdr.detectChanges();


    this.learningService
      .generateCertificate(
        this.enrollment.enrollmentId
      )
      .subscribe({

        next: (response) => {

          console.log(
            'Certificate generated:',
            response
          );


          this.generatingCertificate = false;

          this.cdr.detectChanges();


          window.alert(
            'Certificate generated successfully.'
          );

        },


        error: (err) => {

          console.error(
            'Certificate generation failed:',
            err
          );

          this.generatingCertificate = false;

          this.cdr.detectChanges();


          window.alert(
            'Unable to generate certificate. Please try again.'
          );

        }

      });

  }


  // ==========================================
  // RETRY
  // ==========================================

  retry(): void {

    this.loadEnrollment();

  }

}