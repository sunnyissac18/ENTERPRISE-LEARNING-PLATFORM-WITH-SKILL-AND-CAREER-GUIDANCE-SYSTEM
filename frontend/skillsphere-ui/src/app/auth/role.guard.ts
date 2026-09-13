import {
  CanActivateFn,
  Router
} from '@angular/router';

import { inject } from '@angular/core';

import { KeycloakAuthService } from './keycloak.service';

export function roleGuard(
  allowedRoles: string[]
): CanActivateFn {

  return () => {

    const auth = inject(KeycloakAuthService);
    const router = inject(Router);

    const allowed = allowedRoles.some(
      role => auth.hasRole(role)
    );

    if (allowed) {
      return true;
    }

    return router.parseUrl('/access-denied');
  };
}