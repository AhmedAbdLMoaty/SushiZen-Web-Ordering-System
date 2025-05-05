import { Component, OnInit, Inject, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, MatButtonModule, MatIconModule, RouterModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  specialties = [
    {
      name: 'Signature Rolls',
      description:
        "Our chef's special creation with premium ingredients and artistic presentation.",
      image:
        'https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?q=80&w=774&auto=format&fit=crop',
    },
    {
      name: 'Premium Sashimi',
      description:
        'Fresh-cut fish slices, selected daily for optimal flavor and texture.',
      image:
        'https://images.unsplash.com/photo-1534482421-64566f976cfa?q=80&w=869&auto=format&fit=crop',
    },
    {
      name: 'Zen Platters',
      description:
        'Curated collections of our finest sushi for a complete culinary journey.',
      image:
        'https://images.unsplash.com/photo-1617196034183-421b4917c92d?q=80&w=870&auto=format&fit=crop',
    },
  ];

  features = [
    {
      icon: 'restaurant',
      title: 'Premium Ingredients',
      description:
        'We source the freshest ingredients daily from trusted suppliers.',
    },
    {
      icon: 'local_shipping',
      title: 'Express Delivery',
      description: 'Hot and fresh delivery ensuring optimal quality and taste.',
    },
    {
      icon: 'payments',
      title: 'Secure Payments',
      description: 'Multiple payment options with advanced security features.',
    },
    {
      icon: 'loyalty',
      title: 'Rewards Program',
      description:
        'Earn points with every order and unlock exclusive benefits.',
    },
  ];

  testimonials = [
    {
      quote:
        "The best sushi I've had outside of Japan. Their attention to detail is remarkable!",
      name: 'Sophia Chen',
      location: 'Boston',
      avatar: 'assets/images/testimonial-1.jpg',
    },
    {
      quote:
        'The Zen Platters are perfect for sharing. My family and I order them regularly.',
      name: 'Michael Johnson',
      location: 'Chicago',
      avatar: 'assets/images/testimonial-2.jpg',
    },
    {
      quote:
        'Their delivery is always on time and the food quality is consistently excellent.',
      name: 'Priya Sharma',
      location: 'Seattle',
      avatar: 'assets/images/testimonial-3.jpg',
    },
  ];

  constructor(
    private router: Router,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    // Preload critical images only in browser
    this.preloadImages();
  }

  // Fixed image preloading with browser check
  preloadImages(): void {
    // Only run in browser environment, not on server
    if (isPlatformBrowser(this.platformId)) {
      const images = [
        'assets/images/hero-sushi.jpg',
        'assets/images/signature-rolls.jpg',
        'assets/images/sashimi.jpg',
        'assets/images/zen-platter.jpg',
        'assets/images/chef.jpg',
        'assets/images/pattern-bg.jpg',
        'assets/images/testimonial-1.jpg',
        'assets/images/testimonial-2.jpg',
        'assets/images/testimonial-3.jpg',
      ];

      images.forEach((src) => {
        const img = new Image();
        img.src = src;
      });
    }
  }

  goToMenu(): void {
    this.router.navigate(['/menu']);
  }
}
