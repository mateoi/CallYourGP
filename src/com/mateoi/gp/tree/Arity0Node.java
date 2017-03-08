package com.mateoi.gp.tree;

/**
 * Abstract class of nodes that have no arguments
 *
 * @author mateo
 *
 */
public abstract class Arity0Node extends Node {

    /**
     * Assign the given name to the node, and set the maximum depth to 0.
     *
     * @param name
     */
    public Arity0Node(String name) {
        super(name, 0);
    }

    @Override
    public void createChildren() {
        // Do nothing
    }
}
