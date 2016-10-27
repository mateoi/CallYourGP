package com.mateoi.gp.tree;

public class Reproductor {

    private final double crossoverRate;
    private final double mutationRate;

    public Reproductor(double crossoverRate, double mutationRate) {
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
    }

    public Node reproduce(Node dad, Node mom) {
        final double random = Math.random();
        if (random < crossoverRate) {
            final Node child = dad.crossover(mom);
            return child;
        }
        final Node successor = Math.random() < 0.5 ? dad : mom;
        Node child;
        if (random < crossoverRate + mutationRate) {
            child = successor.mutate();
        } else {
            child = successor;
        }
        return child;
    }

}
