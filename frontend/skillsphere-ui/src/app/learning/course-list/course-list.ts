import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { LearningService } from '../learning';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-course-list',
  templateUrl: './course-list.html',
  imports: [CommonModule],
})
export class CourseListComponent implements OnInit {
  courses: any[] = [];

  constructor(
    private learningService: LearningService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.learningService.getCourses().subscribe({
      next: (data: any) => {
        this.courses = data;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('COURSE API ERROR:', error);
      }
    });
  }
}