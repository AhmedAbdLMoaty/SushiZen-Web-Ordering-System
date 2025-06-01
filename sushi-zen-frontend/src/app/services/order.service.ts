import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient, private authService: AuthService) {}

  // Get all orders - no authentication required
  getAllOrders(): Observable<any[]> {
    console.log(`Making ${this.apiUrl}/orders request without authentication`);
    return this.http.get<any[]>(`${this.apiUrl}/orders`);
  }

  // Get a specific order - no authentication required
  getOrder(orderId: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/orders/${orderId}`);
  }

  // Place an order - no authentication required
  placeOrder(order: any): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.post<any>(`${this.apiUrl}/orders`, order, { headers });
  }

  // Update order status - no authentication required
  updateOrderStatus(orderId: string, status: string): Observable<any> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
    });

    return this.http.put<any>(
      `${this.apiUrl}/kitchen/orders/${orderId}/status`,
      { status: status },
      { headers }
    );
  }
}
