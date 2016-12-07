package com.mateoi.gp.tree;

import java.util.function.Function;

import com.mateoi.gp.exceptions.NoConstructorsSet;

public abstract class Arity1Node extends Node {

    private final Function<Double, Double> function;

    public Arity1Node(String name, int depth, Function<Double, Double> function) {
        super(name, depth);
        this.function = function;
    }

    @Override
    public double evaluate() {
        Node child = getArguments().get(0);
        double value = child.evaluate();
        return function.apply(value);
    }

    @Override
    public void createChildren() {
        try {
            Node child = NodeFactory.getInstance().createRandomNode(getDepth() - 1);
            getArguments().clear();
            getArguments().add(child);
        } catch (NoConstructorsSet e) {
            System.exit(1);
        }
    }
}
