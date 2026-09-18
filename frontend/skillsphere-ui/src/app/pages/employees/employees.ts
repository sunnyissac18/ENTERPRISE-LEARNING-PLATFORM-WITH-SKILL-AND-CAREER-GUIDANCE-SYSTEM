import { CommonModule } from '@angular/common';
import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { EmployeeService } from '../../services/employee.service';

@Component({
  selector: 'app-employees',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './employees.html',
  styleUrl: './employees.scss'
})
export class EmployeesComponent implements OnInit {
  employees: any[] = [];
  loading = true;
  syncing = false;

  constructor(
    private employeeService: EmployeeService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.employeeService.getAllEmployees().subscribe({
      next: (data: any[]) => {
        this.employees = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err: any) => {
        console.error('Failed to load employees', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  syncKeycloak(): void {
    if (this.syncing) return;
    this.syncing = true;
    
    this.employeeService.syncEmployees().subscribe({
      next: () => {
        // Reload employees after sync
        this.employeeService.getAllEmployees().subscribe({
          next: (data: any[]) => {
            this.employees = data;
            this.syncing = false;
            this.cdr.detectChanges();
          },
          error: (err: any) => {
            console.error('Failed to reload employees after sync', err);
            this.syncing = false;
            this.cdr.detectChanges();
          }
        });
      },
      error: (err: any) => {
        console.error('Failed to sync employees', err);
        this.syncing = false;
        this.cdr.detectChanges();
      }
    });
  }

  formatRole(role: string): string {
    if (!role) return 'Unassigned';
    return role.split('_').map(word => 
      word.charAt(0).toUpperCase() + word.slice(1).toLowerCase()
    ).join(' ');
  }
}
