package com.mateoi.gp.tree;

import com.mateoi.gp.exceptions.NoConstructorsSet;

/**
 * Abstract class of nodes with three children.
 *
 * @author mateo
 *
 */
public abstract class Arity3Node extends Node {

    /** function to evaluate */
    private final TriFunction<Double, Double, Double, Double> function;

    /**
     * Create a new node with the given name, depth and function.
     *
     * @param name
     * @param depth
     * @param function
     */
    public Arity3Node(String name, int depth, TriFunction<Double, Double, Double, Double> function) {
        super(name, depth);
        this.function = function;
    }

    /**
     * @return This node's function
     */
    public TriFunction<Double, Double, Double, Double> getFunction() {
        return function;
    }

    @Override
    public double evaluate() {
        Node child1 = getArguments().get(0);
        double value1 = child1.evaluate();
        Node child2 = getArguments().get(1);
        double value2 = child2.evaluate();
        Node child3 = getArguments().get(2);
        double value3 = child3.evaluate();
        return function.apply(value1, value2, value3);
    }

    @Override
    public void createChildren() {
        try {
            getArguments().clear();
            Node child1 = NodeFactory.getInstance().createRandomNode(getDepth() - 1);
            getArguments().add(child1);
            Node child2 = NodeFactory.getInstance().createRandomNode(getDepth() - 1);
            getArguments().add(child2);
            Node child3 = NodeFactory.getInstance().createRandomNode(getDepth() - 1);
            getArguments().add(child3);
        } catch (NoConstructorsSet e) {
            System.out.println("No constructors set!");
            System.exit(1);
        }
    }

}
