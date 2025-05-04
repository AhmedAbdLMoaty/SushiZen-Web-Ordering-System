import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  ReactiveFormsModule,
  FormBuilder,
  FormGroup,
  Validators,
  AbstractControl,
  FormControl,
} from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { AuthService } from '../../services/auth.service';
import { CartService } from '../../services/cart.service';
import { MenuService } from '../../services/menu.service';
import { environment } from '../../../environments/environment';

// First, add this helper function at the top of your file (outside the component class)
function generateUUID() {
  // This function generates a UUID v4
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
    const r = (Math.random() * 16) | 0;
    const v = c === 'x' ? r : (r & 0x3) | 0x8;
    return v.toString(16);
  });
}

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatIconModule,
    MatSnackBarModule,
    RouterModule,
  ],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent implements OnInit {
  registerForm!: FormGroup;
  returnUrl: string = '';
  addToCartItemId: string | null = null;
  loading = false;
  error = '';
  hidePassword = true;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private cartService: CartService,
    private menuService: MenuService,
    private snackBar: MatSnackBar,
    private http: HttpClient // Add this line
  ) {}

  ngOnInit(): void {
    this.registerForm = this.formBuilder.group({
      name: ['', [Validators.required, Validators.minLength(2)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: [
        '',
        [Validators.required, Validators.pattern(/^\+?[0-9\s\-\(\)]+$/)],
      ],
      password: ['', [Validators.required, Validators.minLength(6)]],
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

    // Uncomment this to test direct registration:
    // this.testDirectRegistration();
  }

  // Helper getter for easy form access in template
  get f() {
    return this.registerForm.controls;
  }

  onSubmit(): void {
    // Reset any previous errors
    this.error = '';

    if (this.registerForm.invalid) {
      // Explicitly touch all fields to trigger validation messages
      Object.keys(this.registerForm.controls).forEach((key) => {
        const control = this.registerForm.get(key);
        control?.markAsTouched();
      });
      return;
    }

    this.loading = true;

    // Create the proper UserDTO structure with a generated UUID
    const userData = {
      userId: generateUUID(), // Generate a UUID here instead of passing null
      name: this.registerForm.get('name')?.value,
      email: this.registerForm.get('email')?.value,
      phoneNumber: this.registerForm.get('phoneNumber')?.value,
      password: this.registerForm.get('password')?.value,
      role: 'Customer',
    };

    console.log('Sending user data directly from component:', userData);

    this.authService.register(userData).subscribe({
      next: (result) => {
        this.loading = false;

        if (result.success) {
          this.snackBar.open(
            'Registration successful! You can now log in.',
            'Close',
            {
              duration: 5000,
              panelClass: ['success-snackbar'],
            }
          );

          this.router.navigate(['/login']);
        } else {
          // Handle specific error cases with clear messages
          this.error = result.message;

          // If we know which field is problematic, mark it as erroneous in the form
          if (result.field && this.registerForm.get(result.field)) {
            const fieldControl = this.registerForm.get(result.field);

            if (result.status === 409 && result.field === 'email') {
              fieldControl?.setErrors({ emailExists: true });

              // Add a helper message to guide the user
              this.snackBar
                .open('Try logging in instead?', 'Log In', {
                  duration: 8000,
                  panelClass: ['info-snackbar'],
                })
                .onAction()
                .subscribe(() => {
                  this.router.navigate(['/login'], {
                    queryParams: {
                      email: this.registerForm.get('email')?.value,
                    },
                  });
                });
            } else {
              fieldControl?.setErrors({ serverError: true });
            }
          }
        }
      },
      error: (error) => {
        // This should rarely happen since we're handling errors in the service
        this.loading = false;
        console.error('Unexpected registration error:', error);
        this.error = 'An unexpected error occurred. Please try again later.';
      },
    });
  }

  // Add this method to your component:
  testDirectRegistration() {
    const apiUrl = `${environment.apiUrl}/register`;

    const testData = {
      userID: generateUUID(),
      name: 'Test User',
      email: `test${new Date().getTime()}@example.com`,
      phoneNumber: '1234567890',
      password: 'testpass',
      role: 'Customer',
    };

    console.log('Testing direct registration with:', testData);
    console.log('To URL:', apiUrl);

    this.http.post(apiUrl, testData, { observe: 'response' }).subscribe({
      next: (resp) => {
        console.log('Direct registration succeeded:', resp);
      },
      error: (err) => {
        console.error('Direct registration failed:', err);
      },
    });
  }

  // Add to your component:
  testRegistrationFormat() {
    const directApiTest = {
      // Try different permutations to determine exact format:
      userId: null, // Or use a UUID if backend requires it
      name: 'Test User',
      email: `test${Date.now()}@example.com`,
      phoneNumber: '1234567890',
      password: 'password123',
      role: 'Customer', // Try CUSTOMER (all caps)
    };

    console.log('Testing with direct format:', directApiTest);

    this.http
      .post(`${environment.apiUrl}/users/register`, directApiTest, {
        observe: 'response',
      })
      .subscribe({
        next: (resp) => {
          console.log('SUCCESS! Registration works with this format:', resp);
        },
        error: (err) => {
          console.error('ERROR! Format rejected:', err);
        },
      });
  }

  // Helper method to extract specific error messages from the response
  private extractErrorDetails(errorObj: any): string | null {
    if (!errorObj) return null;

    // Check if there's a direct message
    if (errorObj.message) return errorObj.message;

    // Check for field-specific errors
    const errorFields = ['name', 'email', 'phoneNumber', 'password', 'role'];
    const errors: string[] = [];

    for (const field of errorFields) {
      if (errorObj[field]) {
        errors.push(
          `${field.charAt(0).toUpperCase() + field.slice(1)}: ${
            errorObj[field]
          }`
        );
      }
    }

    return errors.length > 0 ? errors.join(', ') : null;
  }

  private addItemToCartAfterRegister(itemId: string): void {
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
              this.snackBar.open(
                `${item.itemName} added to your cart!`,
                'Close',
                { duration: 3000 }
              );
              this.router.navigate(['/menu']);
            },
            error: (err) => {
              console.error(
                'Error adding item to cart after registration:',
                err
              );
              this.router.navigate(['/menu']);
            },
          });
        } else {
          this.router.navigate(['/menu']);
        }
      },
      error: () => {
        this.router.navigate(['/menu']);
      },
    });
  }
}
