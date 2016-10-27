package com.mateoi.gp.tree;

import java.util.function.BiFunction;

public abstract class Arity2Node extends Node {
    private final BiFunction<Integer, Integer, Integer> function;

    public Arity2Node(String name, int depth, BiFunction<Integer, Integer, Integer> function) {
        super(name, depth);
        this.function = function;
    }

    @Override
    public int evaluate() {
        final Node child1 = getArguments().get(0);
        final int value1 = child1.evaluate();
        final Node child2 = getArguments().get(1);
        final int value2 = child2.evaluate();
        return function.apply(value1, value2);
    }

    public BiFunction<Integer, Integer, Integer> getFunction() {
        return function;
    }

}
