import { Injectable } from '@angular/core';
import Keycloak from 'keycloak-js';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class KeycloakAuthService {

  private keycloak = new Keycloak({
    url: environment.keycloak.url,
    realm: environment.keycloak.realm,
    clientId: environment.keycloak.clientId,
  });

  async init(): Promise<boolean> {
    return await this.keycloak.init({
      onLoad: 'login-required',
      checkLoginIframe: false,
    });
  }

  async login(): Promise<void> {
    await this.keycloak.login({
      redirectUri: window.location.origin,
    });
  }

  async logout(): Promise<void> {
    await this.keycloak.logout({
      redirectUri: window.location.origin,
    });
  }

  async updateToken(): Promise<boolean> {
    if (!this.keycloak.authenticated) {
      return false;
    }

    return await this.keycloak.updateToken(30);
  }

  getToken(): string | undefined {
    return this.keycloak.token;
  }

  getUsername(): string | undefined {
    return this.keycloak.tokenParsed?.['preferred_username'];
  }

  getFullName(): string {
    const parsed = this.keycloak.tokenParsed;
    if (parsed?.['name']) {
      return parsed['name'];
    }
    if (parsed?.['given_name'] || parsed?.['family_name']) {
      return `${parsed['given_name'] || ''} ${parsed['family_name'] || ''}`.trim();
    }
    return this.getUsername() || 'User';
  }

  getUserId(): string | undefined {
    return this.keycloak.tokenParsed?.['sub'];
  }

  getRoles(): string[] {
    const roles = this.keycloak.tokenParsed?.['realm_access']?.['roles'];

    return Array.isArray(roles) ? roles : [];
  }

  hasRole(role: string): boolean {
    const roles = this.getRoles();

    const normalizedRole = role.startsWith('ROLE_')
      ? role
      : `ROLE_${role}`;

    return roles.includes(role) || roles.includes(normalizedRole);
  }

  isLoggedIn(): boolean {
    return !!this.keycloak.authenticated;
  }

  isEmployee(): boolean {
    return this.hasRole('ROLE_EMPLOYEE');
  }

  isAdmin(): boolean {
    return this.hasRole('ROLE_ADMIN');
  }

  isTrainingManager(): boolean {
    return this.hasRole('ROLE_TRAINING_MANAGER');
  }

  isHr(): boolean {
    return this.hasRole('ROLE_HR');
  }

  hasManagementAccess(): boolean {
    return this.isAdmin() || this.isTrainingManager() || this.isHr();
  }
}