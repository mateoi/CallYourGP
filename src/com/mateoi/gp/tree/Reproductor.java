package com.mateoi.gp.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class that handles reproduction of a population into a new generation
 *
 * @author mateo
 *
 */
public class Reproductor {

    /** The probability that two given nodes will reproduce sexually */
    private final double crossoverRate;
    /** The probability that two given nodes will reproduce asexually */
    private final double mutationRate;
    /** The probability for each node in an individual to mutate */
    private final double individualMutationRate;

    /**
     * Create a new Reproductor with the given probabilities
     *
     * @param crossoverRate
     * @param mutationRate
     * @param individualMutationRate
     */
    public Reproductor(double crossoverRate, double mutationRate, double individualMutationRate) {
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.individualMutationRate = individualMutationRate;
    }

    /**
     * Pair two nodes and produce some offspring. Depending on the set
     * probabilities, the resulting Node may come from mutation or crossover.
     *
     * @param dad
     * @param mom
     * @return
     */
    public Node reproduce(Node dad, Node mom) {
        double random = Math.random();
        if (random < crossoverRate) {
            Node child = dad.crossover(mom);
            return child;
        }
        Node child;
        if (random < crossoverRate + mutationRate) {
            child = dad.mutate(individualMutationRate);
        } else {
            child = dad.copy();
        }
        return child;
    }

    /**
     * Reproduce random individuals from the given list of trees until the
     * target number is reached.
     *
     * @param trees
     * @param target
     * @return
     */
    public List<Node> increasePopulation(List<Node> trees, int target) {
        List<Node> list = new ArrayList<>(trees);
        for (int i = 0; i < target - trees.size(); i++) {
            Node dad = trees.get((int) (Math.random() * trees.size()));
            Node mom = trees.get((int) (Math.random() * trees.size()));
            list.add(reproduce(dad, mom));
        }
        return list;
    }

    /**
     * Given a table of scores, select individuals according to their weighted
     * probability and reproduce them.
     *
     * @param scores
     * @return
     */
    public List<Node> nextGeneration(Map<Node, Double> scores) {
        double sum = 0;
        double a = 0;
        for (Entry<Node, Double> e : scores.entrySet()) {
            double rand = Math.random();
            a += e.getValue();
            double newValue = rand * e.getValue();
            sum += newValue;
            scores.put(e.getKey(), newValue);
        }
        System.out.println(a);
        double factor = scores.size() / sum;
        List<Node> parents = new ArrayList<>(scores.size());
        scores.forEach((n, v) -> {
            if (Math.random() < v * factor) {
                parents.add(n);
            }
        });
        return reproduce(parents);
    }

    /**
     * Reproduce the given list of trees by pairing each member with another
     * random member.
     * 
     * @param parents
     * @return
     */
    private List<Node> reproduce(List<Node> parents) {
        List<Node> children = new ArrayList<>(parents.size());
        for (Node dad : parents) {
            Node mom = parents.get((int) (Math.random() * parents.size()));
            children.add(reproduce(dad, mom));
        }
        return children;
    }

}
