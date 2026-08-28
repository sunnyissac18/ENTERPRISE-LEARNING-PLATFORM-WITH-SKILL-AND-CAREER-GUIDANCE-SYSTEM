import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})

export class CertificationService {
  
  private baseUrl ='http://localhost:8080/api/certifications';

  constructor(private http: HttpClient) {}

  register(data: any) {
    return this.http.post(
      this.baseUrl,
      data
    );
  }

  getById(id: string) {
    return this.http.get(
      `${this.baseUrl}/${id}`
    );
  }

  getEmployeeCertifications(empId: string) {
    return this.http.get(
      `${this.baseUrl}/employee/${empId}`
    );
  }

  update(id: string, data: any) {
    return this.http.put(
      `${this.baseUrl}/${id}`,
      data
    );
  }

  delete(id: string) {
    return this.http.delete(
      `${this.baseUrl}/${id}`
    );
  }

  getExpiring() {
    return this.http.get(
      `${this.baseUrl}/expiring`
    );
  }

  getExpired() {
    return this.http.get(
      `${this.baseUrl}/expired`
    );
  }

  requestRenewal(
      certificationId: string,
      requestedBy: string) {
    return this.http.post(
      `${this.baseUrl}/renewals/${certificationId}`,
      null,
      { params: { requestedBy } }
    );
  }

  approveRenewal(
      renewalId: string,
      newExpiry: string,
      approvedBy: string) {
    return this.http.put(
      `${this.baseUrl}/renewals/${renewalId}/approve`,
      null,
      { params: { newExpiry, approvedBy } }
    );
  }

  getCompliance(empId: string) {
    return this.http.get(
      `${this.baseUrl}/compliance/${empId}`
    );
  }

  getReport() {
    return this.http.get(
      `${this.baseUrl}/report`
    );
  }

  getAudit(certificationId: string) {
    return this.http.get(
      `${this.baseUrl}/${certificationId}/audit`
    );
  }

}
