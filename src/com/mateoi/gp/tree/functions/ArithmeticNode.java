package com.mateoi.gp.tree.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.mateoi.gp.tree.Arity2Node;
import com.mateoi.gp.tree.Node;

public class ArithmeticNode extends Arity2Node {

    public ArithmeticNode(String name, int depth, BiFunction<Integer, Integer, Integer> function) {
        super(name, depth, function);
        createChildren();
    }

    public ArithmeticNode(String name, int depth, BiFunction<Integer, Integer, Integer> function, Node left,
            Node right) {
        super(name, depth, function);
        final List<Node> children = new ArrayList<>();
        children.add(left);
        children.add(right);
        setArguments(children);
    }

    public static ArithmeticNode plus(int depth) {
        return new ArithmeticNode("+", depth, (a, b) -> a + b);
    }

    public static ArithmeticNode minus(int depth) {
        return new ArithmeticNode("-", depth, (a, b) -> a - b);
    }

    public static ArithmeticNode times(int depth) {
        return new ArithmeticNode("*", depth, (a, b) -> a * b);
    }

    public static ArithmeticNode div(int depth) {
        return new ArithmeticNode("/", depth, (a, b) -> b == 0 ? a : a / b);
    }

    public static ArithmeticNode mod(int depth) {
        return new ArithmeticNode("mod", depth, (a, b) -> b == 0 ? a : a % b);
    }

    @Override
    public Node copy() {
        final Node left = getArguments().get(0).copy();
        final Node right = getArguments().get(1).copy();
        return new ArithmeticNode(getName(), getDepth(), getFunction(), left, right);
    }
}
