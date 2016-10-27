package com.mateoi.gp.tree.functions;

import com.mateoi.gp.tree.Arity0Node;
import com.mateoi.gp.tree.Node;

public class Constant extends Arity0Node {

    private final int value;

    public Constant(int value) {
        super(Integer.toString(value));
        this.value = value;
    }

    @Override
    public int evaluate() {
        return value;
    }

    @Override
    public Node copy() {
        return new Constant(value);
    }
}
