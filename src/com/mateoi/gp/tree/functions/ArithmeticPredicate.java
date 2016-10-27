package com.mateoi.gp.tree.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.mateoi.gp.tree.Arity2Node;
import com.mateoi.gp.tree.Node;

public class ArithmeticPredicate extends Arity2Node {

    public ArithmeticPredicate(String name, int depth, BiFunction<Integer, Integer, Integer> function) {
        super(name, depth, function);
        createChildren();
    }

    public ArithmeticPredicate(String name, int depth, BiFunction<Integer, Integer, Integer> function, Node left,
            Node right) {
        super(name, depth, function);
        final List<Node> children = new ArrayList<>();
        children.add(left);
        children.add(right);
        setArguments(children);
    }

    public static ArithmeticPredicate equalsNode(int depth) {
        return new ArithmeticPredicate("==", depth, (a, b) -> a == b ? 1 : 0);
    }

    public static ArithmeticPredicate gt(int depth) {
        return new ArithmeticPredicate(">", depth, (a, b) -> a > b ? 1 : 0);
    }

    @Override
    public Node copy() {
        final Node left = getArguments().get(0).copy();
        final Node right = getArguments().get(1).copy();
        return new ArithmeticPredicate(getName(), getDepth(), getFunction(), left, right);
    }
}
