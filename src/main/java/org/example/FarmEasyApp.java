package org.example;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.util.List;
import java.util.Scanner;

public class FarmEasyApp {
    private static final String CONNECTION_STRING = "mongodb+srv://cgallegos09:12345@farmeasyscraping.k4cs9kw.mongodb.net/?retryWrites=true&w=majority&appName=FarmEasyScraping";
    private static final String DB_NAME = "farmeasyscraping";
    private static final String COLLECTION_NAME = "medicamentos";

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    public FarmEasyApp() {
        mongoClient = MongoClients.create(CONNECTION_STRING);
        database = mongoClient.getDatabase(DB_NAME);
        collection = database.getCollection(COLLECTION_NAME);
    }

    private Medicamento documentToMedicamento(Document doc) {
        String id = doc.getObjectId("_id").toHexString();
        String nombre = doc.getString("nombre");
        String dosis = doc.getString("dosis");
        String descripcion = doc.getString("descripcion");
        String precioNormal = doc.getString("precio_farmacia");
        String precioInternet = doc.getString("precio_internet");
        String url = doc.getString("url_producto");
        String farmacia = doc.getString("farmacia");

        return new Medicamento(id, nombre, dosis, descripcion, precioNormal, precioInternet, url, farmacia);
    }


    public void mostrarMedicamentos() {
        System.out.println("Medicamentos en la base de datos:");
        FindIterable<Document> docs = collection.find();
        for (Document doc : docs) {
            Medicamento med = documentToMedicamento(doc);
            System.out.println("----------------------------------");
            med.obtenerDetalles();
            System.out.println("-----------------------------\n");
        }
    }

    public void buscarPorNombre(String nombre) {
        // Búsqueda insensible a mayúsculas/minúsculas y parcial
        FindIterable<Document> docs = collection.find(Filters.regex("nombre", ".*" + nombre + ".*", "i"));

        boolean encontrado = false;
        for (Document doc : docs) {
            Medicamento med = documentToMedicamento(doc);
            System.out.println("--------------------------------");
            med.obtenerDetalles();
            System.out.println("--------------------------------");
            encontrado = true;
        }

        if (!encontrado) {
            System.out.println("No se encontraron medicamentos que coincidan con: " + nombre);
        }
    }


    public void cerrar() {
        mongoClient.close();
    }

    public static void main(String[] args) {
        FarmEasyApp app = new FarmEasyApp();
        Scanner scanner = new Scanner(System.in);
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
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción inválida, intenta otra vez.");
            }
        }
    }
}
