package com.mateoi.gp.memory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Implementation of indexed memory for GP. There is a static memorySupplier,
 * which returns the memory currently in use, depending on some exterior game
 * state. Internally, Memory objects present an array-like memory which can be
 * stored to or read from.
 *
 * @author mateo
 *
 */
public class Memory {

    /** The supplier that determines what memory to use */
    private static Supplier<Memory> memorySupplier;
    /** The map that stores the array-like indexed memory */
    private Map<Integer, Double> store = new HashMap<>();

    public Memory() {
        // Nothing here
    }

    /**
     * Fetch the value currently stored in the array at the given index. If
     * nothing has been stored at that location, the value 0 is returned.
     *
     * @param index
     * @return
     */
    public double get(int index) {
        return store.getOrDefault(index, 0.);
    }

    /**
     * Store the given value at the given location in the array. Returns the
     * previous value stored in the array, or 0 if nothing has been stored there
     * yet.
     *
     * @param index
     * @param value
     * @return
     */
    public double put(int index, double value) {
        double oldValue = get(index);
        store.put(index, value);
        return oldValue;
    }

    /**
     * @return The supplier that returns the currently used memory.
     */
    public static Supplier<Memory> getMemorySupplier() {
        return memorySupplier;
    }

    /**
     * Set the supplier that serves memory
     * 
     * @param memorySupplier
     */
    public static void setMemorySupplier(Supplier<Memory> memorySupplier) {
        Memory.memorySupplier = memorySupplier;
    }
}
