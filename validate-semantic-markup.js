#!/usr/bin/env node

/**
 * Semantic Markup Validation Script
 * This script validates RDFa and JSON-LD markup in our HTML documents
 */

const fs = require("fs");
const path = require("path");

// Simple RDFa validation function
function validateRDFa(htmlContent) {
    const results = {
        valid: true,
        issues: [],
        rdfaElements: 0,
        propertyCount: 0,
        typeofCount: 0,
    };

    // Check for vocab declaration
    if (!htmlContent.includes("vocab=")) {
        results.issues.push("No vocab declaration found in HTML tag");
        results.valid = false;
    }

    // Count RDFa attributes
    const propertyMatches = htmlContent.match(/property="[^"]+"/g) || [];
    const typeofMatches = htmlContent.match(/typeof="[^"]+"/g) || [];
    const resourceMatches = htmlContent.match(/resource="[^"]+"/g) || [];

    results.propertyCount = propertyMatches.length;
    results.typeofCount = typeofMatches.length;
    results.rdfaElements =
        propertyMatches.length + typeofMatches.length + resourceMatches.length;

    // Check for common Schema.org types
    const schemaTypes = [
        "Restaurant",
        "Menu",
        "MenuItem",
        "PostalAddress",
        "GeoCoordinates",
        "Review",
        "AggregateRating",
    ];
    schemaTypes.forEach((type) => {
        if (htmlContent.includes(`typeof="${type}"`)) {
            console.log(`✓ Found Schema.org type: ${type}`);
        }
    });

    // Check for custom namespace usage
    if (htmlContent.includes("sushi:")) {
        console.log("✓ Found custom sushi vocabulary usage");
    }

    return results;
}

// JSON-LD validation function
function validateJsonLD(htmlContent) {
    const results = {
        valid: true,
        issues: [],
        scriptsFound: 0,
        contexts: [],
    };

    // Extract JSON-LD scripts
    const jsonLdRegex =
        /<script type="application\/ld\+json">([\s\S]*?)<\/script>/g;
    let match;

    while ((match = jsonLdRegex.exec(htmlContent)) !== null) {
        results.scriptsFound++;
        try {
            const jsonData = JSON.parse(match[1].trim());

            // Check for @context
            if (jsonData["@context"]) {
                results.contexts.push(jsonData["@context"]);
                console.log(
                    `✓ Found JSON-LD context: ${JSON.stringify(
                        jsonData["@context"]
                    )}`
                );
            }

            // Check for @type
            if (jsonData["@type"]) {
                console.log(`✓ Found JSON-LD type: ${jsonData["@type"]}`);
            }
        } catch (e) {
            results.valid = false;
            results.issues.push(`Invalid JSON-LD syntax: ${e.message}`);
        }
    }

    return results;
}

// Main validation function
function validateSemanticMarkup() {
    const htmlFile = path.join(
        __dirname,
        "sushi-zen-frontend",
        "src",
        "assets",
        "semantic-demo.html"
    );

    if (!fs.existsSync(htmlFile)) {
        console.error("❌ HTML file not found:", htmlFile);
        return;
    }

    const htmlContent = fs.readFileSync(htmlFile, "utf8");

    console.log("🔍 Validating Semantic Markup...\n");

    // Validate RDFa
    console.log("📝 RDFa Validation:");
    const rdfaResults = validateRDFa(htmlContent);
    console.log(`   Properties found: ${rdfaResults.propertyCount}`);
    console.log(`   Types found: ${rdfaResults.typeofCount}`);
    console.log(`   Total RDFa elements: ${rdfaResults.rdfaElements}`);

    if (rdfaResults.issues.length > 0) {
        console.log("   Issues:");
        rdfaResults.issues.forEach((issue) => console.log(`   ❌ ${issue}`));
    } else {
        console.log("   ✅ RDFa markup appears valid");
    }

    console.log("\n📊 JSON-LD Validation:");
    const jsonLdResults = validateJsonLD(htmlContent);
    console.log(`   JSON-LD scripts found: ${jsonLdResults.scriptsFound}`);

    if (jsonLdResults.issues.length > 0) {
        console.log("   Issues:");
        jsonLdResults.issues.forEach((issue) => console.log(`   ❌ ${issue}`));
    } else {
        console.log("   ✅ JSON-LD markup appears valid");
    }

    // Summary
    console.log("\n📋 Summary:");
    console.log(
        `   Overall Status: ${
            rdfaResults.valid && jsonLdResults.valid
                ? "✅ VALID"
                : "❌ ISSUES FOUND"
        }`
    );
    console.log(`   RDFa Elements: ${rdfaResults.rdfaElements}`);
    console.log(`   JSON-LD Scripts: ${jsonLdResults.scriptsFound}`);

    console.log("\n🔗 Next Steps:");
    console.log("   1. Test with RDFa Play: http://rdfa.info/play/");
    console.log(
        "   2. Test with Google Rich Results: https://search.google.com/test/rich-results"
    );
    console.log("   3. Validate custom vocabulary with W3C RDF Validator");
}

validateSemanticMarkup();
