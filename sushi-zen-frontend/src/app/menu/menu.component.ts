import {
  Component,
  OnInit,
  OnDestroy,
  Inject,
  PLATFORM_ID,
} from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MenuService } from '../services/menu.service';
import { CartService } from '../services/cart.service';
import { AuthService } from '../services/auth.service';
import { StructuredDataService } from '../services/structured-data.service';

interface MenuItem {
  itemId: number;
  itemName: string;
  itemDescription: string;
  itemPrice: number;
  itemPicture?: string;
  category: string;
  available: boolean;
}

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
  ],
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss'],
})
export class MenuComponent implements OnInit, OnDestroy {
  menuItems: MenuItem[] = [];
  filteredItems: MenuItem[] = [];
  searchQuery = '';
  activeCategory = 'all';
  loading = true;
  error = false;
  constructor(
    private menuService: MenuService,
    private cartService: CartService,
    private authService: AuthService,
    private router: Router,
    private structuredDataService: StructuredDataService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    this.loadMenuItems();

    if (isPlatformBrowser(this.platformId)) {
      this.structuredDataService.addJsonLdFromAPI(
        this.structuredDataService.getMenuDataFromAPI(),
        'menu-jsonld'
      );
    }
  }

  ngOnDestroy(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.structuredDataService.removeJsonLd('menu-jsonld');
    }
  }
  loadMenuItems(): void {
    this.loading = true;

    this.menuService.getAllMenuItems().subscribe({
      next: (items) => {
        this.menuItems = items;
        this.filteredItems = [...items];
        this.loading = false;
      },
      error: (err) => {
        console.error('Error loading menu items:', err);
        this.error = true;
        this.loading = false;
      },
    });
  }

  filterItems(): void {
    if (!this.searchQuery.trim()) {
      this.filterByCategory(this.activeCategory);
      return;
    }

    const query = this.searchQuery.toLowerCase();
    let filtered = this.menuItems.filter(
      (item) =>
        item.itemName.toLowerCase().includes(query) ||
        item.itemDescription.toLowerCase().includes(query)
    );

    if (this.activeCategory !== 'all') {
      filtered = filtered.filter(
        (item) => item.category === this.activeCategory
      );
    }

    this.filteredItems = filtered;
  }

  filterByCategory(category: string): void {
    this.activeCategory = category;

    if (category === 'all') {
      this.filteredItems = [...this.menuItems];
    } else {
      this.filteredItems = this.menuItems.filter(
        (item) => item.category === category
      );
    }

    // Also apply search filter if present
    if (this.searchQuery.trim()) {
      const query = this.searchQuery.toLowerCase();
      this.filteredItems = this.filteredItems.filter(
        (item) =>
          item.itemName.toLowerCase().includes(query) ||
          item.itemDescription.toLowerCase().includes(query)
      );
    }
  }

  resetFilters(): void {
    this.searchQuery = '';
    this.activeCategory = 'all';
    this.filteredItems = [...this.menuItems];
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
