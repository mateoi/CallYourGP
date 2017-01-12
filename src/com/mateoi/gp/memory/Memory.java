package com.mateoi.gp.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Memory {

    private static Supplier<Memory> memorySupplier;
    private Map<Integer, Double> store = new HashMap<>();

    public Memory() {
        // Nothing here
    }

    public double get(int index) {
        return store.getOrDefault(index, 0.);
    }

    public double put(int index, double value) {
        double oldValue = get(index);
        store.put(index, value);
        return oldValue;
    }

    public static Supplier<Memory> getMemorySupplier() {
        return memorySupplier;
    }

    public static void setMemorySupplier(Supplier<Memory> memorySupplier) {
        Memory.memorySupplier = memorySupplier;
    }
}
