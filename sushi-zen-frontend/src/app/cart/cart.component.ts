import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { CartService } from '../services/cart.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    CommonModule,
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

  constructor(private cartService: CartService, private router: Router) {}

  ngOnInit(): void {
    this.loadCart();
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

  checkout(): void {
    // Implement checkout functionality or navigate to checkout page
    this.router.navigate(['/checkout']);
  }
}
