package com.mateoi.gp.tree;

import java.util.function.BiFunction;

import com.mateoi.gp.exceptions.TypeMismatch;

public class Arity2Node<A, B, T> extends Node<T> {
    private final BiFunction<A, B, T> function;

    public Arity2Node(String name, int depth, Class<T> returnType, Class<A> argType1, Class<B> argType2,
            BiFunction<A, B, T> function) {
        super(name, depth, returnType, argType1, argType2);
        this.function = function;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T evaluate() throws TypeMismatch {
        if (!checkTypes()) {
            throw new TypeMismatch();
        }
        final Node<A> child1 = this.arguments.get(0);
        final A value1 = child1.evaluate();
        final Node<B> child2 = this.arguments.get(1);
        final B value2 = child2.evaluate();
        return this.function.apply(value1, value2);
    }

}
