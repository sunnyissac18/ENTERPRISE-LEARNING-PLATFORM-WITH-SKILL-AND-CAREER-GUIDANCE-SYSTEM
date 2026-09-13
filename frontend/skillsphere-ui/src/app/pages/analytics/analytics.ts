import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CareerService } from '../../services/career';

@Component({
  selector: 'app-analytics',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './analytics.html',
  styleUrl: './analytics.scss',
})
export class Analytics implements OnInit {
  analytics: any = null;

  constructor(
    private careerService: CareerService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.loadAnalytics();
  }

  loadAnalytics(): void {
    this.careerService.getAnalytics().subscribe({
      next: (data) => {
        console.log('Analytics:', data);

        this.analytics = data;

        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error loading analytics:', error);
      },
    });
  }
}
