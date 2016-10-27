package com.mateoi.gp.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.Reproductor;

public abstract class TournamentRules implements Rules {

    private final Reproductor reproductor;

    public TournamentRules(double crossoverRate, double mutationRate, int depth) {
        reproductor = new Reproductor(crossoverRate, mutationRate);
    }

    @Override
    public List<Node> nextGeneration(List<Node> trees) {
        final List<Node> males = tournamentRound(trees);
        final List<Node> females = tournamentRound(trees);
        final List<Node> nextGeneration = new ArrayList<>();
        for (int i = 0; i < males.size(); i++) {
            final Node dad = males.get(i);
            final Node mom1 = females.get(i);
            final Node mom2 = females.get(females.size() - 1 - i);
            nextGeneration.add(reproductor.reproduce(dad, mom1));
            nextGeneration.add(reproductor.reproduce(dad, mom2));
        }
        return nextGeneration;
    }

    @Override
    public Node bestNode(List<Node> trees) {
        while (trees.size() > 1) {
            trees = tournamentRound(trees);
        }
        return trees.get(0);
    }

    private List<Node> tournamentRound(List<Node> trees) {
        Collections.shuffle(trees);
        final List<Node> winners = new ArrayList<>();
        for (int i = 0; i < trees.size() / 2; i++) {
            final Node t1 = trees.get(i);
            final Node t2 = trees.get(trees.size() - i - 1);
            final int winner = compareTrees(t1, t2);
            winners.add(winner == 1 ? t2 : t1);
        }
        return winners;
    }

    protected abstract int compareTrees(Node t1, Node t2);
}
