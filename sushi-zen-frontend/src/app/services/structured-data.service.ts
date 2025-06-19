import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { Meta } from '@angular/platform-browser';
import { isPlatformBrowser } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class StructuredDataService {
  private structuredDataApiUrl = `${environment.apiUrl}/api/structured-data`;

  constructor(
    private meta: Meta,
    private http: HttpClient,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  addJsonLd(data: any, id: string): void {
    if (!data || !isPlatformBrowser(this.platformId)) {
      return;
    }

    this.removeJsonLd(id);

    const script = document.createElement('script');
    script.type = 'application/ld+json';
    script.id = id;
    script.text = JSON.stringify(data);
    document.head.appendChild(script);
  }

  removeJsonLd(id: string): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    const existingScript = document.getElementById(id);
    if (existingScript) {
      existingScript.remove();
    }
  }

  getRestaurantDataFromAPI(): Observable<any> {
    return this.http
      .get(`${this.structuredDataApiUrl}/restaurant`)
      .pipe(catchError(() => of(null)));
  }

  getMenuDataFromAPI(): Observable<any> {
    return this.http
      .get(`${this.structuredDataApiUrl}/menu`)
      .pipe(catchError(() => of(null)));
  }

  getMenuItemDataFromAPI(itemId: string): Observable<any> {
    return this.http
      .get(`${this.structuredDataApiUrl}/menu-item/${itemId}`)
      .pipe(catchError(() => of(null)));
  }

  getWebsiteDataFromAPI(): Observable<any> {
    return this.http
      .get(`${this.structuredDataApiUrl}/website`)
      .pipe(catchError(() => of(null)));
  }

  getBreadcrumbDataFromAPI(
    breadcrumbs: { name: string; url: string }[]
  ): Observable<any> {
    return this.http
      .post(`${this.structuredDataApiUrl}/breadcrumb`, breadcrumbs)
      .pipe(catchError(() => of(null)));
  }
  addJsonLdFromAPI(apiCall: Observable<any>, id: string): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    apiCall.subscribe({
      next: (data) => this.addJsonLd(data, id),
      error: (error) =>
        console.error(`Failed to load structured data for ${id}:`, error),
    });
  }

  getWebsiteData(): Observable<any> {
    return this.getWebsiteDataFromAPI();
  }

  getBreadcrumbData(
    breadcrumbs: { name: string; url: string }[]
  ): Observable<any> {
    return this.getBreadcrumbDataFromAPI(breadcrumbs);
  }

  getRegistrationServiceData(): Observable<any> {
    return this.http
      .get(`${this.structuredDataApiUrl}/registration-service`)
      .pipe(catchError(() => of(null)));
  }

  getCartData(currentDate: string): Observable<any> {
    return this.http
      .get(`${this.structuredDataApiUrl}/cart?date=${currentDate}`)
      .pipe(catchError(() => of(null)));
  }

  getKitchenData(): Observable<any> {
    return this.http
      .get(`${this.structuredDataApiUrl}/kitchen`)
      .pipe(catchError(() => of(null)));
  }
}
