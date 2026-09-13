import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { from, throwError } from 'rxjs';
import { switchMap, catchError } from 'rxjs/operators';

import { KeycloakAuthService } from './keycloak.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const auth = inject(KeycloakAuthService);

  const isApiRequest =
    req.url.startsWith('http://localhost:8090');

  if (!isApiRequest) {
    return next(req);
  }

  return from(auth.updateToken()).pipe(

    switchMap(() => {

      const token = auth.getToken();

      if (!token) {
        console.error('No JWT token available');
        return throwError(
          () => new Error('No JWT token available')
        );
      }

      const authReq = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });

      return next(authReq);
    }),

    catchError(error => {

      console.error('JWT refresh failed:', error);

      return throwError(() => error);
    })
  );
};