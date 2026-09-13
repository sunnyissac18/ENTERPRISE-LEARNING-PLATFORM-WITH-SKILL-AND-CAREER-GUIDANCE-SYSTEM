import { Injectable } from '@angular/core';

import {
  HttpClient
} from '@angular/common/http';

import {
  forkJoin,
  map,
  Observable
} from 'rxjs';

import {
  environment
} from '../../environments/environment';


/*
 * Company / Management dashboard
 */

export interface DashboardData {

  skills: number;

  courses: number;

  completion: number;

  careerPlans: number;

  promotions: number;

  activeCertifications: number;

  expiringCertifications: number;

  skillGaps: {
    name: string;
    gap: number;
  }[];

}


/*
 * Employee dashboard
 */

export interface EmployeeDashboardData {

  username: string;

  courses: number;

  completedCourses: number;

  inProgressCourses: number;

  completion: number;

}


@Injectable({
  providedIn: 'root'
})
export class DashboardService {

  private readonly apiUrl =
    environment.apiUrl;


  constructor(
    private http: HttpClient
  ) {}


  /*
   * ==========================================
   * COMPANY DASHBOARD
   * ==========================================
   *
   * Used by:
   * - Admin
   * - Training Manager
   * - HR
   */

  getDashboardData():
    Observable<DashboardData> {

    return forkJoin({

      skills:
        this.http.get<any[]>(
          `${this.apiUrl}/api/skills/catalog`
        ),

      courses:
        this.http.get<any[]>(
          `${this.apiUrl}/api/learning/courses`
        ),

      certifications:
        this.http.get<any[]>(
          `${this.apiUrl}/api/certifications`
        ),

      analytics:
        this.http.get<any>(
          `${this.apiUrl}/api/career/analytics`
        )

    }).pipe(

      map(result => {

        const analytics =
          result.analytics ?? {};


        return {

          skills:
            Array.isArray(result.skills)
              ? result.skills.length
              : 0,


          courses:
            Array.isArray(result.courses)
              ? result.courses.length
              : 0,


          completion:
            this.getNumber(
              analytics,
              [
                'completion',
                'completionRate',
                'averageProgress'
              ]
            ),


          careerPlans:
            this.getNumber(
              analytics,
              [
                'careerPlans',
                'totalPlans',
                'activePlans',
                'totalCareerPlans'
              ]
            ),


          promotions:
            this.getNumber(
              analytics,
              [
                'promotions',
                'promotionEligibleEmployees',
                'promotionEligible'
              ]
            ),


          skillGaps:
            this.extractSkillGaps(
              analytics
            ),

          activeCertifications: Array.isArray(result.certifications) 
            ? result.certifications.filter((c: any) => c.status === 'VALID').length 
            : 0,

          expiringCertifications: Array.isArray(result.certifications) 
            ? result.certifications.filter((c: any) => c.status === 'PENDING_RENEWAL' || c.status === 'EXPIRED').length 
            : 0

        };

      })

    );

  }


  /*
   * ==========================================
   * NUMBER HELPER
   * ==========================================
   */

  private getNumber(
    data: any,
    fields: string[]
  ): number {

    for (const field of fields) {

      if (
        data &&
        data[field] !== undefined &&
        data[field] !== null
      ) {

        return Number(
          data[field]
        );

      }

    }

    return 0;

  }


  /*
   * ==========================================
   * SKILL GAP HELPER
   * ==========================================
   */

  private extractSkillGaps(
    data: any
  ): {
    name: string;
    gap: number;
  }[] {

    if (
      Array.isArray(
        data?.skillGaps
      )
    ) {

      return data.skillGaps.map(
        (gap: any) => ({

          name:
            gap.name ??
            gap.skillName ??
            'Unknown',

          gap:
            Number(
              gap.gap ??
              gap.value ??
              0
            )

        })
      );

    }


    return [];

  }

}
