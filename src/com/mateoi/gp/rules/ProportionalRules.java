package com.mateoi.gp.rules;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mateoi.gp.games.Game;
import com.mateoi.gp.tree.Node;

public class ProportionalRules implements Rules {
    private Game game;

    public ProportionalRules(Game game) {
        this.game = game;
    }

    @Override
    public Map<Node, Double> score(List<Node> trees) {
        Collections.shuffle(trees);
        Map<Node, Double> scores = new HashMap<>();
        for (int i = 0; i < trees.size() / 2; i++) {
            Node left = trees.get(i);
            Node right = trees.get(trees.size() - i - 1);
            List<Double> grades = game.getFitness(left, right);
            scores.put(left, grades.get(0));
            scores.put(right, grades.get(1));
        }
        return scores;
    }

    @Override
    public List<Node> bestNodes(List<Node> trees, int n) {
        Map<Node, Double> scores = score(trees);
        Collections.sort(trees, (t1, t2) -> Double.compare(scores.get(t1), scores.get(t2)));
        return trees.subList(0, n);
    }

}
