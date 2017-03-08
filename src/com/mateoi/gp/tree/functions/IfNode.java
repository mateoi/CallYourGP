package com.mateoi.gp.tree.functions;

import java.util.ArrayList;
import java.util.List;

import com.mateoi.gp.tree.Arity3Node;
import com.mateoi.gp.tree.Node;

/**
 * A ternary node that returns the second argument if the first one is greater
 * than or equal to 1 and returns the third one otherwise
 *
 * @author mateo
 *
 */
public class IfNode extends Arity3Node {

    /**
     * Create a new IfNode node with the given maximum depth and give it
     * randomly generated children.
     *
     * @param depth
     */
    public IfNode(int depth) {
        // The function here is for completeness, it shouldn't be evaluated
        // because the evaluate method is overridden.
        super("if", depth, (test, ifTrue, ifFalse) -> (test >= 1) ? ifTrue : ifFalse);
        createChildren();
    }

    /**
     * Create a new IfNode node with the given maximum depth and children.
     *
     * @param depth
     * @param left
     * @param right
     */
    public IfNode(int depth, Node test, Node ifTrue, Node ifFalse) {
        // The function here is for completeness, it shouldn't be evaluated
        // because the evaluate method is overridden.
        super("if", depth, (b, t, f) -> b >= 1 ? t : f);
        List<Node> children = new ArrayList<>();
        children.add(test);
        children.add(ifTrue);
        children.add(ifFalse);
        setArguments(children);
    }

    @Override
    public double evaluate() { // Override to shortcut execution
        Node test = getArguments().get(0);
        boolean testValue = test.evaluate() >= 1;
        int index = testValue ? 1 : 2;
        Node result = getArguments().get(index);
        return result.evaluate();
    }

    @Override
    public Node copy() {
        Node test = getArguments().get(0).copy();
        Node ifTrue = getArguments().get(1).copy();
        Node ifFalse = getArguments().get(2).copy();
        return new IfNode(getDepth(), test, ifTrue, ifFalse);
    }
}
