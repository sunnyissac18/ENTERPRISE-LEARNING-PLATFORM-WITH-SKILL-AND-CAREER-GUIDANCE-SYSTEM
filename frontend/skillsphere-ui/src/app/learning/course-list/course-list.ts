import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

import {
  ChangeDetectorRef,
  Component,
  OnInit
} from '@angular/core';

import { LearningService } from '../learning';
import { EnrollmentService } from '../../services/enrollment.service';
import { EmployeeService } from '../../services/employee.service';
import { KeycloakAuthService } from '../../auth/keycloak.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-course-list',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    FormsModule
  ],
  templateUrl: './course-list.html',
  styleUrl: './course-list.scss'
})
export class CourseListComponent implements OnInit {

  courses: any[] = [];
  enrollments: any[] = [];

  loading = true;
  error = false;

  isEmployee = false;
  isManagement = false;
  isHr = false;

  enrollingCourseId: string | null = null;
  enrolledCourseIds = new Set<string>();
  enrolledCourseMap = new Map<string, string>();

  showCreateCourseModal = false;
  newCourse = {
    title: '',
    description: '',
    duration: 0,
    type: 'ONLINE',
    instructor: '',
    rating: 0,
    active: true
  };

  constructor(
    private learningService: LearningService,
    private enrollmentService: EnrollmentService,
    private employeeService: EmployeeService,
    private auth: KeycloakAuthService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.isEmployee = this.auth.isEmployee();
    this.isManagement = this.auth.hasManagementAccess();
    this.isHr = this.auth.isHr();
    this.loadData();
  }

  private loadData(): void {
    this.loading = true;
    this.error = false;

    this.learningService.getCourses().subscribe({
      next: (data) => {
        this.courses = Array.isArray(data) ? data : [];
        if (this.isManagement) {
          this.loadEnrollments();
        } else if (this.isEmployee) {
          this.loadEmployeeEnrollments();
        } else {
          this.loading = false;
          this.cdr.detectChanges();
        }
      },
      error: (err) => {
        console.error('Course loading failed:', err);
        this.loading = false;
        this.error = true;
        this.cdr.detectChanges();
      }
    });
  }

  private loadEnrollments(): void {
    forkJoin({
      enrollments: this.enrollmentService.getAllEnrollments(),
      employees: this.employeeService.getAllEmployees()
    }).subscribe({
      next: (res) => {
        const enrollments = Array.isArray(res.enrollments) ? res.enrollments : [];
        const employees = Array.isArray(res.employees) ? res.employees : [];

        const employeeMap = new Map<string, string>();
        employees.forEach(emp => {
          if (emp.empId && emp.fullName) {
            employeeMap.set(emp.empId, emp.fullName);
          }
          if (emp.keycloakId && emp.fullName) {
            employeeMap.set(emp.keycloakId, emp.fullName);
          }
        });

        this.enrollments = enrollments.map(e => ({
          ...e,
          employeeName: employeeMap.get(e.empId) || e.empId
        }));
        
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Enrollment loading failed:', err);
        this.loading = false;
        this.error = true;
        this.cdr.detectChanges();
      }
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
        this.enrolledCourseIds.clear();
        this.enrolledCourseMap.clear();
        enrollments.forEach(e => {
          const cid = e.courseId || e.course?.id;
          if (cid) {
            this.enrolledCourseIds.add(cid);
            this.enrolledCourseMap.set(cid, e.enrollmentId || e.id);
          }
        });
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

  enroll(course: any): void {

    const courseId =
      course.courseId || course.id;

    if (!courseId) {

      console.error(
        'Course ID is missing'
      );

      return;
    }

    const empId = this.auth.getUserId();

    if (!empId) {

      console.error(
        'Employee ID could not be obtained from Keycloak'
      );

      return;
    }

    const confirmed = window.confirm(
      `Ready to start learning ${course.title}?`
    );

    if (!confirmed) {
      return;
    }

    this.enrollingCourseId = courseId;

    this.cdr.detectChanges();

    this.enrollmentService
      .createEnrollment(empId, courseId)
      .subscribe({

        next: (response) => {

          console.log(
            'Enrollment successful:',
            response
          );

          this.enrollingCourseId = null;

          this.cdr.detectChanges();

          if (response?.enrollmentId) {

            this.router.navigate([
              '/learning/enrollment',
              response.enrollmentId
            ]);

          } else {

            console.error(
              'Enrollment ID missing from response'
            );

          }

        },

        error: (err) => {

          console.error(
            'Enrollment failed:',
            err
          );

          this.enrollingCourseId = null;

          this.cdr.detectChanges();

          window.alert(
            'Unable to enroll in this course. Please try again.'
          );

        }

      });

  }

  retry(): void {
    this.loadData();
  }

  // --- Create Course ---

  canCreateCourse(): boolean {
    return this.auth.isAdmin() || this.auth.isTrainingManager();
  }

  openCreateCourseModal(): void {
    if (!this.canCreateCourse()) return;
    this.newCourse = {
      title: '',
      description: '',
      duration: 0,
      type: 'ONLINE',
      instructor: '',
      rating: 0,
      active: true
    };
    this.showCreateCourseModal = true;
  }

  closeCreateCourseModal(): void {
    this.showCreateCourseModal = false;
  }

  createCourse(): void {
    if (!this.newCourse.title || !this.newCourse.description) {
      alert('Please fill out all required fields.');
      return;
    }
    
    this.loading = true;
    this.learningService.createCourse(this.newCourse).subscribe({
      next: (res) => {
        console.log('Course created', res);
        this.closeCreateCourseModal();
        this.loadData();
      },
      error: (err) => {
        console.error('Failed to create course', err);
        alert('Failed to create course. Please try again.');
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

}