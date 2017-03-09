package com.mateoi.gp;

import com.mateoi.gp.tree.Node;

/**
 * A class that records the state of an evolutionary run
 *
 * @author mateo
 *
 */
public class Snapshot {

    /** Current round of evolution */
    private final int round;
    /** Average fitness value at this point */
    private final double averageFitness;
    /** Fitness value of the best individual */
    private final double bestFitness;
    /** Best individual this generation */
    private final Node bestNode;

    /**
     * Create a snapshot of the current evolutionary state
     *
     * @param round
     * @param averageFitness
     * @param bestFitness
     * @param bestNode
     */
    public Snapshot(int round, double averageFitness, double bestFitness, Node bestNode) {
        this.round = round;
        this.averageFitness = averageFitness;
        this.bestFitness = bestFitness;
        this.bestNode = bestNode;
    }

    /**
     * @return The generation this snapshot was taken
     */
    public int getRound() {
        return round;
    }

    /**
     * @return The average fitness at this generation
     */
    public double getAverageFitness() {
        return averageFitness;
    }

    /**
     * @return The maximum fitness at this generation
     */
    public double getBestFitness() {
        return bestFitness;
    }

    /**
     * @return The best individual of this generation
     */
    public Node getBestNode() {
        return bestNode;
    }

    @Override
    public String toString() {
        return round + "," + averageFitness + "," + bestFitness + "," + bestNode;
    }

}
