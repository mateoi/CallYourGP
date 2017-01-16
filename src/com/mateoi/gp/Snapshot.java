package com.mateoi.gp;

import com.mateoi.gp.tree.Node;

public class Snapshot {

    private final int round;
    private final double averageFitness;
    private final double bestFitness;
    private final Node bestNode;

    public Snapshot(int round, double averageFitness, double bestFitness, Node bestNode) {
        this.round = round;
        this.averageFitness = averageFitness;
        this.bestFitness = bestFitness;
        this.bestNode = bestNode;
    }

    public int getRound() {
        return round;
    }

    public double getAverageFitness() {
        return averageFitness;
    }

    public double getBestFitness() {
        return bestFitness;
    }

    public Node getBestNode() {
        return bestNode;
    }

    @Override
    public String toString() {
        return round + "," + averageFitness + "," + bestFitness + "," + bestNode;
    }

}
