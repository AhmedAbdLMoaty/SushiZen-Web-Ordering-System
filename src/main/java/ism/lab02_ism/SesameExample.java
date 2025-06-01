package ism.lab02_ism;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.query.*;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

public class SesameExample {

    private Repository repository;

    public SesameExample() {
        // Create a new Repository using an in-memory store
        repository = new SailRepository(new MemoryStore());
        repository.init();
    }

    public void loadRdfData(String ontologyFilePath, String instancesFilePath)
            throws IOException, RDFParseException, UnsupportedRDFormatException {
        try (RepositoryConnection conn = repository.getConnection()) {
            // Load ontology
            File ontologyFile = new File(ontologyFilePath);
            try (FileInputStream input = new FileInputStream(ontologyFile)) {
                conn.add(input, "", RDFFormat.TURTLE);
                System.out.println("Ontology loaded successfully.");
            }

            // Load instances
            File instancesFile = new File(instancesFilePath);
            try (FileInputStream input = new FileInputStream(instancesFile)) {
                conn.add(input, "", RDFFormat.TURTLE);
                System.out.println("Instances loaded successfully.");
            }
        }
    }

    public void executeQuery(String sparqlQuery, String queryName) {
        try (RepositoryConnection conn = repository.getConnection()) {
            TupleQuery query = conn.prepareTupleQuery(QueryLanguage.SPARQL, sparqlQuery);

            System.out.println("\n----- Executing " + queryName + " -----");
            System.out.println(sparqlQuery);
            System.out.println("----- Results -----");

            try (TupleQueryResult result = query.evaluate()) {
                // Process the results
                int count = 0;
                while (result.hasNext()) {
                    BindingSet bindingSet = result.next();
                    count++;

                    System.out.print("Result " + count + ": ");
                    for (Binding binding : bindingSet) {
                        System.out.print(binding.getName() + " = " + binding.getValue() + " | ");
                    }
                    System.out.println();
                }
                System.out.println("Total results: " + count);
            }
        }
    }

    public void convertToJsonLD(String turtleFilePath, String outputFilePath) {
        try {
            // Parse the Turtle file
            File file = new File(turtleFilePath);
            Model model = Rio.parse(new FileInputStream(file), "", RDFFormat.TURTLE);

            // Write to JSON-LD
            FileWriter writer = new FileWriter(outputFilePath);
            Rio.write(model, writer, RDFFormat.JSONLD);
            writer.close();
            System.out.println("Converted " + turtleFilePath + " to JSON-LD format at " + outputFilePath);
        } catch (Exception e) {
            System.err.println("Error converting to JSON-LD: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void convertToRDFXML(String turtleFilePath, String outputFilePath) {
        try {
            // Parse the Turtle file
            File file = new File(turtleFilePath);
            Model model = Rio.parse(new FileInputStream(file), "", RDFFormat.TURTLE);

            // Write to RDF/XML
            FileWriter writer = new FileWriter(outputFilePath);
            Rio.write(model, writer, RDFFormat.RDFXML);
            writer.close();
            System.out.println("Converted " + turtleFilePath + " to RDF/XML format at " + outputFilePath);
        } catch (Exception e) {
            System.err.println("Error converting to RDF/XML: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void close() {
        repository.shutDown();
        System.out.println("Repository shut down.");
    }

    public static void main(String[] args) {
        SesameExample example = new SesameExample();

        try {
            // Define file paths
            String ontologyPath = "src/main/resources/ontology.ttl";
            String instancesPath = "src/main/resources/instances.ttl";

            // Load RDF data
            example.loadRdfData(ontologyPath, instancesPath);

            // Convert files to other formats
            example.convertToJsonLD(ontologyPath, "src/main/resources/ontology.jsonld");
            example.convertToJsonLD(instancesPath, "src/main/resources/instances.jsonld");
            example.convertToRDFXML(ontologyPath, "src/main/resources/ontology.rdf");
            example.convertToRDFXML(instancesPath, "src/main/resources/instances.rdf");

            // Execute SPARQL queries

            // Query 1: Find all food items with their prices, ordered by price (descending)
            String query1 = "PREFIX : <http://www.semanticweb.org/sushi-ontology#>\n" +
                    "SELECT ?foodName ?price WHERE {\n" +
                    "  ?food rdf:type/rdfs:subClassOf* :FoodItem .\n" +
                    "  ?food :hasName ?foodName .\n" +
                    "  ?food :hasPrice ?price .\n" +
                    "} ORDER BY DESC(xsd:decimal(?price))";
            example.executeQuery(query1, "Query 1: Food items by price (descending)");

            // Query 2: Find vegetarian food items with calorie information
            String query2 = "PREFIX : <http://www.semanticweb.org/sushi-ontology#>\n" +
                    "SELECT ?foodName ?calories WHERE {\n" +
                    "  ?food rdf:type/rdfs:subClassOf* :FoodItem .\n" +
                    "  ?food :hasName ?foodName .\n" +
                    "  ?food :isVegetarian \"true\"^^xsd:boolean .\n" +
                    "  ?food :hasCalories ?calories .\n" +
                    "} ORDER BY ?calories";
            example.executeQuery(query2, "Query 2: Vegetarian food items with calories");

            // Query 3: Find food items with specific ingredients (using FILTER and UNION)
            String query3 = "PREFIX : <http://www.semanticweb.org/sushi-ontology#>\n" +
                    "SELECT ?foodName ?ingredientName WHERE {\n" +
                    "  ?food rdf:type/rdfs:subClassOf* :FoodItem .\n" +
                    "  ?food :hasName ?foodName .\n" +
                    "  ?food :hasIngredient ?ingredient .\n" +
                    "  ?ingredient :hasName ?ingredientName .\n" +
                    "  FILTER (?ingredient = :SalmonFish || ?ingredient = :Avocado)\n" +
                    "} ORDER BY ?foodName";
            example.executeQuery(query3, "Query 3: Food items with salmon or avocado");

            // Query 4: Find orders with customer details and what items they ordered
            String query4 = "PREFIX : <http://www.semanticweb.org/sushi-ontology#>\n" +
                    "SELECT ?orderNum ?customerName ?foodName ?orderDate ?status WHERE {\n" +
                    "  ?order rdf:type :Order .\n" +
                    "  ?order :hasOrderNumber ?orderNum .\n" +
                    "  ?order :orderedBy ?customer .\n" +
                    "  ?customer :hasName ?customerName .\n" +
                    "  ?order :includes ?food .\n" +
                    "  ?food :hasName ?foodName .\n" +
                    "  ?order :hasOrderDate ?orderDate .\n" +
                    "  ?order :hasOrderStatus ?status .\n" +
                    "} ORDER BY ?orderDate";
            example.executeQuery(query4, "Query 4: Orders with customer details and items");

            // Query 5: Complex query with aggregation - Count food items by category
            String query5 = "PREFIX : <http://www.semanticweb.org/sushi-ontology#>\n" +
                    "SELECT ?foodType (COUNT(?food) as ?count) WHERE {\n" +
                    "  ?food rdf:type ?foodType .\n" +
                    "  ?foodType rdfs:subClassOf* :FoodItem .\n" +
                    "  FILTER (?foodType != :FoodItem)\n" +
                    "} GROUP BY ?foodType";
            example.executeQuery(query5, "Query 5: Count of food items by category");

            // Query 6: Most expensive items in each restaurant menu
            String query6 = "PREFIX : <http://www.semanticweb.org/sushi-ontology#>\n" +
                    "SELECT ?restaurantName ?foodName ?price WHERE {\n" +
                    "  ?restaurant rdf:type :Restaurant .\n" +
                    "  ?restaurant :hasName ?restaurantName .\n" +
                    "  ?food :servedAt ?restaurant .\n" +
                    "  ?food :hasName ?foodName .\n" +
                    "  ?food :hasPrice ?price .\n" +
                    "} ORDER BY ?restaurantName DESC(xsd:decimal(?price))\n" +
                    "LIMIT 10";
            example.executeQuery(query6, "Query 6: Most expensive items in each restaurant"); // Query 7: Compare
                                                                                              // restaurant details
                                                                                              // (dining options,
                                                                                              // payment methods, etc.)
            String query7 = "PREFIX : <http://www.semanticweb.org/sushi-ontology#>\n" +
                    "SELECT ?restaurantName ?address ?diningOption ?paymentMethod ?employeeName ?employeeType WHERE {\n"
                    +
                    "  ?restaurant rdf:type :Restaurant .\n" +
                    "  ?restaurant :hasName ?restaurantName .\n" +
                    "  ?restaurant :hasAddress ?address .\n" +
                    "  ?restaurant :hasDiningOption ?dining .\n" +
                    "  ?dining :hasName ?diningOption .\n" +
                    "  ?restaurant :offersPaymentMethod ?payment .\n" +
                    "  ?payment :hasName ?paymentMethod .\n" +
                    "  ?restaurant :employs ?employee .\n" +
                    "  ?employee :hasName ?employeeName .\n" +
                    "  ?employee rdf:type ?type .\n" +
                    "  ?type rdfs:subClassOf :Person .\n" +
                    "  BIND(STRAFTER(STR(?type), 'http://www.semanticweb.org/sushi-ontology#') AS ?employeeType)\n" +
                    "} ORDER BY ?restaurantName ?employeeType";
            example.executeQuery(query7, "Query 7: Restaurant details comparison");

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            example.close();
        }
    }
}
