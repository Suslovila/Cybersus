package ru.mephi.dozen.oop.lab3;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import ru.mephi.dozen.oop.lab3.model.IDocumentTemplate;
import ru.mephi.dozen.oop.lab3.model.SimpleDocumentTemplate;
import ru.mephi.dozen.oop.lab3.service.impl.BlockingDocumentService;

public class AbstractTest {

    @SneakyThrows
    protected String readFromSources(String path) {
        var aPath = path.startsWith("/") ? path : '/' + path;
        try (var reader = getClass().getResourceAsStream(aPath)) {
            return new String(reader.readAllBytes());
        }
    }

    private final Random rand = new Random();
    private static final String ALPHABET = "ABCDEFGKLMNPQRSTUVWXYZabcdefgklmnpqrstuvwxyz0123456789_ ";

    private String getLetterOrDep(Set<String> dependencies, double probability) {
        if (!dependencies.isEmpty() && rand.nextDouble() < probability) {
            var list = new ArrayList<>(dependencies);
            Collections.shuffle(list, rand);
            return "#{" + list.stream().findFirst() + "}";
        }
        var pos = rand.nextInt(ALPHABET.length());
        return "" + ALPHABET.charAt(pos);
    }

    private String randomKey() {
        var key = new StringBuilder();
        var size = rand.nextInt(5, 50);
        for (int i = 0; i < size; ++i) {
            key.append(ALPHABET.charAt(rand.nextInt(ALPHABET.length() - 1)));
        }
        return key.toString();
    }

    protected IDocumentTemplate generateRandomDocument(Set<String> dependencies, int minSize, int maxSize,
            double probability) {
        var size = rand.nextInt(minSize, maxSize);
        StringBuilder bldr = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            bldr.append(getLetterOrDep(dependencies, probability));
        }
        return new SimpleDocumentTemplate(bldr.toString());
    }

    private String nextKey(Set<String> used) {
        var key = randomKey();
        while (used.contains(key)) {
            key = randomKey();
        }
        return key;
    }

    protected Map<String, IDocumentTemplate> generateRandomDocuments() {
        Map<String, IDocumentTemplate> documents = new HashMap<>();
        for (int i = 0; i < 100; ++i) {
            var key = nextKey(documents.keySet());
            documents.put(key, generateRandomDocument(Set.of(), 10, 100, 0));
        }
        for (int j = 0; j < 4; ++j) {
            for (int i = 0; i < 25; ++i) {
                var key = nextKey(documents.keySet());
                documents.put(key, generateRandomDocument(documents.keySet(), 100, 200, 0.01));
            }
        }
        for (int i = 0; i < 50; ++i) {
            var key = nextKey(documents.keySet());
            documents.put(key, generateRandomDocument(documents.keySet(), 100_000, 200_000, 0.0002));
        }
        return documents;
    }

    protected Map<String, IDocumentTemplate> generateRandomDocuments(int n) {
        var generated = generateRandomDocuments();
        if (n >= generated.size()) {
            return generated;
        }
        var documents = new HashMap<String, IDocumentTemplate>();
        while (documents.size() < n) {
            var list = List.copyOf(generated.keySet());
            var el = list.get(rand.nextInt(list.size()));
            documents.put(el, generated.get(el));
        }
        return documents;
    }

    @SneakyThrows
    protected <T> T getTask(Future<T> future) {
        return future.get(30, TimeUnit.SECONDS);
    }
}

class TestGenerator extends AbstractTest {

    @RepeatedTest(10)
    @DisplayName("Проверка, что генератор документов не создает циклы")
    void testGenerate() {
        var templates = generateRandomDocuments();
        var service = new BlockingDocumentService(new TestDigitalSignatureService());
        assertDoesNotThrow(() -> templates.forEach(service::saveTemplate));
    }
}
