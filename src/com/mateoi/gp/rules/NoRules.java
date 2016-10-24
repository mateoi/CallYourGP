package com.mateoi.gp.rules;

import java.util.List;

import com.mateoi.gp.tree.Node;

public class NoRules<T> implements Rules<T> {

    public NoRules() {
        // Do Nothing
    }

    @Override
    public List<Node<T>> nextGeneration(List<Node<T>> trees) {
        return trees;
    }

    @Override
    public Node<T> bestNode(List<Node<T>> trees) {
        return trees.get(0);
    }

}
