package com.mateoi.gp.games;

import com.mateoi.gp.tree.Node;

public interface Game {

    public int playHeadToHead(Node... trees);

    public double[] getFitness(Node... trees);
}
