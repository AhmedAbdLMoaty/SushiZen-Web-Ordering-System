import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MenuService } from '../services/menu.service';
import { CartService } from '../services/cart.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [
    CommonModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
})
export class MenuComponent implements OnInit {
  menuItems: any[] = [];
  loading = true;
  error = false;

  constructor(
    private menuService: MenuService,
    private cartService: CartService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadMenuItems();
  }

  loadMenuItems(): void {
    this.loading = true;
    this.menuService.getAllMenuItems().subscribe({
      next: (items) => {
        this.menuItems = items;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading menu items:', err);
        this.error = true;
        this.loading = false;
      },
    });
  }

  addToCart(item: any): void {
    // Check if user is logged in
    if (this.authService.isLoggedIn()) {
      this.addItemToCart(item);
    } else {
      // If not logged in, redirect to login with return URL that includes item
      const returnUrl = `/menu?addToCart=${item.itemId}`;
      this.router.navigate(['/login'], { queryParams: { returnUrl } });
    }
  }

  private addItemToCart(item: any): void {
    const cartItem = {
      itemId: item.itemId,
      quantity: 1,
      totalPrice: item.itemPrice,
    };

    this.cartService.addItemToCart(cartItem).subscribe({
      next: () => {
        // Show success message or update cart count
        this.showNotification(`${item.itemName} added to cart!`);
      },
      error: (err) => {
        console.error('Error adding to cart:', err);
        this.showNotification('Could not add item to cart. Please try again.');
      },
    });
  }

  private showNotification(message: string): void {
    // Simple implementation - you might want to use a more sophisticated notification system
    const notification = document.createElement('div');
    notification.className = 'notification';
    notification.textContent = message;
    document.body.appendChild(notification);

    setTimeout(() => {
      notification.classList.add('show');
      setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
          document.body.removeChild(notification);
        }, 300);
      }, 2000);
    }, 100);
  }
}
