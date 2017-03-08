package com.mateoi.gp.tree.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import com.mateoi.gp.tree.Arity2Node;
import com.mateoi.gp.tree.Node;

/**
 * Class of nodes that implement basic arithmetic operations
 *
 * @author mateo
 *
 */
public class ArithmeticNode extends Arity2Node {

    /**
     * Create a new ArithmeticNode node with the given maximum depth and
     * function and give it randomly generated children.
     *
     * @param depth
     */
    public ArithmeticNode(String name, int depth, BiFunction<Double, Double, Double> function) {
        super(name, depth, function);
        createChildren();
    }

    /**
     * Create a new ArithmeticNode node with the given maximum depth, function,
     * and children.
     *
     * @param depth
     * @param left
     * @param right
     */
    public ArithmeticNode(String name, int depth, BiFunction<Double, Double, Double> function, Node left, Node right) {
        super(name, depth, function);
        List<Node> children = new ArrayList<>();
        children.add(left);
        children.add(right);
        setArguments(children);
    }

    /**
     * Create a new node that calculates the sum of its arguments.
     *
     * @param depth
     *            Maximum depth of the node
     * @return
     */
    public static ArithmeticNode plus(int depth) {
        return new ArithmeticNode("+", depth, (a, b) -> a + b);
    }

    /**
     * Create a new node that subtracts the second argument from the first.
     *
     * @param depth
     *            Maximum depth of the node
     * @return
     */
    public static ArithmeticNode minus(int depth) {
        return new ArithmeticNode("-", depth, (a, b) -> a - b);
    }

    /**
     * Create a new node that calculates the product of its arguments.
     *
     * @param depth
     *            Maximum depth of the node
     * @return
     */
    public static ArithmeticNode times(int depth) {
        return new ArithmeticNode("*", depth, (a, b) -> a * b);
    }

    /**
     * Create a new node that divides the first argument by the second. If the
     * second argument is zero, simply returns the first argument.
     *
     * @param depth
     *            Maximum depth of the node
     * @return
     */
    public static ArithmeticNode div(int depth) {
        return new ArithmeticNode("/", depth, (a, b) -> b == 0 ? a : a / b);
    }

    /**
     * Create a new node that calculates the first argument modulo the second.
     * If the second argument is zero, simply returns the first argument.
     *
     * @param depth
     *            Maximum depth of the node
     * @return
     */
    public static ArithmeticNode mod(int depth) {
        return new ArithmeticNode("mod", depth, (a, b) -> b == 0 ? a : a % b);
    }

    @Override
    public Node copy() {
        Node left = getArguments().get(0).copy();
        Node right = getArguments().get(1).copy();
        return new ArithmeticNode(getName(), getDepth(), getFunction(), left, right);
    }
}
