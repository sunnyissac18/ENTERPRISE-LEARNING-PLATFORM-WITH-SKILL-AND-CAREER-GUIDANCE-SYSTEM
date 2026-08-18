import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })

export class LearningService {

  private baseUrl ='http://localhost:8082/api/learning';

  constructor(private http: HttpClient) {}

  getCourses() {
    return this.http.get(`${this.baseUrl}/courses`);
  }

  getCourse(courseId: string) {
    return this.http.get(
      `${this.baseUrl}/courses/${courseId}`);
  }

  enroll(empId: string, courseId: string) {
    return this.http.post(
      `${this.baseUrl}/enrollments`,
      null,
      { params: { empId, courseId } }
    );
  }

  getEnrollments(empId: string) {
    return this.http.get(
      `${this.baseUrl}/enrollments/employee/${empId}`);
  }

  updateProgress(
      enrollmentId: string, progress: number) {
    return this.http.put(
      `${this.baseUrl}/progress/${enrollmentId}`,
      null,
      { params: { progress } }
    );
  }

  submitAssessment(
      enrollmentId: string, score: number) {
    return this.http.post(
      `${this.baseUrl}/progress/${enrollmentId}/assessment`,
      null,
      { params: { score } }
    );
  }
  
  completeCourse(enrollmentId: string) {
    return this.http.post(
      `${this.baseUrl}/progress/${enrollmentId}/complete`,
      null
    );
  }
}