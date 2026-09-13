import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SkillProfileService {

  private baseUrl = `${environment.apiUrl}/api/skills`;

  constructor(private http: HttpClient) {}

  getProfile(empId: string) {
    return this.http.get(
      `${this.baseUrl}/employee/${empId}`
    );
  }
}