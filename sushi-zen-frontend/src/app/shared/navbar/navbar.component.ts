import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatBadgeModule } from '@angular/material/badge';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatBadgeModule,
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent implements OnInit, OnDestroy {
  cartItemCount = 0;
  isMobileMenuOpen = false;
  isLoggedIn = false;
  isAdmin = false;
  isKitchenStaff = false;
  private subscriptions: Subscription[] = [];

  constructor(
    public authService: AuthService,
    private cartService: CartService
  ) {}

  ngOnInit(): void {
    this.subscriptions.push(
      this.cartService.cartItems$.subscribe((count) => {
        this.cartItemCount = count;
      })
    );

    // Subscribe to user changes to update role flags
    this.subscriptions.push(
      this.authService.currentUser$.subscribe((user) => {
        this.isLoggedIn = !!user;
        this.isAdmin = user?.role === 'ADMIN';
        this.isKitchenStaff = user?.role === 'Kitchen_staff';
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((sub) => sub.unsubscribe());
  }

  logout(): void {
    this.authService.logout();
  }

  toggleMobileMenu(): void {
    this.isMobileMenuOpen = !this.isMobileMenuOpen;
  }
}
