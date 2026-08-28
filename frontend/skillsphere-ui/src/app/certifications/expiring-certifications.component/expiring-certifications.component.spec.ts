import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExpiringCertificationsComponent } from './expiring-certifications.component';

describe('ExpiringCertificationsComponent', () => {
  let component: ExpiringCertificationsComponent;
  let fixture: ComponentFixture<ExpiringCertificationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExpiringCertificationsComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ExpiringCertificationsComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
