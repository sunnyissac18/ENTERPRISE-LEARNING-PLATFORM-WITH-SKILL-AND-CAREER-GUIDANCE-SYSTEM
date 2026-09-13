import { Routes } from '@angular/router';

import { CourseListComponent } from './learning/course-list/course-list';
import { CertificationListComponent } from './certifications/certification-list.component/certification-list.component';
import { Career } from './pages/career/career';
import { Jobs } from './pages/jobs/jobs';
import { Analytics } from './pages/analytics/analytics';
import { EmployeesComponent } from './pages/employees/employees';

import { roleGuard } from './auth/role.guard';
import { AccessDeniedComponent } from './pages/access-denied/access-denied.component';
import { Enrollment } from './learning/enrollment/enrollment';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full',
  },
  {
    path: 'dashboard',
    loadComponent: () => import('./pages/dashboard/dashboard').then((m) => m.DashboardComponent),
  },
  {
    path: 'skills',
    loadComponent: () => import('./pages/skills/skills').then((m) => m.SkillsComponent),
  },
  {
    path: 'learning/enrollment/:enrollmentId',
    component: Enrollment,
  },
  {
    path: 'learning/courses/:courseId/enroll',
    component: Enrollment,
  },
  {
    path: 'learning/courses/:courseId',
    loadComponent: () =>
      import('./learning/course-details/course-details').then((m) => m.CourseDetailsComponent),
  },

  {
    path: 'learning/courses',
    component: CourseListComponent,
  },

  {
    path: 'certifications',
    component: CertificationListComponent,
  },

  {
    path: 'career',
    component: Career,
  },

  {
    path: 'jobs',
    component: Jobs,
  },

  {
    path: 'employees',
    canActivate: [roleGuard(['ROLE_ADMIN', 'ROLE_HR', 'ROLE_TRAINING_MANAGER'])],
    component: EmployeesComponent,
  },

  {
    path: 'analytics',
    canActivate: [roleGuard(['ROLE_ADMIN', 'ROLE_HR', 'ROLE_TRAINING_MANAGER'])],
    component: Analytics,
  },

  {
    path: 'access-denied',
    component: AccessDeniedComponent,
  },
];
