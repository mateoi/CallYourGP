package com.mateoi.gp.rules;

import java.util.List;

import com.mateoi.gp.tree.Node;

public interface Rules<T> {
    public List<Node<T>> nextGeneration(List<Node<T>> trees);

    public Node<T> bestNode(List<Node<T>> trees);
}
