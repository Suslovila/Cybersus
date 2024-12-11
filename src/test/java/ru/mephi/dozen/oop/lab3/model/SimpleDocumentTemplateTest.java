package ru.mephi.dozen.oop.lab3.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SimpleDocumentTemplateTest {

    @Test
    void getDefinedBindings() {
        var document = new SimpleDocumentTemplate("lol #{kek} pek\naustra#{lo}{#pitek}\t#{kek}");
        var expected = Set.of("kek", "lo");
        var actual = document.getDefinedBindings();
        assertEquals(expected, actual);
    }

    @Test
    void fillIfPresent() {
        var document = new SimpleDocumentTemplate("author: #{author}\nemail: #{email}\neditor: #{author}\n{#editor}");
        var expected = "author: A\nemail: #{email}\neditor: A\n{#editor}";
        var actual = document.fillIfPresent(Map.of("author", "A", "editor", "E"));
        assertEquals(expected, actual);
    }

    @Test
    void fill_ok() {
        var document = new SimpleDocumentTemplate("author: #{author}\nemail: #{email}\neditor: #{author}\n{#editor}");
        var expected = "author: A\nemail: B@C.D\neditor: A\n{#editor}";
        var actual = document.fill(Map.of("author", "A", "email", "B@C.D", "editor", "E"));
        assertEquals(expected, actual);
    }


    @Test
    void fill_recursive() {
        var document = new SimpleDocumentTemplate("author: #{author} #{date}");
        var expected1 = "author: #{first_name} #{middle_name} #{last_name} 2000-01-01";
        var expected2 = "author: James Tiberius Kirk 2000-01-01";

        var data = Map.of("author", "#{first_name} #{middle_name} #{last_name}",
                "first_name", "James",
                "middle_name", "Tiberius",
                "last_name", "Kirk",
                "date", "2000-01-01");

        var actual = document.fill(data);
        var actual2 = new SimpleDocumentTemplate(actual).fill(data);

        assertEquals(expected1, actual);
        assertEquals(expected2, actual2);
    }

    @Test
    void fill_absentBinding() {
        var document = new SimpleDocumentTemplate("author: #{author}\nemail: #{email}\neditor: #{author}\n{#editor}");
        var data = Map.of("author", "A", "editor", "E");
        var exception = assertThrows(NoSuchElementException.class, () -> document.fill(data));
        assertEquals("Key 'email' not found", exception.getMessage());
    }

    @Test
    void noBinding() {
        var document = new SimpleDocumentTemplate("This document does not contains bindings");
        var bindings = document.getDefinedBindings();
        assertTrue(bindings.isEmpty());
    }
}