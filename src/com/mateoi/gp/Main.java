package com.mateoi.gp;

import java.util.ArrayList;
import java.util.List;

import com.mateoi.gp.exceptions.NoConstructorsSet;
import com.mateoi.gp.rules.NoRules;
import com.mateoi.gp.rules.Rules;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;

public class Main {

    public static final int POPULATION = 5000;
    public static final int DEPTH = 3;
    public static final int GENERATIONS = 100;
    public static final double CROSSOVER_RATE = 0.9;
    public static final double MUTATION_RATE = 0.015;

    private List<Node> trees = new ArrayList<>();
    private final Rules rules;

    public Main(Rules rules) {
        this.rules = rules;
        initializeNodes();
    }

    private void initializeNodes() {
        try {
            for (int i = 0; i < POPULATION; i++) {
                trees.add(NodeFactory.getInstance().createRandomNode(DEPTH));
            }
        } catch (final NoConstructorsSet e) {
            System.exit(1);
        }
    }

    public Node run() {
        for (int i = 0; i < GENERATIONS; i++) {
            trees = rules.nextGeneration(trees);
        }
        return rules.bestNode(trees);
    }

    public static void main(String[] args) {
        final Rules rules = new NoRules();
        final Main main = new Main(rules);
        final Node winner = main.run();
        System.out.println(winner);
    }
}
