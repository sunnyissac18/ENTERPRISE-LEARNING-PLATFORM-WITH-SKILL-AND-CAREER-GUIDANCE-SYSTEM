import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  private baseUrl = `${environment.apiUrl}/api/employees`;

  constructor(private http: HttpClient) {}

  getAllEmployees() {
    return this.http.get<any[]>(`${this.baseUrl}`);
  }

  getCurrentEmployee() {
    return this.http.get<any>(`${this.baseUrl}/me`);
  }

  syncEmployees() {
    return this.http.post(`${this.baseUrl}/sync`, {}, { responseType: 'text' });
  }
}
