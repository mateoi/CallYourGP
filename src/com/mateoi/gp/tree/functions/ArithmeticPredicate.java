package com.mateoi.gp.tree.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.mateoi.gp.tree.Arity2Node;
import com.mateoi.gp.tree.Node;

/**
 * A class of nodes that implement arithmetic predicates.
 *
 * @author mateo
 *
 */
public class ArithmeticPredicate extends Arity2Node {

    /**
     * Create a new ArithmeticPredicate node with the given maximum depth and
     * function and give it randomly generated children.
     *
     * @param depth
     */
    public ArithmeticPredicate(String name, int depth, BiFunction<Double, Double, Double> function) {
        super(name, depth, function);
        createChildren();
    }

    /**
     * Create a new ArithmeticPredicate node with the given maximum depth,
     * function, and children.
     *
     * @param depth
     * @param left
     * @param right
     */
    public ArithmeticPredicate(String name, int depth, BiFunction<Double, Double, Double> function, Node left,
            Node right) {
        super(name, depth, function);
        List<Node> children = new ArrayList<>();
        children.add(left);
        children.add(right);
        setArguments(children);
    }

    /**
     * Create a new node that checks if its arguments are equal
     *
     * @param depth
     *            Maximum depth of the node
     * @return A node that evaluates to 1 if both arguments are equal; 0
     *         otherwise.
     */
    public static ArithmeticPredicate equalsNode(int depth) {
        return new ArithmeticPredicate("==", depth, (a, b) -> a == b ? 1. : 0.);
    }

    /**
     * Create a new node that checks if the first argument is greater than the
     * second
     *
     * @param depth
     *            Maximum depth of the node
     * @return A node that evaluates to 1 if the first argument is greater than
     *         the first; 0 otherwise.
     */
    public static ArithmeticPredicate gt(int depth) {
        return new ArithmeticPredicate(">", depth, (a, b) -> a > b ? 1. : 0.);
    }

    @Override
    public Node copy() {
        Node left = getArguments().get(0).copy();
        Node right = getArguments().get(1).copy();
        return new ArithmeticPredicate(getName(), getDepth(), getFunction(), left, right);
    }
}
