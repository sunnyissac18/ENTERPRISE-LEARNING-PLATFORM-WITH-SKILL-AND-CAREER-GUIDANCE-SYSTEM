import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CertificationListComponent } from './certification-list.component';

describe('CertificationListComponent', () => {
  let component: CertificationListComponent;
  let fixture: ComponentFixture<CertificationListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CertificationListComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(CertificationListComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
