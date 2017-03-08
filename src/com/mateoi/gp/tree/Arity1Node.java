package com.mateoi.gp.tree;

import java.util.function.Function;

import com.mateoi.gp.exceptions.NoConstructorsSet;

/**
 * Abstract class of nodes that have one child.
 *
 * @author mateo
 *
 */
public abstract class Arity1Node extends Node {

    /** Function to evaluate */
    private final Function<Double, Double> function;

    /**
     * Create a new node with the given name, depth and function.
     *
     * @param name
     * @param depth
     * @param function
     */
    public Arity1Node(String name, int depth, Function<Double, Double> function) {
        super(name, depth);
        this.function = function;
    }

    /**
     * @return This node's function.
     */
    public Function<Double, Double> getFunction() {
        return function;
    }

    @Override
    public double evaluate() {
        Node child = getArguments().get(0);
        double value = child.evaluate();
        return function.apply(value);
    }

    @Override
    public void createChildren() {
        try {
            Node child = NodeFactory.getInstance().createRandomNode(getDepth() - 1);
            getArguments().clear();
            getArguments().add(child);
        } catch (NoConstructorsSet e) {
            System.out.println("No constructors set!");
            System.exit(1);
        }
    }
}
