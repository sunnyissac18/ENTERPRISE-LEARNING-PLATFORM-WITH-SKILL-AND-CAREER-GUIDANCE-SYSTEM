import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CertificationService {
  private baseUrl = `${environment.apiUrl}/api/certifications`;

  constructor(private http: HttpClient) {}

  register(data: any) {
    return this.http.post(this.baseUrl, data);
  }

  getById(id: string) {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  getEmployeeCertifications(empId: string) {
    return this.http.get(`${this.baseUrl}/employee/${empId}`);
  }

  getAllCertifications() {
    return this.http.get(this.baseUrl);
  }

  update(id: string, data: any) {
    return this.http.put(`${this.baseUrl}/${id}`, data);
  }

  delete(id: string) {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  getExpiring() {
    return this.http.get(`${this.baseUrl}/expiring`);
  }

  getExpired() {
    return this.http.get(`${this.baseUrl}/expired`);
  }

  requestRenewal(certificationId: string, requestedBy: string) {
    return this.http.post(`${this.baseUrl}/renewals/${certificationId}`, null, {
      params: { requestedBy },
    });
  }

  approveRenewal(renewalId: string, newExpiry: string, approvedBy: string) {
    return this.http.put(`${this.baseUrl}/renewals/${renewalId}/approve`, null, {
      params: { newExpiry, approvedBy },
    });
  }

  getAllRenewals() {
    return this.http.get(`${this.baseUrl}/renewals`);
  }

  getEmployeeRenewals(empId: string) {
    return this.http.get(`${this.baseUrl}/renewals/employee/${empId}`);
  }

  getReport() {
    return this.http.get(`${this.baseUrl}/report`);
  }

  getAudit(certificationId: string) {
    return this.http.get(`${this.baseUrl}/${certificationId}/audit`);
  }
}
