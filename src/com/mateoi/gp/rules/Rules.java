package com.mateoi.gp.rules;

import java.util.List;
import java.util.Map;

import com.mateoi.gp.tree.Node;

public interface Rules {
    public Map<Node, Double> score(List<Node> trees);

    public List<Node> bestNodes(List<Node> trees, int n);
}
