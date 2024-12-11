package ru.mephi.dozen.oop.lab3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestDigitalSignatureServiceTest {

    @Test
    void sign() {
        var expected = "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003038A2000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000003038A20";
        var service = new TestDigitalSignatureService();

        var string = "lol kek pek";
        var result = service.sign(string);

        Assertions.assertEquals(expected, result);
    }
}