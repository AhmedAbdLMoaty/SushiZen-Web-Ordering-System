import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
} from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';
import { MenuService } from '../../services/menu.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    RouterModule,
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  returnUrl: string = '';
  addToCartItemId: string | null = null;
  loading = false;
  error = '';

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private cartService: CartService,
    private menuService: MenuService
  ) {}

  ngOnInit(): void {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });

    // Get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

    // Check if return URL contains an addToCart query param
    if (this.returnUrl.includes('addToCart=')) {
      const match = this.returnUrl.match(/addToCart=([^&]*)/);
      if (match && match[1]) {
        this.addToCartItemId = match[1];
      }
    }
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      return;
    }

    this.loading = true;
    this.authService
      .login({
        email: this.loginForm.controls['email'].value,
        password: this.loginForm.controls['password'].value,
      })
      .subscribe({
        next: () => {
          if (this.addToCartItemId) {
            this.addItemToCartAfterLogin(this.addToCartItemId);
          } else {
            this.router.navigate([this.returnUrl]);
          }
        },
        error: (error) => {
          this.error = 'Invalid email or password';
          this.loading = false;
        },
      });
  }

  private addItemToCartAfterLogin(itemId: string): void {
    this.menuService.getMenuItem(itemId).subscribe({
      next: (item) => {
        if (item) {
          const cartItem = {
            itemId: item.itemId,
            quantity: 1,
            totalPrice: item.itemPrice,
          };

          this.cartService.addItemToCart(cartItem).subscribe({
            next: () => {
              // Navigate to the menu and show success message
              this.router.navigate(['/menu']);
            },
            error: (err) => {
              console.error('Error adding item to cart after login:', err);
              this.router.navigate(['/menu']);
            },
          });
        } else {
          this.router.navigate(['/menu']);
        }
      },
      error: () => {
        // If there's an error getting the menu item, just navigate to menu
        this.router.navigate(['/menu']);
      },
    });
  }
}
