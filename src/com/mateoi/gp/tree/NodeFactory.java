package com.mateoi.gp.tree;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mateoi.gp.exceptions.NoConstructorsSet;

public class NodeFactory {

    private static final NodeFactory instance = new NodeFactory();
    private List<Function<Integer, Node>> functions;
    private List<Supplier<Node>> terminals;

    public Node createRandomNode(int depth) throws NoConstructorsSet {
        if (functions == null || terminals == null) {
            throw new NoConstructorsSet();
        }
        if (depth <= 0) {
            return createTerminal();
        } else {
            return createAnyNode(depth);
        }
    }

    public Node createFunction(int depth) throws NoConstructorsSet {
        if (functions == null || terminals == null) {
            throw new NoConstructorsSet();
        }

        if (depth <= 0) {
            return createTerminal();
        } else {
            int index = randInt(functions.size());
            Function<Integer, Node> c = functions.get(index);
            return c.apply(depth);
        }

    }

    public Node createTerminal() {
        int index = randInt(terminals.size());
        return terminals.get(index).get();
    }

    private Node createAnyNode(int depth) {
        int index = randInt(functions.size() + terminals.size());
        if (index < functions.size()) {
            Function<Integer, Node> c = functions.get(index);
            return c.apply(depth);
        } else {
            Supplier<Node> c = terminals.get(index - functions.size());
            return c.get();
        }
    }

    public void setConstructors(List<Function<Integer, Node>> functions, List<Supplier<Node>> terminals) {
        this.terminals = terminals;
        this.functions = functions;
    }

    private int randInt(int max) {
        double rand = Math.random();
        return (int) (rand * max);
    }

    private NodeFactory() {
        // private to prevent instantiation
    }

    public static NodeFactory getInstance() {
        return instance;
    }
}
