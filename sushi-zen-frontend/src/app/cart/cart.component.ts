import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CartService } from '../services/cart.service';
import { Router } from '@angular/router';
import { MenuService } from '../services/menu.service';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './cart.component.html',
  styleUrls: ['./cart.component.scss'],
})
export class CartComponent implements OnInit {
  cart: any = null;
  loading = true;
  error = false;
  displayedColumns: string[] = ['itemId', 'quantity', 'totalPrice', 'actions'];
  menuItems: any[] = []; // To store the menu items for display
  deliveryFee = 5.0; // Default delivery fee
  discount = 0;
  promoCode = '';

  constructor(
    private cartService: CartService,
    private router: Router,
    private menuService: MenuService,
    private snackBar: MatSnackBar // Add this
  ) {}

  ngOnInit(): void {
    this.loadCart();
    this.loadMenuItems();
  }

  loadCart(): void {
    this.loading = true;
    this.cartService.getUserCart().subscribe({
      next: (data) => {
        this.cart = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading cart:', err);
        this.error = true;
        this.loading = false;
      },
    });
  }

  loadMenuItems(): void {
    this.menuService.getAllMenuItems().subscribe({
      next: (items) => {
        this.menuItems = items;
      },
      error: (err) => {
        console.error('Error loading menu items:', err);
      },
    });
  }

  getItemImage(itemId: string): string {
    const item = this.menuItems.find((item) => item.itemId === itemId);
    return item
      ? item.itemPicture || '/assets/images/default-sushi.jpg'
      : '/assets/images/default-sushi.jpg';
  }

  getItemName(itemId: string): string {
    const item = this.menuItems.find((item) => item.itemId === itemId);
    return item ? item.itemName : 'Item';
  }

  getItemDescription(itemId: string): string {
    const item = this.menuItems.find((item) => item.itemId === itemId);
    return item ? item.itemDescription : 'Item description not available';
  }

  increaseQuantity(item: any): void {
    // Clone the cart item to modify
    const updatedItem = { ...item, quantity: item.quantity + 1 };
    // Update item price based on quantity
    const menuItem = this.menuItems.find((m) => m.itemId === item.itemId);
    if (menuItem) {
      updatedItem.totalPrice = menuItem.itemPrice * updatedItem.quantity;
    }

    // For simplicity, remove and re-add with new quantity
    this.cartService.removeItemFromCart(item.itemId).subscribe({
      next: () => {
        this.cartService.addItemToCart(updatedItem).subscribe({
          next: () => {
            this.loadCart();
          },
          error: (err) => console.error('Error updating item:', err),
        });
      },
      error: (err) => console.error('Error removing item:', err),
    });
  }

  decreaseQuantity(item: any): void {
    if (item.quantity <= 1) {
      this.removeItem(item.itemId);
      return;
    }

    // Clone the cart item to modify
    const updatedItem = { ...item, quantity: item.quantity - 1 };
    // Update item price based on quantity
    const menuItem = this.menuItems.find((m) => m.itemId === item.itemId);
    if (menuItem) {
      updatedItem.totalPrice = menuItem.itemPrice * updatedItem.quantity;
    }

    // For simplicity, remove and re-add with new quantity
    this.cartService.removeItemFromCart(item.itemId).subscribe({
      next: () => {
        this.cartService.addItemToCart(updatedItem).subscribe({
          next: () => {
            this.loadCart();
          },
          error: (err) => console.error('Error updating item:', err),
        });
      },
      error: (err) => console.error('Error removing item:', err),
    });
  }

  removeItem(itemId: string): void {
    this.cartService.removeItemFromCart(itemId).subscribe({
      next: () => {
        this.loadCart();
      },
      error: (err) => {
        console.error('Error removing item:', err);
      },
    });
  }

  calculateTotal(): number {
    if (!this.cart || !this.cart.totalPrice) return 0;
    return this.cart.totalPrice + this.deliveryFee - this.discount;
  }

  applyPromoCode(): void {
    // Simple promo code implementation
    if (this.promoCode.toLowerCase() === 'sushi10') {
      this.discount = this.cart.totalPrice * 0.1; // 10% discount
    } else if (this.promoCode.toLowerCase() === 'welcome') {
      this.discount = 5; // $5 off
    } else {
      this.discount = 0;
    }
  }

  checkout(): void {
    // First clear the cart
    this.cartService.clearCart().subscribe({
      next: () => {
        console.log('Cart cleared successfully');
        // Show a success message or notification
        this.showCheckoutSuccess();
        // Navigate to checkout completion page
        this.router.navigate(['/checkout-success']);
      },
      error: (err) => {
        console.error('Error clearing cart:', err);
        // Still navigate to checkout but may need to handle the error
        this.router.navigate(['/checkout']);
      },
    });
  }

  // Add a method to show success message
  private showCheckoutSuccess(): void {
    this.snackBar.open('Order placed successfully!', 'Close', {
      duration: 3000,
      panelClass: ['success-snackbar'],
    });
  }

  private showNotification(message: string): void {
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
      }, 3000);
    }, 100);
  }
}
