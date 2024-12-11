package ru.mephi.dozen.oop.lab3.service;

import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import ru.mephi.dozen.oop.lab3.AbstractTest;
import ru.mephi.dozen.oop.lab3.TestDigitalSignatureService;
import ru.mephi.dozen.oop.lab3.model.IDocumentTemplate;
import ru.mephi.dozen.oop.lab3.model.SimpleDocumentTemplate;
import ru.mephi.dozen.oop.lab3.service.impl.BlockingDocumentService;
import ru.mephi.dozen.oop.lab3.service.impl.ParallelDocumentService;

public abstract class AbstractDocumentServiceTest extends AbstractTest {

    protected abstract DocumentService getDocumentService();

    private DocumentService service;

    @BeforeEach
    void init() {
        service = getDocumentService();
    }

    @Test
    @DisplayName("Вставка новых шаблонов без циклов не приводит к ошибке")
    void saveTemplate() {
        var template1 = new SimpleDocumentTemplate("#{template2}");
        var template2 = new SimpleDocumentTemplate("#{template3}");
        service.saveTemplate("template1", template1);
        service.saveTemplate("template2", template2);

        assertTrue(service.getTemplate("template1").isPresent());
        assertTrue(service.getTemplate("template2").isPresent());
        assertEquals(template1, service.getTemplate("template1").get());
        assertEquals(template2, service.getTemplate("template2").get());
        assertTrue(service.getTemplate("template3").isEmpty());
    }

    @Test
    @DisplayName("Вставка новых шаблонов c глубокими циклами приводит к ошибке")
    void saveTemplate_failOnLongCycle() {
        var template1 = new SimpleDocumentTemplate("#{template2}");
        var template2 = new SimpleDocumentTemplate("#{template3}");
        var template3 = new SimpleDocumentTemplate("#{template1}");
        service.saveTemplate("template1", template1);
        service.saveTemplate("template2", template2);

        var ex = assertThrows(IllegalStateException.class, () -> service.saveTemplate("template3", template3));
        assertEquals("Cycled dependency for template3", ex.getMessage());
    }

    @Test
    @DisplayName("Вставка новых шаблонов c короткими циклами приводит к ошибке")
    void saveTemplate_failOnShortCycle() {
        var template1 = new SimpleDocumentTemplate("#{template2}");
        var template2 = new SimpleDocumentTemplate("#{template1}");
        service.saveTemplate("template1", template1);

        var ex = assertThrows(IllegalStateException.class, () -> service.saveTemplate("template2", template2));
        assertEquals("Cycled dependency for template2", ex.getMessage());
    }

    @Test
    void removeTemplate() {
        assertTrue(service.getTemplate("template1").isEmpty());
        assertTrue(service.getTemplate("template2").isEmpty());
        assertTrue(service.getTemplate("template3").isEmpty());

        var template1 = new SimpleDocumentTemplate("#{template2}");
        var template2 = new SimpleDocumentTemplate("#{template3}");
        var template3 = new SimpleDocumentTemplate("#{template4}");

        service.saveTemplate("template1", template1);
        service.saveTemplate("template2", template2);
        service.saveTemplate("template3", template3);

        assertTrue(service.getTemplate("template1").isPresent());
        assertTrue(service.getTemplate("template2").isPresent());
        assertTrue(service.getTemplate("template3").isPresent());

        service.removeTemplate("template2");

        assertTrue(service.getTemplate("template1").isPresent());
        assertTrue(service.getTemplate("template2").isEmpty());
        assertTrue(service.getTemplate("template3").isPresent());
    }

    @Test
    void prepareCompositeTemplate() {
        var exceptedA = readFromSources("/actual/document_a.txt");
        var exceptedB = readFromSources("/actual/document_b.txt");

        var toSave = List.of("document_a", "document_b", "document_c");
        var prefix = "/templates/";
        toSave.stream()
                .map(it -> Map.entry(it, readFromSources(prefix + it + ".txt")))
                .forEach(e -> service.saveTemplate(e.getKey(), new SimpleDocumentTemplate(e.getValue())));

        var resultA = service.prepareCompositeTemplate("document_a");
        var resultB = service.prepareCompositeTemplate("document_b");

        assertEquals(exceptedA, resultA.map(IDocumentTemplate::getSource).orElse(null));
        assertEquals(exceptedB, resultB.map(IDocumentTemplate::getSource).orElse(null));
    }

    @Test
    @DisplayName("Проверка, что при вставке нового документа он применяется для компоновки остальных документов")
    void prepareCompositeTemplate_cache() {
        var a1 = new SimpleDocumentTemplate("#{external1}");
        var a2 = new SimpleDocumentTemplate("#{external2}");
        var b = new SimpleDocumentTemplate("BA: B#{A}");
        var c = new SimpleDocumentTemplate("CA: C#{A}");
        var d = new SimpleDocumentTemplate("BC: #{B} : #{C}");

        service.saveTemplate("A", a1);
        service.saveTemplate("B", b);
        service.saveTemplate("C", c);
        service.saveTemplate("D", d);

        var result1 = service.prepareCompositeTemplate("D");

        service.saveTemplate("A", a2);

        var result2 = service.prepareCompositeTemplate("D");

        assertTrue(result1.isPresent());
        assertTrue(result2.isPresent());
        assertNotEquals(result1.get().getSource(), result2.get().getSource());
    }

    private final Random random = new Random();

    private void saveDocument(String doc, IDocumentTemplate template) {
        service.saveTemplate(doc, template);
    }

    private void removeDocument(String doc) {
        service.removeTemplate(doc);
    }

    private Optional<IDocumentTemplate> getDocument(String doc) {
        return service.getTemplate(doc);
    }

    private Optional<IDocumentTemplate> requestDocument(String doc) {
        return service.prepareCompositeTemplate(doc);
    }

    private <T> T randomElement(Collection<T> collection) {
        var list = List.copyOf(collection);
        return list.get(random.nextInt(list.size()));
    }

    @RepeatedTest(10)
    @DisplayName("Проверка, что выполнение в многопоточной среде не приводит к возникновениям ошибок")
    void multithreadingTest() {
        final var tasksCount = 100;

        var documents = generateRandomDocuments(1000000);
        documents.forEach((k, v) -> service.saveTemplate(k, v));
        var tasks = IntStream.range(0, tasksCount)
                .mapToObj(i -> randomElement(documents.keySet()))
                .map(randKey -> switch (random.nextInt(4)) {
                    case 0 -> runAsync((() -> saveDocument(randKey, documents.get(randKey))));
                    case 1 -> runAsync((() -> removeDocument(randKey)));
                    case 2 -> supplyAsync((() -> getDocument(randKey)));
                    default -> supplyAsync((() -> requestDocument(randKey)));
                }).toList();

        var results = tasks.stream().map(this::getTask).toList();

        assertEquals(tasksCount, results.size());
    }

    @RepeatedTest(10)
    @DisplayName("Проверка, что выполнение в многопоточной среде не приводит к возникновениям ошибок")
    void multithreadingTestKostya() {
        final var tasksCount = 100000;

        var documents = generateRandomDocuments(1000000);
        documents.forEach((k, v) -> service.saveTemplate(k, v));
        IntStream.range(0, tasksCount)
                .mapToObj(i -> randomElement(documents.keySet()))
                .forEach(randKey -> {switch (random.nextInt(4)) {
//                    case 0 -> saveDocument(randKey, documents.get(randKey));
//                    case 1 -> removeDocument(randKey);
//                    case 2 -> {
//                        getDocument(randKey);
//                    }
                    default -> {
                        requestDocument(randKey);
                    }
                }
                });


    }


    @Test
    void clean() {
        assertTrue(service.getTemplate("template1").isEmpty());
        assertTrue(service.getTemplate("template2").isEmpty());

        var template1 = new SimpleDocumentTemplate("#{template2}");
        var template2 = new SimpleDocumentTemplate("#{template3}");
        service.saveTemplate("template1", template1);
        service.saveTemplate("template2", template2);

        assertTrue(service.getTemplate("template1").isPresent());
        assertTrue(service.getTemplate("template2").isPresent());

        service.clean();

        assertTrue(service.getTemplate("template1").isEmpty());
        assertTrue(service.getTemplate("template2").isEmpty());
    }

    double oneRepetitionBenchmark(long iteration) {
        final int count = 100;
        System.out.println("=============================");
        System.out.println("Iteration " + iteration);
        service.clean();
        var documents = generateRandomDocuments();
        documents.forEach((k, v) -> service.saveTemplate(k, v));
        System.out.println(documents.size() + " documents generated and saved.");
        double total = 0;
        for (int i = 0; i < count; ++i) {
            System.out.print("\rCase #" + i + "/" + count);
            System.out.flush();
            var key = randomElement(documents.keySet());
            var start = System.nanoTime();
            service.prepareCompositeTemplate(key);
            var end = System.nanoTime();
            total += (end - start);
        }
        System.out.println();
        System.out.println("Iteration finished");
        return total / count;
    }

    @Test
    @DisplayName("Замер производительности")
    void benchmark() {
        var result = LongStream.range(0L, 5L)
                .mapToDouble(this::oneRepetitionBenchmark)
                .map(it -> it / 1_000_000d)
                .peek(it -> System.out.println("Time: " + it + " ms"))
                .average();
        System.out.println("=============================");
        System.out.println("Average time: " + result.orElse(0) + " ms");
    }
}
