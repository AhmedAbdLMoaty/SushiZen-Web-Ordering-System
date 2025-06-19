import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

@Injectable({
  providedIn: 'root',
})
export class JsonLdService {
  constructor(@Inject(PLATFORM_ID) private platformId: Object) {}

  injectMenuItemJsonLd(itemId: string): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }

    const jsonLd = {
      '@context': 'https://schema.org',
      '@type': 'MenuItem',
      '@id': `#menu-item-${itemId}`,
      name: 'Menu Item',
      description: 'Delicious menu item description',
    };

    this.addJsonLd(jsonLd, `menu-item-${itemId}`);
  }

  private addJsonLd(data: any, id: string): void {
    const existingScript = document.getElementById(id);
    if (existingScript) {
      existingScript.remove();
    }

    const script = document.createElement('script');
    script.type = 'application/ld+json';
    script.id = id;
    script.text = JSON.stringify(data);
    document.head.appendChild(script);
  }
}
