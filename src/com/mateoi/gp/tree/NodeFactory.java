package com.mateoi.gp.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mateoi.gp.exceptions.NoConstructorsSet;

public class NodeFactory {

    private static final NodeFactory instance = new NodeFactory();
    private List<Function<Integer, Node>> functions;
    private List<Function<Integer, Node>> arity1;
    private List<Function<Integer, Node>> arity2;
    private List<Function<Integer, Node>> arity3;
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

    public Node createByArity(int arity) {
        List<Function<Integer, Node>> constructors;
        switch (arity) {
        case 0:
            if (Math.random() < terminals.size() / (arity1.size() + terminals.size())) {
                return createTerminal();
            } else {
                int index = randInt(arity1.size());
                return arity1.get(index).apply(1);
            }
        case 1:
            constructors = arity1;
            break;
        case 2:
            constructors = arity2;
            break;
        default:
            constructors = arity3;
        }
        int index = randInt(constructors.size());
        return constructors.get(index).apply(0);
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

    public void setConstructors(List<Function<Integer, Node>> arity1, List<Function<Integer, Node>> arity2,
            List<Function<Integer, Node>> arity3, List<Supplier<Node>> terminals) {
        this.terminals = terminals;
        this.arity1 = arity1;
        this.arity2 = arity2;
        this.arity3 = arity3;
        functions = new ArrayList<>(arity1);
        functions.addAll(arity2);
        functions.addAll(arity3);
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
