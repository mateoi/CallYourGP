package com.mateoi.gp.tree;

import java.util.ArrayList;
import java.util.List;

import com.mateoi.gp.exceptions.NoConstructorsSet;

/**
 * Abstract class that defines a Node. A Node can be evaluated to a numerical
 * value, and this value may depend on further children Nodes. Therefore a Node
 * is a tree structure, and is the base for GP individuals. Nodes can crossover
 * and mutate.
 *
 * @author mateo
 *
 */
public abstract class Node {

    /** The name of the node, used for display purposes */
    private final String name;
    /** The maximum depth of the node */
    private int depth;
    /** Sub-trees owned by this node */
    private final List<Node> arguments;

    /**
     * Create a new node with the given name and depth and no children.
     *
     * @param name
     * @param depth
     */
    protected Node(String name, int depth) {
        this.name = name;
        this.depth = depth;
        arguments = new ArrayList<>();
    }

    /**
     * Create a new node with the given name, depth and children.
     *
     * @param name
     * @param depth
     */
    protected Node(String name, int depth, ArrayList<Node> arguments) {
        this.name = name;
        this.depth = depth;
        this.arguments = arguments;
    }

    /**
     * Crossover this node and return a new node. Crossover is done by
     * exchanging a random subtree of this node with a random subtree of the
     * other node.
     *
     * @param other
     * @return
     */
    public Node crossover(Node other) {
        Node child = this.copy();
        int oldBranchIndex = ((int) (Math.random() * child.size() - 1)) + 1;
        Node branchToAdd = other.getRandomSubNode().copy();
        child.setNode(oldBranchIndex, branchToAdd);
        child.trim(depth);
        return child;
    }

    /**
     * Create a new node that is a random mutation of this node. For each node
     * in this tree, there is the given probability that it will be exchanged
     * for another node of the same arity.
     *
     * @param probability
     * @return
     */
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

    /**
     * Create a new node that has identical arguments as this one, but the head
     * (root of the tree) is a randomly selected one.
     *
     * @return
     */
    private Node changeHead() {
        int arity = arguments.size();
        Node newNode;
        try {
            newNode = NodeFactory.getInstance().createByArity(arity);
        } catch (NoConstructorsSet e) {
            return this.copy();
        }
        newNode.setDepth(depth);
        newNode.getArguments().clear();
        arguments.forEach(n -> newNode.getArguments().add(n.copy()));
        return newNode;
    }

    /**
     * Return a random node of this tree.
     */
    public Node getRandomSubNode() {
        int index = ((int) Math.random() * size());
        return getNode(index);
    }

    /**
     * Convert this node's subtrees beyond the given maximum depth to arity-0
     * nodes, effectively discarding nodes of a depth greater than that
     * specified.
     *
     * @param maxDepth
     */
    public void trim(int maxDepth) {
        if (maxDepth <= 1) {
            int children = arguments.size();
            arguments.clear();
            for (int i = 0; i < children; i++) {
                try {
                    arguments.add(NodeFactory.getInstance().createByArity(0));
                } catch (NoConstructorsSet e) {
                    e.printStackTrace();
                }
            }
        } else {
            for (Node child : arguments) {
                child.trim(maxDepth - 1);
            }
        }
    }

    /**
     * Get the node at the specified index (counting in breadth first,
     * left-to-right order)
     *
     * @param index
     * @return
     */
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

    /**
     * Set the node at the given index (in breadth first, left-to-right order)
     */
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
                "index: " + originalIndex + ", size: " + this.size() + "\ntree: " + this + "\narguments: " + arguments);
    }

    /**
     * Calculate the total number of nodes in this tree.
     *
     * @return
     */
    public int size() {
        int size = 0;
        for (Node child : arguments) {
            size += child.size();
        }
        return 1 + size;
    }

    /**
     * Set the maximum depth of this node.
     */
    private void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * @return The maximum depth of this Node.
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Set this node's children to the given nodes.
     *
     * @param arguments
     */
    public void setArguments(List<Node> arguments) {
        this.arguments.clear();
        this.arguments.addAll(arguments);
    }

    /**
     * @return The argument list of this Node.
     */
    public List<Node> getArguments() {
        return arguments;
    }

    /**
     * @return This Node's name
     */
    public String getName() {
        return name;
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

    /**
     * Create random children for this Node.
     */
    public abstract void createChildren();

    /**
     * Calculate this Node's value.
     *
     * @return
     */
    public abstract double evaluate();

    /**
     * Create an exact copy of this node, including children.
     *
     * @return
     */
    public abstract Node copy();

}
