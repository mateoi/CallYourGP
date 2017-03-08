package com.mateoi.gp.tree.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.mateoi.gp.tree.Arity2Node;
import com.mateoi.gp.tree.Node;

/**
 * Class of nodes that implement boolean algebra functions.
 *
 * @author mateo
 *
 */
public class BooleanNode extends Arity2Node {

    /**
     * Create a new BooleanNode node with the given maximum depth and function
     * and give it randomly generated children.
     *
     * @param depth
     */
    public BooleanNode(String name, int depth, BiFunction<Double, Double, Double> function) {
        super(name, depth, function);
        createChildren();
    }

    /**
     * Create a new BooleanNode node with the given maximum depth, function, and
     * children.
     *
     * @param depth
     * @param left
     * @param right
     */
    public BooleanNode(String name, int depth, BiFunction<Double, Double, Double> function, Node left, Node right) {
        super(name, depth, function);
        List<Node> children = new ArrayList<>();
        children.add(left);
        children.add(right);
        setArguments(children);
    }

    /**
     * Create a new node with the given maximum depth that represents the
     * boolean AND function.
     *
     * @param depth
     * @return A node that evaluates to 1 iff both arguments evaluate to 1 or
     *         more; 0 otherwise.
     */
    public static BooleanNode and(int depth) {
        return new BooleanNode("and", depth, (a, b) -> (a >= 1 && b >= 1) ? 1. : 0.);
    }

    /**
     * Create a new node with the given maximum depth that represents the
     * boolean OR function.
     *
     * @param depth
     * @return A node that evaluates to 1 iff either argument evaluates to 1 or
     *         more; 0 otherwise.
     */
    public static BooleanNode or(int depth) {
        return new BooleanNode("or", depth, (a, b) -> (a >= 1 || b >= 1) ? 1. : 0.);
    }

    /**
     * Create a new node with the given maximum depth that represents the
     * boolean XOR function.
     *
     * @param depth
     * @return A node that evaluates to 1 iff one, but not both, arguments
     *         evaluate to 1 or more; 0 otherwise.
     */
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
