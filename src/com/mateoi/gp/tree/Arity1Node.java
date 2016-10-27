package com.mateoi.gp.tree;

import java.util.function.Function;

public abstract class Arity1Node extends Node {

    private final Function<Integer, Integer> function;

    public Arity1Node(String name, int depth, Function<Integer, Integer> function) {
        super(name, depth);
        this.function = function;
    }

    @Override
    public int evaluate() {
        final Node child = getArguments().get(0);
        final int value = child.evaluate();
        return function.apply(value);
    }
}
