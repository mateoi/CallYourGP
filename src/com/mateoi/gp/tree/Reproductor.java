package com.mateoi.gp.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Reproductor {

    private final double crossoverRate;
    private final double mutationRate;
    private final double individualMutationRate;

    public Reproductor(double crossoverRate, double mutationRate, double individualMutationRate) {
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.individualMutationRate = individualMutationRate;
    }

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

    public List<Node> increasePopulation(List<Node> trees, int target) {
        List<Node> list = new ArrayList<>(trees);
        for (int i = 0; i < target - trees.size(); i++) {
            Node dad = trees.get((int) (Math.random() * trees.size()));
            Node mom = trees.get((int) (Math.random() * trees.size()));
            list.add(reproduce(dad, mom));
        }
        return list;
    }

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

    private List<Node> reproduce(List<Node> parents) {
        List<Node> children = new ArrayList<>(parents.size());
        for (Node dad : parents) {
            Node mom = parents.get((int) (Math.random() * parents.size()));
            children.add(reproduce(dad, mom));
        }
        return children;
    }

}
