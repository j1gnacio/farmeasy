package org.example;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.example.model.Medicamento;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class FarmEasyAppTest {

    private FarmEasyApp app;
    private MongoClient mockClient;
    private MongoDatabase mockDatabase;
    @SuppressWarnings("unchecked")
    private MongoCollection<Document> mockCollection;

    private MockedStatic<com.mongodb.client.MongoClients> mockedStatic;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        mockClient = mock(MongoClient.class);
        mockDatabase = mock(MongoDatabase.class);
        mockCollection = mock(MongoCollection.class);

        // Mock static MongoClients.create to return mockClient
        mockedStatic = Mockito.mockStatic(com.mongodb.client.MongoClients.class);
        mockedStatic.when(() -> com.mongodb.client.MongoClients.create(anyString())).thenReturn(mockClient);

        when(mockClient.getDatabase(anyString())).thenReturn(mockDatabase);
        when(mockDatabase.getCollection(anyString())).thenReturn(mockCollection);

        app = new FarmEasyApp();
    }

    @AfterEach
    public void tearDown() {
        if (mockedStatic != null) {
            mockedStatic.close();
        }
    }

    @Test
    public void testMostrarMedicamentosConDatos() {
        List<Document> docs = new ArrayList<>();
        docs.add(new Document("nombre", "Med1").append("dosis", "10mg"));
        docs.add(new Document("nombre", "Med2").append("dosis", "20mg"));

        FindIterable<Document> iterable = mock(FindIterable.class);
        MongoCursor<Document> cursor = mock(MongoCursor.class);
        when(mockCollection.find()).thenReturn(iterable);
        when(iterable.iterator()).thenReturn(cursor);
        when(cursor.hasNext()).thenReturn(true, true, false);
        when(cursor.next()).thenReturn(docs.get(0), docs.get(1));

        // No exception expected
        assertDoesNotThrow(() -> app.mostrarMedicamentos());
    }

    @Test
    public void testMostrarMedicamentosSinDatos() {
        List<Document> docs = new ArrayList<>();

        FindIterable<Document> iterable = mock(FindIterable.class);
        MongoCursor<Document> cursor = mock(MongoCursor.class);
        when(mockCollection.find()).thenReturn(iterable);
        when(iterable.iterator()).thenReturn(cursor);
        when(cursor.hasNext()).thenReturn(false);

        assertDoesNotThrow(() -> app.mostrarMedicamentos());
    }

    @Test
    public void testBuscarPorNombreConResultados() {
        List<Document> docs = new ArrayList<>();
        docs.add(new Document("nombre", "Med1").append("dosis", "10mg"));

        FindIterable<Document> iterable = mock(FindIterable.class);
        MongoCursor<Document> cursor = mock(MongoCursor.class);
        when(mockCollection.find(any(org.bson.conversions.Bson.class))).thenReturn(iterable);
        when(iterable.iterator()).thenReturn(cursor);
        when(cursor.hasNext()).thenReturn(true, false);
        when(cursor.next()).thenReturn(docs.get(0));

        assertDoesNotThrow(() -> app.buscarPorNombre("Med1"));
    }

    @Test
    public void testBuscarPorNombreSinResultados() {
        List<Document> docs = new ArrayList<>();

        FindIterable<Document> iterable = mock(FindIterable.class);
        MongoCursor<Document> cursor = mock(MongoCursor.class);
        when(mockCollection.find(any(org.bson.conversions.Bson.class))).thenReturn(iterable);
        when(iterable.iterator()).thenReturn(cursor);
        when(cursor.hasNext()).thenReturn(false);

        assertDoesNotThrow(() -> app.buscarPorNombre("NoExiste"));
    }

    @Test
    public void testCerrar() {
        assertDoesNotThrow(() -> app.cerrar());
    }

    @Test
    public void testDocumentToMedicamentoNull() {
        assertNull(app.documentToMedicamento(null));
    }

    /*@Test
    public void testDocumentToMedicamentoCompleto() {
        Document doc = new Document("_id", new org.bson.types.ObjectId())
                .append("nombre", "Med1")
                .append("dosis", "10mg")
                .append("descripcion", "desc")
                .append("precio_farmacia", "90")
                .append("precio_internet", "100")
                .append("url_producto", "url")
                .append("farmacia", "farm");

        Medicamento med = app.documentToMedicamento(doc);
        assertNotNull(med);
        assertEquals("Med1", med.getNombre());
        assertEquals("10mg", med.getDosis());
        assertEquals("desc", med.getDescripcion());
        assertEquals("100", med.getPrecioInternet());
        assertEquals("90", med.getPrecioNormal());
        assertEquals("url", med.getUrl());
        assertEquals("farm", med.getFarmacia());
    }*/
}
