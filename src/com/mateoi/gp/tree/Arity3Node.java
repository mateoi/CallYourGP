package com.mateoi.gp.tree;

import com.mateoi.gp.exceptions.TypeMismatch;

public class Arity3Node<A, B, C, T> extends Node<T> {

    private final TriFunction<A, B, C, T> function;

    public Arity3Node(String name, int depth, Class<T> returnType, Class<A> argType1, Class<B> argType2,
            Class<C> argType3, TriFunction<A, B, C, T> function) {
        super(name, depth, returnType, argType1, argType2, argType3);
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
        final Node<C> child3 = this.arguments.get(2);
        final C value3 = child3.evaluate();
        return this.function.apply(value1, value2, value3);
    }

}
