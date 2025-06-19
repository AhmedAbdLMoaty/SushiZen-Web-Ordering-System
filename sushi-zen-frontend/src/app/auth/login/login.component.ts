import {
  Component,
  OnInit,
  OnDestroy,
  Inject,
  PLATFORM_ID,
} from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
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
import { StructuredDataService } from '../../services/structured-data.service';

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
export class LoginComponent implements OnInit, OnDestroy {
  loginForm!: FormGroup;
  returnUrl: string = '';
  addToCartItemId: string | null = null;
  loading = false;
  error = '';
  structuredData: any;
  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private cartService: CartService,
    private menuService: MenuService,
    private structuredDataService: StructuredDataService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    // Initialize structured data for rich results
    this.initStructuredData();
  }

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
          // Add this debug log
          console.log('Current user:', this.authService.getCurrentUser());

          // Check if the user is kitchen staff and redirect accordingly
          if (this.authService.isKitchenStaff()) {
            console.log('User is kitchen staff, redirecting to /kitchen');
            this.router.navigate(['/kitchen']);
          } else if (this.addToCartItemId) {
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
  private initStructuredData(): void {
    if (!isPlatformBrowser(this.platformId)) return;
    this.structuredDataService.addJsonLdFromAPI(
      this.structuredDataService.getWebsiteData(),
      'website-jsonld'
    );

    this.structuredDataService.addJsonLdFromAPI(
      this.structuredDataService.getRestaurantDataFromAPI(),
      'restaurant-jsonld'
    );

    this.structuredDataService.addJsonLdFromAPI(
      this.structuredDataService.getBreadcrumbData([
        { name: 'Home', url: 'https://sushizen.com' },
        { name: 'Login', url: 'https://sushizen.com/login' },
      ]),
      'breadcrumb-jsonld'
    );
  }

  getCurrentUrl(): string {
    if (isPlatformBrowser(this.platformId)) {
      return window.location.href;
    }
    return 'https://sushizen.com/login';
  }

  ngOnDestroy(): void {
    if (!isPlatformBrowser(this.platformId)) return;

    this.structuredDataService.removeJsonLd('website-jsonld');
    this.structuredDataService.removeJsonLd('restaurant-jsonld');
    this.structuredDataService.removeJsonLd('breadcrumb-jsonld');
  }
}
