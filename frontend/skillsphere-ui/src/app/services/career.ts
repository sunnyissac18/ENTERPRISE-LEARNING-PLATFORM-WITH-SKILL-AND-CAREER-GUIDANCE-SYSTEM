import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CareerService {
  private baseUrl = `${environment.apiUrl}/api/career`;

  constructor(private http: HttpClient) {}

  getCareerPlans() {
    return this.http.get<any[]>(`${this.baseUrl}/plans`);
  }

  getCareerPlanByEmployee(empId: string) {
    return this.http.get<any[]>(`${this.baseUrl}/plans/employee/${empId}`);
  }

  createCareerPlan(data: any) {
    return this.http.post(`${this.baseUrl}/plans`, data);
  }

  updateCareerPlan(id: string, data: any) {
    return this.http.put(`${this.baseUrl}/plans/${id}`, data);
  }

  getJobs() {
    return this.http.get<any[]>(`${this.baseUrl}/jobs/active`);
  }

  createJob(data: any) {
    return this.http.post(`${this.baseUrl}/jobs`, data);
  }

  getAnalytics() {
    return this.http.get<any>(`${this.baseUrl}/analytics`);
  }
}
