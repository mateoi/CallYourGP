package com.mateoi.gp.rules;

import java.util.List;

import com.mateoi.gp.tree.Node;

public interface Rules {
    public List<Node> nextGeneration(List<Node> trees);

    public Node bestNode(List<Node> trees);
}
