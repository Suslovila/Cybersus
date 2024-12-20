package com.suslovila.cybersus.utils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;
public class SusCollectionUtils {
        public static <T> Optional<T> first(Iterable<T> iterable, Predicate<T> predicate) {
            for (T element : iterable) {
                if (predicate.test(element)) {
                    return Optional.ofNullable(element);
                }
            }
            return Optional.empty();
        }


    public static <T> ArrayList<T> arrayListOf(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }


}
