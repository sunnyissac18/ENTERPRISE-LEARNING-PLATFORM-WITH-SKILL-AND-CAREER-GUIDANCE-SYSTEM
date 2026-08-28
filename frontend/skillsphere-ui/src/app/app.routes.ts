import { Routes } from '@angular/router';
import { CourseListComponent } from './learning/course-list/course-list';
import { CertificationListComponent } from './certifications/certification-list.component/certification-list.component';

export const routes: Routes = [
    {
    path: 'learning/courses',
    component: CourseListComponent
  },
  {
    path: 'certifications',
    component: CertificationListComponent
  }
];
