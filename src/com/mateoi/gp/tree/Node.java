package com.mateoi.gp.tree;

import java.util.ArrayList;
import java.util.List;

public abstract class Node {

    private final String name;
    private int depth;
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
        for (Node n : arguments) {
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
        Node child = this.copy();
        int oldBranchIndex = ((int) (Math.random() * child.size() - 1)) + 1;
        Node branchToAdd = other.getRandomSubNode().copy();
        child.setNode(oldBranchIndex, branchToAdd);
        child.trim(depth);
        return child;
    }

    public Node mutate(double probability) {
        Node child = null;
        for (int i = 0; i < size(); i++) {
            if (Math.random() < probability) {
                Node toChange = getNode(i);
                Node newNode = toChange.changeHead();
                if (i == 0) {
                    child = newNode;
                } else {
                    child = this.copy();
                    child.setNode(i, newNode);
                }
                break;
            }
        }
        return child == null ? this.copy() : child;
    }

    private Node changeHead() {
        int arity = arguments.size();
        Node newNode = NodeFactory.getInstance().createByArity(arity);
        newNode.setDepth(depth);
        newNode.getArguments().clear();
        arguments.forEach(n -> newNode.getArguments().add(n.copy()));
        return newNode;
    }

    public Node getRandomSubNode() {
        int index = ((int) Math.random() * size());
        return getNode(index);
    }

    public void trim(int maxDepth) {
        if (maxDepth <= 1) {
            int children = arguments.size();
            arguments.clear();
            for (int i = 0; i < children; i++) {
                arguments.add(NodeFactory.getInstance().createTerminal());
            }
        } else {
            for (Node child : arguments) {
                child.trim(maxDepth - 1);
            }
        }
    }

    public Node getNode(int index) {
        if (index == 0) {
            return this;
        } else {
            for (Node child : arguments) {
                int childSize = child.size();
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
        int originalIndex = index;
        if (index <= 0) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = 0; i < arguments.size(); i++) {
            if (index == 1) {
                arguments.set(i, node);
                return;
            } else {
                int childSize = arguments.get(i).size();
                if (index <= childSize) {
                    arguments.get(i).setNode(index - 1, node);
                    return;
                } else {
                    index -= childSize;
                }
            }
        }
        throw new IndexOutOfBoundsException(
                "index: " + originalIndex + ", size: " + this.size() + "\ntree: " + this + "\nargunents: " + arguments);
    }

    public int size() {
        int size = 0;
        for (Node child : arguments) {
            size += child.size();
        }
        return 1 + size;
    }

    private void setDepth(int depth) {
        this.depth = depth;
    }

    public abstract void createChildren();

    public abstract double evaluate();

    public abstract Node copy();

}
