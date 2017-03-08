package com.mateoi.gp.rules;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mateoi.gp.tree.Node;

public class NoRules implements Rules {

    public NoRules() {
        // Nothing here
    }

    @Override
    public Map<Node, Double> score(List<Node> trees) {
        return trees.stream().collect(Collectors.toMap(Function.identity(), v -> 1.));
    }

    @Override
    public List<Node> bestNodes(List<Node> trees, int n) {
        return trees.subList(0, n);
    }

}
