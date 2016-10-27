package com.mateoi.gp.tree.functions;

import java.util.ArrayList;
import java.util.List;

import com.mateoi.gp.tree.Arity3Node;
import com.mateoi.gp.tree.Node;

public class IfNode extends Arity3Node {

    public IfNode(int depth) {
        super("if", depth, (test, ifTrue, ifFalse) -> (test == 1) ? ifTrue : ifFalse);
        createChildren();
    }

    public IfNode(int depth, Node test, Node ifTrue, Node ifFalse) {
        super("if", depth, (b, t, f) -> b == 1 ? t : f);
        final List<Node> children = new ArrayList<>();
        children.add(test);
        children.add(ifTrue);
        children.add(ifFalse);
        setArguments(children);

    }

    @Override
    public int evaluate() { // Override to shortcut execution
        final Node test = getArguments().get(0);
        final boolean testValue = test.evaluate() == 1;
        final int index = testValue ? 1 : 2;
        final Node result = getArguments().get(index);
        return result.evaluate();
    }

    @Override
    public Node copy() {
        final Node test = getArguments().get(0).copy();
        final Node ifTrue = getArguments().get(1).copy();
        final Node ifFalse = getArguments().get(2).copy();
        return new IfNode(getDepth(), test, ifTrue, ifFalse);
    }
}
