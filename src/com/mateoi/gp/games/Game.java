package com.mateoi.gp.games;

import java.util.List;

import com.mateoi.gp.tree.Node;

/**
 * A Game is a problem that GP individuals are trying to solve. A Game
 * implements some fitness function and initializes NodeFactory's constructors
 * (determines whicj nnodes are available)
 *
 * @author mateo
 *
 */
public interface Game {

    /**
     * Given many trees, determine which one has the highest fitness function
     * 
     * @param trees
     * @return
     */
    public int playHeadToHead(Node... trees);

    /**
     * Given many trees, return a list of their fitness values
     * 
     * @param trees
     * @return
     */
    public List<Double> getFitness(Node... trees);
}
