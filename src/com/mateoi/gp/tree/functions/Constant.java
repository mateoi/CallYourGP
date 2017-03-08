package com.mateoi.gp.tree.functions;

import com.mateoi.gp.tree.Arity0Node;
import com.mateoi.gp.tree.Node;

/**
 * An arity-zero node that returns a constant value.
 *
 * @author mateo
 *
 */
public class Constant extends Arity0Node {

    /** This node's return value */
    private final double value;

    /**
     * Create a new WriteMemory node with the given value.
     *
     * @param depth
     */
    public Constant(double value) {
        super(Double.toString(value));
        this.value = value;
    }

    @Override
    public double evaluate() {
        return value;
    }

    @Override
    public Node copy() {
        return new Constant(value);
    }
}
