#!/usr/bin/env node

/**
 * Simple HTTP Server for Semantic Demo
 * Serves the semantic-demo.html file for testing with online validators
 */

const http = require("http");
const fs = require("fs");
const path = require("path");

const PORT = 3000;
const HOST = "localhost";

const server = http.createServer((req, res) => {
    // Set CORS headers for testing
    res.setHeader("Access-Control-Allow-Origin", "*");
    res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
    res.setHeader("Access-Control-Allow-Headers", "Content-Type");

    if (req.url === "/" || req.url === "/semantic-demo.html") {
        const htmlFile = path.join(
            __dirname,
            "sushi-zen-frontend",
            "src",
            "assets",
            "semantic-demo.html"
        );

        if (fs.existsSync(htmlFile)) {
            const htmlContent = fs.readFileSync(htmlFile, "utf8");
            res.writeHead(200, { "Content-Type": "text/html" });
            res.end(htmlContent);
        } else {
            res.writeHead(404, { "Content-Type": "text/plain" });
            res.end("Semantic demo HTML file not found");
        }
    } else if (req.url === "/vocab/sushi") {
        const vocabFile = path.join(
            __dirname,
            "sushi-zen-frontend",
            "src",
            "assets",
            "vocabularies",
            "sushi-vocab.ttl"
        );

        if (fs.existsSync(vocabFile)) {
            const vocabContent = fs.readFileSync(vocabFile, "utf8");
            res.writeHead(200, { "Content-Type": "text/turtle" });
            res.end(vocabContent);
        } else {
            res.writeHead(404, { "Content-Type": "text/plain" });
            res.end("Vocabulary file not found");
        }
    } else if (req.url === "/api/structured-data/restaurant") {
        // Serve the structured data for testing
        const jsonLd = {
            "@context": [
                "https://schema.org",
                "https://sushizen.com/vocab/sushi",
            ],
            "@type": "Restaurant",
            name: "Sushi Zen",
            alternateName: "寿司禅",
            description:
                "Experience the art of authentic Japanese cuisine with our premium sushi, sashimi, and traditional Japanese dishes.",
            url: "https://sushizen.com",
            logo: "https://sushizen.com/assets/images/logo.png",
            image: "https://sushizen.com/assets/images/hero-sushi.jpg",
            servesCuisine: ["Japanese", "Sushi", "Asian"],
            priceRange: "$$",
            currenciesAccepted: "USD",
            paymentAccepted: ["Cash", "Credit Card", "Mobile Payment"],
            address: {
                "@type": "PostalAddress",
                streetAddress: "123 Main Street",
                addressLocality: "New York",
                addressRegion: "NY",
                postalCode: "10001",
                addressCountry: "US",
            },
            geo: {
                "@type": "GeoCoordinates",
                latitude: "40.7589",
                longitude: "-73.9851",
            },
            telephone: "+1-212-555-1234",
            email: "info@sushizen.com",
            openingHours: [
                "Mo-Th 11:00-22:00",
                "Fr-Sa 11:00-23:00",
                "Su 12:00-21:00",
            ],
            aggregateRating: {
                "@type": "AggregateRating",
                ratingValue: "4.8",
                reviewCount: "324",
                bestRating: "5",
                worstRating: "1",
            },
        };

        res.writeHead(200, { "Content-Type": "application/json" });
        res.end(JSON.stringify(jsonLd, null, 2));
    } else {
        res.writeHead(404, { "Content-Type": "text/plain" });
        res.end("Not Found");
    }
});

server.listen(PORT, HOST, () => {
    console.log(`🌐 Semantic Demo Server running at http://${HOST}:${PORT}`);
    console.log("📋 Available endpoints:");
    console.log(`   📄 Main demo: http://${HOST}:${PORT}/`);
    console.log(
        `   📊 Restaurant JSON-LD: http://${HOST}:${PORT}/api/structured-data/restaurant`
    );
    console.log(`   📚 Custom vocabulary: http://${HOST}:${PORT}/vocab/sushi`);
    console.log("\n🔍 Test with validators:");
    console.log(
        `   🟦 RDFa Play: http://rdfa.info/play/ (paste URL: http://${HOST}:${PORT}/)`
    );
    console.log(
        `   🟨 Google Rich Results: https://search.google.com/test/rich-results (paste URL: http://${HOST}:${PORT}/)`
    );
    console.log(
        `   🟩 W3C Markup Validator: https://validator.w3.org/ (paste URL: http://${HOST}:${PORT}/)`
    );
    console.log("\n⏹️  Press Ctrl+C to stop the server");
});

// Handle graceful shutdown
process.on("SIGINT", () => {
    console.log("\n🛑 Shutting down server...");
    server.close(() => {
        console.log("✅ Server stopped");
        process.exit(0);
    });
});
