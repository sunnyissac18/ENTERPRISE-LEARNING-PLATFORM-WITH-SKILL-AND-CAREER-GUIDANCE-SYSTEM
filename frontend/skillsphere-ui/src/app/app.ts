import { Component } from '@angular/core';
import {
  RouterLink,
  RouterLinkActive,
  RouterOutlet,
  Router,
  NavigationEnd
} from '@angular/router';

import { KeycloakAuthService } from './auth/keycloak.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,

  imports: [
    RouterLink,
    RouterLinkActive,
    RouterOutlet
  ],

  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class AppComponent {

  currentRouteName: string = 'Dashboard';

  constructor(
    public auth: KeycloakAuthService,
    private router: Router
  ) {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe((event: any) => {
      // Very simple URL to Title mapping
      const url = event.urlAfterRedirects || event.url;
      if (url.includes('/skills')) this.currentRouteName = 'Skills';
      else if (url.includes('/learning')) this.currentRouteName = 'Learning';
      else if (url.includes('/certifications')) this.currentRouteName = 'Certifications';
      else if (url.includes('/career')) this.currentRouteName = 'Career';
      else if (url.includes('/jobs')) this.currentRouteName = 'Jobs';
      else if (url.includes('/employees')) this.currentRouteName = 'Employees';
      else if (url.includes('/analytics')) this.currentRouteName = 'Analytics';
      else this.currentRouteName = 'Dashboard';
    });
  }

  canViewAnalytics(): boolean {

    return (
      this.auth.hasRole('ROLE_ADMIN') ||
      this.auth.hasRole('ROLE_HR') ||
      this.auth.hasRole('ROLE_TRAINING_MANAGER')
    );

  }
  canViewEmployees(): boolean {

  return (
    this.auth.hasRole('ROLE_ADMIN') ||
    this.auth.hasRole('ROLE_HR')
  );

}

  getInitials(): string {

    const name = this.auth.getFullName();

    if (!name || name === 'User') {
      return 'U';
    }

    const parts = name.trim().split(/\s+/);

    if (parts.length === 1) {
      return parts[0].substring(0, 2).toUpperCase();
    }

    return (
      parts[0].charAt(0) +
      parts[parts.length - 1].charAt(0)
    ).toUpperCase();

  }

  getPrimaryRole(): string {

    const roles = this.auth.getRoles();

    if (roles.includes('ROLE_ADMIN')) {
      return 'Administrator';
    }

    if (roles.includes('ROLE_HR')) {
      return 'HR Manager';
    }

    if (roles.includes('ROLE_TRAINING_MANAGER')) {
      return 'Training Manager';
    }

    if (roles.includes('ROLE_EMPLOYEE')) {
      return 'Employee';
    }

    return 'User';

  }

  async logout(): Promise<void> {
    await this.auth.logout();
  }

}