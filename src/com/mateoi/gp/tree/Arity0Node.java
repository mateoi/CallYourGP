package com.mateoi.gp.tree;

public abstract class Arity0Node extends Node {

    protected Arity0Node(String name) {
        super(name, 0);
    }

    @Override
    public void createChildren() {
        // Do nothing
    }
}
