package com.mateoi.gp.tree;

import java.util.function.Function;

import com.mateoi.gp.exceptions.TypeMismatch;

public class Arity1Node<A, T> extends Node<T> {

    private final Function<A, T> function;

    public Arity1Node(String name, int depth, Class<T> returnType, Class<A> argType, Function<A, T> function) {
        super(name, depth, returnType, argType);
        this.function = function;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T evaluate() throws TypeMismatch {
        if (!checkTypes()) {
            throw new TypeMismatch();
        }
        final Node<A> child = this.arguments.get(0);
        final A value = child.evaluate();
        return this.function.apply(value);
    }
}
