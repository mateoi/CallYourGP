package com.mateoi.gp.tree;

/**
 * A functional interface for a function with three arguments.
 *
 * @author mateo
 *
 * @param <A>
 *            Type of the first argument
 * @param <B>
 *            Type of the second argument
 * @param <C>
 *            Type of the third argument
 * @param <T>
 *            Return type
 */
@FunctionalInterface
public interface TriFunction<A, B, C, T> {

    /**
     * Apply the function to the given arguments
     * 
     * @param a
     * @param b
     * @param c
     * @return
     */
    public T apply(A a, B b, C c);
}
