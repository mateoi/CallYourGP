package com.mateoi.gp.tree;

public abstract class Arity3Node extends Node {

    private final TriFunction<Integer, Integer, Integer, Integer> function;

    public Arity3Node(String name, int depth, TriFunction<Integer, Integer, Integer, Integer> function) {
        super(name, depth);
        this.function = function;
    }

    @Override
    public int evaluate() {
        final Node child1 = getArguments().get(0);
        final int value1 = child1.evaluate();
        final Node child2 = getArguments().get(1);
        final int value2 = child2.evaluate();
        final Node child3 = getArguments().get(2);
        final int value3 = child3.evaluate();
        return function.apply(value1, value2, value3);
    }

}
