package com.mateoi.gp.tree;

import java.util.function.Function;

import com.mateoi.gp.exceptions.NoConstructorsSet;

public abstract class Arity1Node extends Node {

    private final Function<Integer, Integer> function;

    public Arity1Node(String name, int depth, Function<Integer, Integer> function) {
        super(name, depth);
        this.function = function;
    }

    @Override
    public int evaluate() {
        Node child = getArguments().get(0);
        int value = child.evaluate();
        return function.apply(value);
    }

    @Override
    public void createChildren() {
        try {
            Node child = NodeFactory.getInstance().createRandomNode(getDepth() - 1);
            getArguments().add(child);
        } catch (NoConstructorsSet e) {
            System.exit(1);
        }
    }
}
