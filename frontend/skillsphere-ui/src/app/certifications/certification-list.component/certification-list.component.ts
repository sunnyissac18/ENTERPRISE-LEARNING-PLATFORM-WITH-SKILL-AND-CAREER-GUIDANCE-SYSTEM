import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CertificationService } from '../certification.service';
import { EmployeeService } from '../../services/employee.service';
import { KeycloakAuthService } from '../../auth/keycloak.service';

@Component({
  selector: 'app-certification-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './certification-list.component.html',
  styleUrls: ['./certification-list.component.scss'],
})
export class CertificationListComponent implements OnInit {

  certifications: any[] = [];

  loading = true;
  error = false;

  /*
   * Role flags
   */
  isEmployee = false;
  isAdmin = false;
  isTrainingManager = false;
  isHr = false;
  isManagement = false;

  // View state for Management
  activeTab: 'certifications' | 'renewals' = 'certifications';

  // Modal states
  showCreateModal = false;
  showRenewModal = false;

  // Form states
  employees: any[] = [];
  newCertification = {
    empId: '',
    name: '',
    issuingOrganization: '',
    credentialId: '',
    issued: '',
    expiry: ''
  };

  renewals: any[] = [];
  selectedRenewalId: string | null = null;
  newExpiryDate: string = '';

  constructor(
    private certificationService: CertificationService,
    private employeeService: EmployeeService,
    private auth: KeycloakAuthService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit(): void {
    this.isEmployee = this.auth.isEmployee();
    this.isAdmin = this.auth.isAdmin();
    this.isTrainingManager = this.auth.isTrainingManager();
    this.isHr = this.auth.isHr();
    this.isManagement = this.auth.hasManagementAccess();

    if (this.isEmployee) {
      this.loadEmployeeCertifications();
    } else {
      this.loadManagementCertifications();
      this.loadAllRenewals();
      this.loadEmployees();
    }
  }

  loadEmployees(): void {
    if (this.employees.length > 0) return;
    this.employeeService.getAllEmployees().subscribe({
      next: (data) => {
        this.employees = data;
        this.cdr.detectChanges();
      }
    });
  }

  /*
   * Only Admin and Training Manager can perform
   * management actions (Renew, Report).
   * HR is view-only.
   */
  canManageCertifications(): boolean {
    return this.isAdmin || this.isTrainingManager;
  }

  loadEmployeeCertifications(): void {
    const keycloakId = this.auth.getUserId();

    if (!keycloakId) {
      console.error('No employee ID found');
      this.loading = false;
      return;
    }

    this.employeeService.getCurrentEmployee().subscribe({
      next: (me) => {
        if (!me) {
          console.error('Employee record not found for current user');
          this.loading = false;
          this.error = true;
          this.cdr.detectChanges();
          return;
        }

        this.certificationService.getEmployeeCertifications(me.empId).subscribe({
          next: (data: any) => {
            this.certifications = Array.isArray(data) ? data : [];
            this.loading = false;
            this.cdr.detectChanges();
          },
          error: (err) => {
            console.error('Certification load error:', err);
            this.loading = false;
            this.error = true;
            this.cdr.detectChanges();
          },
        });
      },
      error: (err) => {
        console.error('Failed to fetch current employee', err);
        this.loading = false;
        this.error = true;
        this.cdr.detectChanges();
      }
    });
  }

  loadManagementCertifications(): void {
    this.certificationService.getAllCertifications().subscribe({
      next: (data: any) => {
        this.certifications = Array.isArray(data) ? data : [];
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Certification load error:', err);
        this.loading = false;
        this.error = true;
        this.cdr.detectChanges();
      },
    });
  }

  retry(): void {
    this.loading = true;
    this.error = false;
    if (this.isEmployee) {
      this.loadEmployeeCertifications();
    } else {
      this.loadManagementCertifications();
    }
  }

  // --- Creation Logic ---
  openCreateModal(): void {
    if (!this.canManageCertifications()) return;
    
    // Fetch employees if not already loaded (should be loaded from init, but fallback)
    if (this.employees.length === 0) {
      this.loadEmployees();
    }

    this.newCertification = { empId: '', name: '', issuingOrganization: '', credentialId: '', issued: '', expiry: '' };
    this.showCreateModal = true;
    this.cdr.detectChanges();
  }

  closeCreateModal(): void {
    this.showCreateModal = false;
    this.cdr.detectChanges();
  }

  createCertification(): void {
    if (!this.newCertification.empId || !this.newCertification.name) return;

    this.loading = true;
    this.certificationService.register(this.newCertification).subscribe({
      next: (res) => {
        this.closeCreateModal();
        this.loadManagementCertifications();
      },
      error: (err) => {
        console.error('Failed to create certification', err);
        this.loading = false;
        this.error = true;
        this.cdr.detectChanges();
      }
    });
  }

  // --- Renewal Logic (Management/HR) ---
  requestRenewal(certId: string): void {
    const keycloakId = this.auth.getUserId();
    const me = this.employees.find(e => e.keycloakId === keycloakId);
    const requestedBy = me ? me.fullName : (this.auth.getUsername() || 'HR');

    this.loading = true;
    this.certificationService.requestRenewal(certId, requestedBy).subscribe({
      next: () => {
        alert('Renewal requested successfully!');
        this.loadManagementCertifications();
        this.loadAllRenewals();
      },
      error: (err) => {
        console.error('Failed to request renewal', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  // --- Renewal Logic (Management) ---
  switchTab(tab: 'certifications' | 'renewals'): void {
    this.activeTab = tab;
    if (tab === 'renewals') {
      this.loadAllRenewals();
    }
    this.cdr.detectChanges();
  }

  loadAllRenewals(): void {
    this.loading = true;
    this.certificationService.getAllRenewals().subscribe({
      next: (data: any) => {
        const raws = Array.isArray(data) ? data : [];
        
        // Map UUIDs to Names
        this.renewals = raws.map(renewal => {
          // Find certification name
          const cert = this.certifications.find(c => c.certId === renewal.certificationId);
          const certName = cert ? cert.name : renewal.certificationId;

          // Replace UUIDs if needed (but we already save names for requestedBy/approvedBy now)
          // For older records that have UUIDs, let's try to map them if they exist in employees
          const reqEmp = this.employees.find(e => e.empId === renewal.requestedBy || e.keycloakId === renewal.requestedBy);
          const appEmp = this.employees.find(e => e.empId === renewal.approvedBy || e.keycloakId === renewal.approvedBy);

          return {
            ...renewal,
            certName: certName,
            requestedByName: reqEmp ? reqEmp.fullName : renewal.requestedBy,
            approvedByName: appEmp ? appEmp.fullName : renewal.approvedBy
          };
        });

        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load renewals', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  hasRequestedRenewal(certId: string): boolean {
    return this.renewals.some(r => r.certificationId === certId && r.status === 'REQUESTED');
  }

  openRenewModal(renewalId: string): void {
    this.selectedRenewalId = renewalId;
    this.newExpiryDate = '';
    this.showRenewModal = true;
    this.cdr.detectChanges();
  }

  closeRenewModal(): void {
    this.showRenewModal = false;
    this.selectedRenewalId = null;
    this.cdr.detectChanges();
  }

  approveRenewal(): void {
    if (!this.selectedRenewalId || !this.newExpiryDate) return;

    const keycloakId = this.auth.getUserId();
    const me = this.employees.find(e => e.keycloakId === keycloakId);
    const approvedBy = me ? me.fullName : (this.auth.getUsername() || 'Admin');

    this.loading = true;
    this.certificationService.approveRenewal(this.selectedRenewalId, this.newExpiryDate, approvedBy).subscribe({
      next: () => {
        alert('Renewal approved successfully!');
        this.closeRenewModal();
        this.loadAllRenewals();
      },
      error: (err) => {
        console.error('Failed to approve renewal', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
}
