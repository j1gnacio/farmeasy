package org.example;

import java.util.Scanner;

import org.bson.Document;
import org.example.model.Medicamento;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class FarmEasyApp {
    private static final String CONNECTION_STRING = "mongodb+srv://cgallegos09:12345@farmeasyscraping.k4cs9kw.mongodb.net/?retryWrites=true&w=majority&appName=FarmEasyScraping";
    private static final String DB_NAME = "farmeasyscraping";
    private static final String COLLECTION_NAME = "medicamentos";

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public FarmEasyApp() {
        try {
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DB_NAME);
            collection = database.getCollection(COLLECTION_NAME);
        } catch (Exception e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            System.exit(1);
        }
    }

    private Medicamento documentToMedicamento(Document doc) {
        if (doc == null) {
            return null;
        }
        String id = doc.containsKey("_id") && doc.getObjectId("_id") != null ? doc.getObjectId("_id").toHexString() : "";
        String nombre = doc.getString("nombre") != null ? doc.getString("nombre") : "";
        String dosis = doc.getString("dosis") != null ? doc.getString("dosis") : "";
        String descripcion = doc.getString("descripcion") != null ? doc.getString("descripcion") : "";
        String precioNormal = doc.getString("precio_farmacia") != null ? doc.getString("precio_farmacia") : "";
        String precioInternet = doc.getString("precio_internet") != null ? doc.getString("precio_internet") : "";
        String url = doc.getString("url_producto") != null ? doc.getString("url_producto") : "";
        String farmacia = doc.getString("farmacia") != null ? doc.getString("farmacia") : "";

        return new Medicamento(id, nombre, dosis, descripcion, precioNormal, precioInternet, url, farmacia);
    }


    public void mostrarMedicamentos() {
        System.out.println("Medicamentos en la base de datos:");
        try {
            FindIterable<Document> docs = collection.find();
            for (Document doc : docs) {
                Medicamento med = documentToMedicamento(doc);
                if (med != null) {
                    System.out.println("----------------------------------");
                    System.out.println(med.toString());
                    System.out.println("-----------------------------\n");
                }
            }
        } catch (Exception e) {
            System.err.println("Error al obtener medicamentos: " + e.getMessage());
        }
    }

    public void buscarPorNombre(String nombre) {
        // Búsqueda insensible a mayúsculas/minúsculas y parcial
        try {
            FindIterable<Document> docs = collection.find(Filters.regex("nombre", ".*" + nombre + ".*", "i"));

            boolean encontrado = false;
            for (Document doc : docs) {
                Medicamento med = documentToMedicamento(doc);
                if (med != null) {
                    System.out.println("--------------------------------");
                    System.out.println(med.toString());
                    System.out.println("--------------------------------");
                    encontrado = true;
                }
            }

            if (!encontrado) {
                System.out.println("No se encontraron medicamentos que coincidan con: " + nombre);
            }
        } catch (Exception e) {
            System.err.println("Error en la búsqueda: " + e.getMessage());
        }
    }


    public void cerrar() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

    public static void main(String[] args) {
        FarmEasyApp app = new FarmEasyApp();
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("\n--- Menú FarmEasy ---");
                System.out.println("1. Mostrar todos los medicamentos");
                System.out.println("2. Buscar medicamento por nombre");
                System.out.println("3. Salir");
                System.out.print("Elige una opción: ");

                String opcion = scanner.nextLine();
                switch (opcion) {
                    case "1":
                        app.mostrarMedicamentos();
                        break;
                    case "2":
                        System.out.print("Ingresa el nombre del medicamento: ");
                        String nombre = scanner.nextLine();
                        app.buscarPorNombre(nombre);
                        break;
                    case "3":
                        app.cerrar();
                        System.out.println("Saliendo...");
                        scanner.close();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opción inválida, intenta otra vez.");
                }
            }
        } catch (Exception e) {
            System.err.println("Error en la aplicación: " + e.getMessage());
        } finally {
            scanner.close();
            app.cerrar();
        }
    }
}
