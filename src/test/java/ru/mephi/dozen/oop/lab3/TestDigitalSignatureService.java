package ru.mephi.dozen.oop.lab3;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;
import ru.mephi.dozen.oop.lab3.service.DigitalSignatureService;

/**
 * Тестовый сервис подписи документов. Нарочито сделан мудреным, чтобы имитировать долгий процесс, который можно
 * распараллеливать. Криптографическая стойкость где-то около нуля :)
 */
public class TestDigitalSignatureService implements DigitalSignatureService {

    private static final BigInteger[] PRIMES = {
            new BigInteger("49211"), new BigInteger("49223"), new BigInteger("49253"), new BigInteger("49261"),
            new BigInteger("49277"), new BigInteger("49279"), new BigInteger("49297"), new BigInteger("49307"),
            new BigInteger("49331"), new BigInteger("49333"), new BigInteger("49339"), new BigInteger("49363"),
            new BigInteger("49367"), new BigInteger("49369"), new BigInteger("49391"), new BigInteger("49393"),
            new BigInteger("49409"), new BigInteger("49411"), new BigInteger("49417"), new BigInteger("49429"),
            new BigInteger("49433"), new BigInteger("49451"), new BigInteger("49459"), new BigInteger("49463"),
            new BigInteger("49477"), new BigInteger("49481"), new BigInteger("49499"), new BigInteger("49523"),
            new BigInteger("49529"), new BigInteger("49531"), new BigInteger("49537"), new BigInteger("49547"),
            new BigInteger("49549"), new BigInteger("49559"), new BigInteger("49597"), new BigInteger("49603"),
            new BigInteger("49613"), new BigInteger("49627"), new BigInteger("49633"), new BigInteger("49639"),
            new BigInteger("49663"), new BigInteger("49667"), new BigInteger("49669"), new BigInteger("49681"),
            new BigInteger("49697"), new BigInteger("49711"), new BigInteger("49727"), new BigInteger("49739"),
            new BigInteger("49741"), new BigInteger("49747"), new BigInteger("49757"), new BigInteger("49783"),
            new BigInteger("49787"), new BigInteger("49789"), new BigInteger("49801"), new BigInteger("49807"),
            new BigInteger("49811"), new BigInteger("49823"), new BigInteger("49831"), new BigInteger("49843"),
            new BigInteger("49853"), new BigInteger("49871"), new BigInteger("49877"), new BigInteger("49891"),
            new BigInteger("49919"), new BigInteger("49921"), new BigInteger("49927"), new BigInteger("49937"),
            new BigInteger("49939"), new BigInteger("49943"), new BigInteger("49957"), new BigInteger("49991"),
            new BigInteger("49993"), new BigInteger("49999"),
    };


    private static final int BIT_COUNT = 1024;
    private static final int BIT_COUNT_HALF = BIT_COUNT / 2;
    private static final BigInteger MODULUS = new BigInteger("2").pow(BIT_COUNT);

    private BigInteger map(AtomicInteger index, int value) {
        int v = index.getAndIncrement() % PRIMES.length;
        return PRIMES[v].multiply(BigInteger.valueOf(value)).mod(MODULUS);
    }

    private BigInteger shuffle(BigInteger i) {
        return i.xor(i.shiftLeft(BIT_COUNT_HALF));
    }

    private BigInteger addByMod(BigInteger a, BigInteger b) {
        return a.add(b).mod(MODULUS);
    }

    /**
     * Имитирует бурную деятельность по подсчету подписи
     *
     * @param str документ для подписи
     * @return подпись
     */
    @Override
    public String sign(String str) {
        final var index = new AtomicInteger(0);
        var result = str.chars()
                .mapToObj(ch -> map(index, ch))
                .map(this::shuffle)
                .reduce(BigInteger.ZERO, this::addByMod);
        return String.format("%0256X", result);
    }
}
