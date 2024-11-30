package com.suslovila.cybersus.utils;

public class NbtKeyNameHelper {
    String name;
    public NbtKeyNameHelper(String name) {
        this.name = name;
    }

    public String doAndGet(String key) {
        return name + ":" + key;
    }
}
