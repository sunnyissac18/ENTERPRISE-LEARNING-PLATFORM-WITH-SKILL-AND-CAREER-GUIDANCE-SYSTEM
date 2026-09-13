import { CommonModule } from '@angular/common';

import { ChangeDetectorRef, Component, OnInit } from '@angular/core';

import { ActivatedRoute, Router, RouterModule } from '@angular/router';

import { LearningService } from '../learning';
import { EnrollmentService } from '../../services/enrollment.service';
import { KeycloakAuthService } from '../../auth/keycloak.service';

@Component({
  selector: 'app-course-details',
  standalone: true,

  imports: [CommonModule, RouterModule],

  templateUrl: './course-details.html',
  styleUrl: './course-details.scss',
})
export class CourseDetailsComponent implements OnInit {
  course: any = null;

  loading = true;

  error = false;

  enrolling = false;

  isEmployee = false;
  isManagement = false;
  isHr = false;
  
  enrolledCourseId: string | null = null;
  enrolledEnrollmentId: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private learningService: LearningService,
    private enrollmentService: EnrollmentService,
    private auth: KeycloakAuthService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.isEmployee = this.auth.isEmployee();
    this.isManagement = this.auth.hasManagementAccess();
    this.isHr = this.auth.isHr();

    const courseId = this.route.snapshot.paramMap.get('courseId');

    if (!courseId) {
      this.error = true;
      this.loading = false;

      this.cdr.detectChanges();

      return;
    }

    this.loadCourse(courseId);
  }

  private loadCourse(courseId: string): void {
    this.loading = true;
    this.error = false;

    this.learningService.getCourse(courseId).subscribe({
      next: (data) => {
        console.log('Course details:', data);

        this.course = data;
        
        if (this.isEmployee) {
          this.loadEmployeeEnrollments();
        } else {
          this.loading = false;
          this.cdr.detectChanges();
        }
      },

      error: (err) => {
        console.error('Course details loading failed:', err);

        this.loading = false;
        this.error = true;

        this.cdr.detectChanges();
      },
    });
  }

  private loadEmployeeEnrollments(): void {
    const empId = this.auth.getUserId();
    if (!empId) {
      this.loading = false;
      this.cdr.detectChanges();
      return;
    }
    
    this.enrollmentService.getEmployeeEnrollments(empId).subscribe({
      next: (data) => {
        const enrollments = Array.isArray(data) ? data : [];
        const enrolled = enrollments.find(e => (e.courseId || e.course?.id) === (this.course?.courseId || this.course?.id));
        if (enrolled) {
          this.enrolledCourseId = this.course?.courseId || this.course?.id;
          this.enrolledEnrollmentId = enrolled.enrollmentId || enrolled.id;
        }
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Employee enrollments loading failed:', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  enroll(): void {
    const courseId = this.course?.courseId || this.course?.id;

    if (!courseId) {
      console.error('Course ID is missing');

      return;
    }

    const empId = this.auth.getUserId();

    if (!empId) {
      console.error('Employee ID could not be obtained from Keycloak');

      window.alert('Unable to identify the logged-in employee.');

      return;
    }

    const confirmed = window.confirm(`Ready to start learning ${this.course.title}?`);

    if (!confirmed) {
      return;
    }

    this.enrolling = true;

    this.cdr.detectChanges();

    console.log('Creating enrollment:', {
      empId,
      courseId,
    });

    this.enrollmentService.createEnrollment(empId, courseId).subscribe({
      next: (response) => {
        console.log('Enrollment successful:', response);

        this.enrolling = false;

        this.cdr.detectChanges();

        if (response?.enrollmentId) {
          this.router.navigate(['/learning/enrollment', response.enrollmentId]);
        } else {
          console.error('Enrollment ID missing from response:', response);

          window.alert('Enrollment was created, but the enrollment ID was not returned.');
        }
      },

      error: (err) => {
        console.error('Enrollment failed:', err);

        this.enrolling = false;

        this.cdr.detectChanges();

        window.alert('Unable to enroll in this course. Please try again.');
      },
    });
  }

  goBack(): void {
    this.router.navigate(['/learning/courses']);
  }
}
