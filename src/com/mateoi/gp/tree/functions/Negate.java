package com.mateoi.gp.tree.functions;

import java.util.ArrayList;
import java.util.List;

import com.mateoi.gp.tree.Arity1Node;
import com.mateoi.gp.tree.Node;

/**
 * A unary node that negates its argument.
 * 
 * @author mateo
 *
 */
public class Negate extends Arity1Node {

    /**
     * Create a new Negate node with the given maximum depth and give it a
     * randomly generated child.
     *
     * @param depth
     */
    public Negate(int depth) {
        super("neg", depth, d -> -d);
        createChildren();
    }

    /**
     * Create a new Negate node with the given maximum depth and child.
     * 
     * @param depth
     * @param left
     * @param right
     */
    public Negate(int depth, Node child) {
        super("neg", depth, d -> -d);
        List<Node> children = new ArrayList<>();
        children.add(child);
        setArguments(children);
    }

    @Override
    public Node copy() {
        return new Negate(getDepth(), getArguments().get(0).copy());
    }
}
