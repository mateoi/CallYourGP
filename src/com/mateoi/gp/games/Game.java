package com.mateoi.gp.games;

import java.util.List;

import com.mateoi.gp.tree.Node;

public interface Game {

    public int playHeadToHead(Node... trees);

    public List<Double> getFitness(Node... trees);
}
