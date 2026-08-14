import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SkillProfileService {

  private baseUrl = 'http://localhost:8080/api/skills';

  constructor(private http: HttpClient) {}

  getProfile(empId: string) {
    return this.http.get(
      `${this.baseUrl}/employee/${empId}`
    );
  }
}