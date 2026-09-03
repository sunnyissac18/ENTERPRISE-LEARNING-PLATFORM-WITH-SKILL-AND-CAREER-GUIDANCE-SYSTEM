import { Component, OnInit } from '@angular/core';
import { CertificationService } from '../certification.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-expiring-certifications',
  templateUrl: './expiring-certifications.component.html',
  imports: [CommonModule],
})
export class ExpiringCertificationsComponent implements OnInit {
  certifications: any[] = [];

  constructor(private certificationService: CertificationService) {}

  ngOnInit(): void {
    this.certificationService.getExpiring().subscribe((data: any) => {
      this.certifications = data;
    });
  }
}
