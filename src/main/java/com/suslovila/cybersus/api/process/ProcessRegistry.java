package com.suslovila.cybersus.api.process;

import java.util.HashMap;

public class ProcessRegistry {
    private static final HashMap<String, Class<? extends WorldProcess>> nameToClass = new HashMap<>();
    private static final HashMap<Class<? extends WorldProcess>, String> classToName = new HashMap<>();

    public static void registerProcess(Class<? extends WorldProcess> classZ, String key) {
        if (nameToClass.containsKey(key)) {
            throw new IllegalArgumentException("Duplicate key: " + key);
        }
        if (classToName.containsKey(classZ)) {
            throw new IllegalArgumentException("Duplicate class Type: " + key);

        }
        nameToClass.put(key, classZ);
        classToName.put(classZ, key);
    }

    public static Class<? extends WorldProcess> getClassType(String key) {
        return nameToClass.get(key);
    }

    public static String getKey(Class<? extends WorldProcess> clazz) {
        return classToName.get(clazz);
    }
}

