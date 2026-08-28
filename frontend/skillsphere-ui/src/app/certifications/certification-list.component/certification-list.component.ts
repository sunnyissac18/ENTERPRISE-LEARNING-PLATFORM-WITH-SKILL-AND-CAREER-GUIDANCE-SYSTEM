import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CertificationService } from '../certification.service';

@Component({
  selector: 'app-certification-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './certification-list.component.html',
})

export class CertificationListComponent implements OnInit {
  
  certifications: any[] = [];

  constructor(
    private certificationService: CertificationService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    const empId = '828a95e0-63e3-442d-af74-755bcf706b50';

    this.certificationService.getEmployeeCertifications(empId).subscribe({
      next: (data: any) => {
        console.log('CERTIFICATION DATA:', data);
        console.log('IS ARRAY:', Array.isArray(data));

        this.certifications = data;
        this.cdr.detectChanges();
      },

      error: (error) => {
        console.error('CERTIFICATION API ERROR:', error);
      },
    });
  }
}
