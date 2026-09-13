import { Injectable } from '@angular/core';
import {
  HttpClient
} from '@angular/common/http';

import {
  Observable
} from 'rxjs';

import {
  environment
} from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {

  private readonly apiUrl =
    environment.apiUrl;

  constructor(
    private http: HttpClient
  ) {}

  createEnrollment(
    empId: string,
    courseId: string
  ): Observable<any> {

    return this.http.post<any>(
      `${this.apiUrl}/api/learning/enrollments`,
      null,
      {
        params: {
          empId,
          courseId
        }
      }
    );
  }

  getEmployeeEnrollments(
    empId: string
  ): Observable<any[]> {

    return this.http.get<any[]>(
      `${this.apiUrl}/api/learning/enrollments/employee/${empId}`
    );
  }

  getAllEnrollments(): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.apiUrl}/api/learning/enrollments`
    );
  }

  updateProgress(
    enrollmentId: string,
    progress: number
  ): Observable<any> {

    return this.http.put<any>(
      `${this.apiUrl}/api/learning/progress/${enrollmentId}`,
      null,
      {
        params: {
          progress
        }
      }
    );
  }

  submitAssessment(
    enrollmentId: string,
    score: number
  ): Observable<any> {

    return this.http.post<any>(
      `${this.apiUrl}/api/learning/progress/${enrollmentId}/assessment`,
      null,
      {
        params: {
          score
        }
      }
    );
  }

  completeCourse(
    enrollmentId: string
  ): Observable<any> {

    return this.http.post<any>(
      `${this.apiUrl}/api/learning/progress/${enrollmentId}/complete`,
      null
    );
  }

  generateCertificate(
    enrollmentId: string
  ): Observable<any> {

    return this.http.post<any>(
      `${this.apiUrl}/api/learning/certificates/${enrollmentId}`,
      null
    );
  }

}