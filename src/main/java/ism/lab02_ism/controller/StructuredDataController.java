package ism.lab02_ism.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/structured-data")
@CrossOrigin(origins = "*")
public class StructuredDataController {

    @GetMapping(value = "/restaurant", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRestaurantStructuredData() {
        String jsonLd = """
                {
                  "@context": "https://schema.org",
                  "@type": "Restaurant",
                  "name": "Sushi Zen",
                  "alternateName": "寿司禅",
                  "description": "Experience the art of authentic Japanese cuisine with our premium sushi, sashimi, and traditional Japanese dishes.",
                  "url": "https://sushizen.com",
                  "logo": "https://sushizen.com/assets/images/logo.png",
                  "image": "https://sushizen.com/assets/images/hero-sushi.jpg",
                  "servesCuisine": ["Japanese", "Sushi", "Asian"],
                  "priceRange": "$$",
                  "currenciesAccepted": "USD",
                  "paymentAccepted": ["Cash", "Credit Card", "Mobile Payment"],
                  "address": {
                    "@type": "PostalAddress",
                    "streetAddress": "123 Main Street",
                    "addressLocality": "New York",
                    "addressRegion": "NY",
                    "postalCode": "10001",
                    "addressCountry": "US"
                  },
                  "geo": {
                    "@type": "GeoCoordinates",
                    "latitude": "40.7589",
                    "longitude": "-73.9851"
                  },
                  "telephone": "+1-212-555-1234",
                  "openingHours": [
                    "Mo-Th 11:00-22:00",
                    "Fr-Sa 11:00-23:00",
                    "Su 12:00-21:00"
                  ],
                  "hasMenu": {
                    "@type": "Menu",
                    "name": "Sushi Zen Menu",
                    "description": "A collection of traditional and fusion sushi dishes",
                    "url": "https://sushizen.com/menu"
                  },
                  "aggregateRating": {
                    "@type": "AggregateRating",
                    "ratingValue": "4.8",
                    "reviewCount": "324"
                  }
                }
                """;

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonLd);
    }

    @GetMapping(value = "/menu", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMenuStructuredData() {
        String jsonLd = """
                {
                  "@context": ["https://schema.org", "https://sushizen.com/vocab/sushi"],
                  "@type": "Menu",
                  "name": "Sushi Zen Menu",
                  "description": "Discover the artistry of Japanese cuisine",
                  "url": "https://sushizen.com/menu",
                  "hasMenuSection": [
                    {
                      "@type": "MenuSection",
                      "name": "Signature Rolls",
                      "description": "Our chef's special creation with premium ingredients",
                      "hasMenuItem": [
                        {
                          "@type": ["MenuItem", "SushiMenuItem", "Uramaki"],
                          "name": "Dragon Roll",
                          "description": "Eel, avocado, and cucumber topped with sliced avocado",
                          "image": "https://sushizen.com/assets/images/dragon-roll.jpg",
                          "offers": {
                            "@type": "Offer",
                            "price": "18.95",
                            "priceCurrency": "USD",
                            "availability": "InStock"
                          },
                          "hasJapaneseName": "ドラゴンロール",
                          "isRaw": false,
                          "hasSpiceLevel": "mild"
                        },
                        {
                          "@type": ["MenuItem", "SushiMenuItem", "Uramaki"],
                          "name": "Rainbow Roll",
                          "description": "California roll topped with assorted sashimi",
                          "image": "https://sushizen.com/assets/images/rainbow-roll.jpg",
                          "offers": {
                            "@type": "Offer",
                            "price": "16.95",
                            "priceCurrency": "USD",
                            "availability": "InStock"
                          },
                          "hasJapaneseName": "レインボーロール",
                          "isRaw": true,
                          "hasSpiceLevel": "mild"
                        }
                      ]
                    },
                    {
                      "@type": "MenuSection",
                      "name": "Premium Sashimi",
                      "description": "Fresh-cut fish slices, selected daily for optimal flavor",
                      "hasMenuItem": [
                        {
                          "@type": ["MenuItem", "SushiMenuItem", "Sashimi"],
                          "name": "Salmon Sashimi",
                          "description": "Fresh Atlantic salmon, expertly sliced",
                          "image": "https://sushizen.com/assets/images/salmon-sashimi.jpg",
                          "offers": {
                            "@type": "Offer",
                            "price": "14.95",
                            "priceCurrency": "USD",
                            "availability": "InStock"
                          },
                          "hasJapaneseName": "サーモン刺身",
                          "isRaw": true,
                          "hasOrigin": "Norwegian Atlantic",
                          "hasFreshness": "daily"
                        }
                      ]
                    }
                  ]
                }
                """;

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonLd);
    }

    @GetMapping(value = "/menu-item/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMenuItemStructuredData(@PathVariable String itemId) {
        // This would typically fetch from your database
        String jsonLd = String.format("""
                {
                  "@context": ["https://schema.org", "https://sushizen.com/vocab/sushi"],
                  "@type": ["MenuItem", "SushiMenuItem"],
                  "@id": "https://sushizen.com/menu/item/%s",
                  "name": "Sample Sushi Item",
                  "description": "A delicious sushi item from our menu",
                  "image": "https://sushizen.com/assets/images/sushi-item.jpg",
                  "offers": {
                    "@type": "Offer",
                    "price": "12.95",
                    "priceCurrency": "USD",
                    "availability": "InStock"
                  },
                  "nutrition": {
                    "@type": "NutritionInformation",
                    "calories": "250"
                  },
                  "isVegetarian": false,
                  "hasJapaneseName": "寿司アイテム",
                  "isRaw": true,
                  "servesWithWasabi": true,
                  "servesWithSoyaSauce": true,
                  "servesWithGinger": true
                }
                """, itemId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonLd);
    }
}
