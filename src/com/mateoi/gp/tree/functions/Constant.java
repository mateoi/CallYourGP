package com.mateoi.gp.tree.functions;

import com.mateoi.gp.tree.Arity0Node;
import com.mateoi.gp.tree.Node;

public class Constant extends Arity0Node {

    private final double value;

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
