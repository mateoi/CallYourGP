package com.mateoi.gp.tree;

public abstract class Arity0Node<T> extends Node<T> {

    protected Arity0Node(String name, Class<T> type) {
        super(name, 0, type);
    }

    @Override
    protected void createChildren() {
        // Do nothing
    }
}
