package com.mateoi.gp.tree.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.mateoi.gp.tree.Arity2Node;
import com.mateoi.gp.tree.Node;

public class BooleanNode extends Arity2Node {

    public BooleanNode(String name, int depth, BiFunction<Double, Double, Double> function) {
        super(name, depth, function);
        createChildren();
    }

    public BooleanNode(String name, int depth, BiFunction<Double, Double, Double> function, Node left, Node right) {
        super(name, depth, function);
        List<Node> children = new ArrayList<>();
        children.add(left);
        children.add(right);
        setArguments(children);
    }

    public static BooleanNode and(int depth) {
        return new BooleanNode("and", depth, (a, b) -> (a >= 1 && b >= 1) ? 1. : 0.);
    }

    public static BooleanNode or(int depth) {
        return new BooleanNode("or", depth, (a, b) -> (a >= 1 || b >= 1) ? 1. : 0.);
    }

    public static BooleanNode xor(int depth) {
        return new BooleanNode("xor", depth, (a, b) -> (a >= 1 && b < 1 || a < 1 && b >= 1) ? 1. : 0.);
    }

    @Override
    public Node copy() {
        Node left = getArguments().get(0).copy();
        Node right = getArguments().get(1).copy();
        return new BooleanNode(getName(), getDepth(), getFunction(), left, right);
    }

}
