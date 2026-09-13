import { Component } from '@angular/core';

@Component({
  selector: 'app-access-denied',
  standalone: true,
  template: `
    <div class="access-denied">
      <h1>Access Denied</h1>
      <p>You do not have permission to access this page.</p>
    </div>
  `
})
export class AccessDeniedComponent {}