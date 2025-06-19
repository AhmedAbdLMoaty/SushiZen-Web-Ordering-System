import {
  Component,
  OnInit,
  OnDestroy,
  Inject,
  PLATFORM_ID,
} from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { NavbarComponent } from './shared/navbar/navbar.component';
import { StructuredDataService } from './services/structured-data.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, NavbarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit, OnDestroy {
  title = 'sushi-zen-frontend';

  constructor(
    private structuredDataService: StructuredDataService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.structuredDataService.addJsonLdFromAPI(
        this.structuredDataService.getRestaurantDataFromAPI(),
        'restaurant-jsonld'
      );

      this.structuredDataService.addJsonLdFromAPI(
        this.structuredDataService.getWebsiteDataFromAPI(),
        'website-jsonld'
      );
    }
  }

  ngOnDestroy(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.structuredDataService.removeJsonLd('restaurant-jsonld');
      this.structuredDataService.removeJsonLd('website-jsonld');
    }
  }
}
