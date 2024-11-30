package com.suslovila.cybersus.utils;

@FunctionalInterface
public interface Converter<T, R> {
    R convert(T input);
}