import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SkillProfileService {

  private baseUrl = `${environment.apiUrl}/api/skills`;

  constructor(
    private http: HttpClient
  ) {}

  getProfile(empId: string) {
    return this.http.get(
      `${this.baseUrl}/profile/${empId}`
    );
  }

  getCatalog() {
    return this.http.get(
      `${this.baseUrl}/catalog`
    );
  }

  createSkill(skill: any) {
    return this.http.post(
      `${this.baseUrl}/catalog`,
      skill
    );
  }

  addSkillToProfile(empId: string, skillId: string) {
    return this.http.post(
      `${this.baseUrl}/profile/${empId}/skills/${skillId}`,
      {}
    );
  }

  updateSkillProficiency(empId: string, skillId: string, proficiency: number) {
    return this.http.put(
      `${this.baseUrl}/profile/${empId}/skills/${skillId}?proficiency=${proficiency}`,
      {}
    );
  }
}
