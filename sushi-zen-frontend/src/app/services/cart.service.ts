import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { AuthService } from './auth.service';

interface CartItem {
  itemId: string;
  quantity: number;
  totalPrice: number;
}

interface Cart {
  cartId: string;
  userId: string;
  items: CartItem[];
  totalPrice: number;
}

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private apiUrl = `${environment.apiUrl}/cart`;
  private cartItemsSubject = new BehaviorSubject<number>(0);
  cartItems$ = this.cartItemsSubject.asObservable();

  constructor(private http: HttpClient, private authService: AuthService) {
    // Load cart items count when service initializes
    this.loadCartCount();

    // Subscribe to auth status to reload cart when user logs in/out
    this.authService.currentUser$.subscribe((user) => {
      if (user) {
        this.loadCartCount();
      } else {
        this.cartItemsSubject.next(0);
      }
    });
  }

  private loadCartCount(): void {
    if (this.authService.isLoggedIn()) {
      this.getUserCart().subscribe({
        next: (cart: Cart) => {
          if (cart && cart.items) {
            const itemCount = cart.items.reduce(
              (sum: number, item: CartItem) => sum + item.quantity,
              0
            );
            this.cartItemsSubject.next(itemCount);
          }
        },
        error: () => {
          // Silent error handling for initial load
          this.cartItemsSubject.next(0);
        },
      });
    }
  }

  getUserCart(): Observable<Cart> {
    return this.http.get<Cart>(this.apiUrl);
  }

  addItemToCart(cartItem: CartItem): Observable<any> {
    return this.http.post<any>(this.apiUrl, cartItem).pipe(
      tap(() => {
        // Increment cart count
        const currentCount = this.cartItemsSubject.value;
        this.cartItemsSubject.next(currentCount + cartItem.quantity);
      })
    );
  }

  removeItemFromCart(itemId: string): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${itemId}`).pipe(
      tap(() => {
        // Update cart count after removal
        this.loadCartCount();
      })
    );
  }
}
