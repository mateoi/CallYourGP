package com.mateoi.gp.tree;

import java.util.ArrayList;
import java.util.List;

import com.mateoi.gp.exceptions.NoConstructorsSet;

public abstract class Node {

    private final String name;
    private final int depth;
    private final List<Node> arguments;

    protected Node(String name, int depth) {
        this.name = name;
        this.depth = depth;
        arguments = new ArrayList<>();
    }

    protected Node(String name, int depth, ArrayList<Node> arguments) {
        this.name = name;
        this.depth = depth;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        String s = "(" + name;
        for (final Node n : arguments) {
            s += " " + n.toString();
        }
        s += ")";
        return s;
    }

    public void setArguments(List<Node> arguments) {
        this.arguments.clear();
        this.arguments.addAll(arguments);
    }

    public List<Node> getArguments() {
        return arguments;
    }

    public String getName() {
        return name;
    }

    public int getDepth() {
        return depth;
    }

    public Node crossover(Node other) {
        final Node child = this.copy();
        final int oldBranchIndex = ((int) (Math.random() * child.size() - 1)) + 1;
        final Node branchToAdd = other.getRandomSubNode().copy();
        child.setNode(oldBranchIndex, branchToAdd);
        child.trim(depth);
        return child;
    }

    public Node mutate() {
        try {
            final Node random = NodeFactory.getInstance().createRandomNode(depth);
            return this.crossover(random);
        } catch (final NoConstructorsSet e) {
            return this.copy();
        }
    }

    public Node getRandomSubNode() {
        final int index = ((int) Math.random() * size());
        return getNode(index);
    }

    public void trim(int maxDepth) {
        if (maxDepth <= 1) {
            final int children = arguments.size();
            arguments.clear();
            for (int i = 0; i < children; i++) {
                arguments.add(NodeFactory.getInstance().createTerminal());
            }
        } else {
            for (final Node child : arguments) {
                child.trim(maxDepth - 1);
            }
        }
    }

    public Node getNode(int index) {
        if (index == 0) {
            return this;
        } else {
            for (final Node child : arguments) {
                final int childSize = child.size();
                if (index <= childSize) {
                    return child.getNode(index - 1);
                } else {
                    index -= childSize;
                }
            }
        }
        throw new IndexOutOfBoundsException();
    }

    public void setNode(int index, Node node) {
        if (index <= 0) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = 0; i < arguments.size(); i++) {
            if (index == 1) {
                arguments.set(i, node);
                return;
            } else {
                final int childSize = arguments.get(i).size();
                if (index <= childSize) {
                    arguments.get(i).setNode(index - 1, node);
                    return;
                } else {
                    index -= childSize;
                }
            }
        }
        throw new IndexOutOfBoundsException();
    }

    public int size() {
        int size = 0;
        for (final Node child : arguments) {
            size += child.size();
        }
        return 1 + size;
    }

    public void createChildren() {
        try {
            final Node child = NodeFactory.getInstance().createRandomNode(depth - 1);
            arguments.add(child);
        } catch (final NoConstructorsSet e) {
            System.exit(1);
        }
    }

    public abstract int evaluate();

    public abstract Node copy();

}
