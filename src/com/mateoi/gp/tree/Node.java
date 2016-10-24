package com.mateoi.gp.tree;

import java.util.ArrayList;

import com.mateoi.gp.exceptions.TypeMismatch;

@SuppressWarnings("rawtypes")
public abstract class Node<T> {

    private final Class<T> type;
    private final String name;
    protected final int depth;
    protected final ArrayList<Node> arguments;
    protected final ArrayList<Class> argumentTypes;

    protected Node(String name, int depth, Class<T> type, Class... types) {
        this.type = type;
        this.name = name;
        this.depth = depth;
        this.arguments = new ArrayList<>();
        this.argumentTypes = new ArrayList<>();
        for (final Class c : types) {
            this.argumentTypes.add(c);
        }
        createChildren();
    }

    @Override
    public String toString() {
        String s = "(" + this.name;
        for (final Node n : this.arguments) {
            s += " " + n.toString();
        }
        s += ")";
        return s;
    }

    public Class<T> getType() {
        return this.type;
    }

    protected boolean checkTypes() {
        if (this.arguments.size() != this.argumentTypes.size()) {
            return false;
        }
        for (int i = 0; i < this.arguments.size(); i++) {
            if (!this.arguments.get(i).getType().equals(this.argumentTypes.get(i))) {
                return false;
            }
        }
        return true;
    }

    protected void createChildren() {
        for (final Class type : this.argumentTypes) {
            final Node child = NodeFactory.getInstance().createRandomNode(type, this.depth - 1);
            this.arguments.add(child);
        }
    }

    public abstract T evaluate() throws TypeMismatch;
}
