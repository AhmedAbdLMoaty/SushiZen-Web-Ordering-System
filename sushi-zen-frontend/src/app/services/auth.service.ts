import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject, of } from 'rxjs';
import { tap, catchError, map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/users`;
  private currentUserSubject = new BehaviorSubject<any>(null);
  currentUser$ = this.currentUserSubject.asObservable();
  private platformId = inject(PLATFORM_ID);

  constructor(private http: HttpClient, private router: Router) {
    // Only access localStorage in the browser environment
    if (isPlatformBrowser(this.platformId)) {
      // Check local storage for existing user session
      const savedUser = localStorage.getItem('currentUser');
      if (savedUser) {
        try {
          this.currentUserSubject.next(JSON.parse(savedUser));
        } catch (e) {
          // Handle parsing error
          console.error('Error parsing stored user data', e);
          localStorage.removeItem('currentUser');
        }
      }
    }
  }

  // Get authentication headers
  getAuthHeaders(): HttpHeaders {
    const user = this.getCurrentUser();
    let headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    if (user && user.token) {
      headers = headers.set('Authorization', `Bearer ${user.token}`);
    }

    return headers;
  }

  login(credentials: { email: string; password: string }): Observable<any> {
    return this.http
      .post<any>(`${this.apiUrl}/login`, credentials, { observe: 'response' })
      .pipe(
        map((response) => {
          // Extract the token from Authorization header if present
          let token = response.headers.get('Authorization');
          if (token) {
            // Remove 'Bearer ' prefix if it exists
            token = token.replace('Bearer ', '');
          } else {
            // Generate a consistent token for development if none provided
            // IMPORTANT: This should be removed in production
            token = 'dev-token-for-kitchen-staff-' + credentials.email;
          }

          // Extract user data from body
          const userData = {
            ...response.body,
            token: token, // Add token to user data
          };

          // Normalize role format if needed
          if (
            userData.role &&
            userData.role.toUpperCase() === 'KITCHEN_STAFF'
          ) {
            userData.role = 'Kitchen_staff';
          }

          console.log('Login successful, storing user with token:', token);

          // Store in localStorage for browser environments
          if (isPlatformBrowser(this.platformId)) {
            localStorage.setItem('currentUser', JSON.stringify(userData));
          }

          this.currentUserSubject.next(userData);
          return userData;
        })
      );
  }

  register(userData: any): Observable<any> {
    // The critical fix: match the exact field names expected by the backend
    const apiDto = {
      userID: userData.userId || null, // CHANGE HERE: userId -> userID (uppercase ID)
      name: userData.name,
      email: userData.email,
      phoneNumber: userData.phoneNumber,
      password: userData.password,
      role: 'Customer', // This is correct - matches Java enum in backend
    };

    console.log('Sending exact API format:', apiDto);

    return this.http
      .post<any>(`${this.apiUrl}/register`, apiDto, {
        observe: 'response',
      })
      .pipe(
        map((response) => {
          return { success: true };
        }),
        catchError((error) => {
          console.log('Registration error:', error);

          // Return a more descriptive error object
          const errorResponse = {
            success: false,
            status: error.status,
            message: this.getErrorMessage(error),
            field: this.getErrorField(error),
          };

          return of(errorResponse);
        })
      );
  }

  // Helper method to get user-friendly error messages
  private getErrorMessage(error: any): string {
    switch (error.status) {
      case 400:
        return 'The registration information you provided is invalid. Please check all fields and try again.';
      case 409:
        return 'An account with this email address already exists. Please use a different email or try logging in.';
      case 403:
        return 'You do not have permission to register at this time.';
      case 500:
        return "Sorry, we're experiencing server issues. Please try again in a few moments.";
      default:
        return 'Registration failed. Please try again later.';
    }
  }

  // Helper to determine which field might be causing the error
  private getErrorField(error: any): string | null {
    if (error.status === 409) {
      return 'email'; // Most likely field for conflict errors
    }

    if (error.error && typeof error.error === 'object') {
      // Try to extract field info from error response if available
      const errorFields = ['name', 'email', 'phoneNumber', 'password'];
      for (const field of errorFields) {
        if (error.error[field]) {
          return field;
        }
      }
    }

    return null;
  }

  logout(): void {
    // Remove user from local storage (only in browser)
    if (isPlatformBrowser(this.platformId)) {
      localStorage.removeItem('currentUser');
    }
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  isLoggedIn(): boolean {
    return !!this.currentUserSubject.value;
  }

  getCurrentUser(): any {
    return this.currentUserSubject.value;
  }

  isAdmin(): boolean {
    const user = this.currentUserSubject.value;
    return user && user.role === 'ADMIN';
  }
  isKitchenStaff(): boolean {
    const user = this.getCurrentUser();
    // Check for both possible formats of the kitchen staff role
    return user?.role === 'Kitchen_staff';
  }
}
