package com.mateoi.gp.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mateoi.gp.games.Game;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.Reproductor;

public class TournamentRules implements Rules {

    private final Reproductor reproductor;
    private final Game game;

    public TournamentRules(double crossoverRate, double mutationRate, Game game) {
        reproductor = new Reproductor(crossoverRate, mutationRate);
        this.game = game;
    }

    @Override
    public List<Node> nextGeneration(List<Node> trees) {
        List<Node> males = tournamentRound(trees);
        List<Node> females = tournamentRound(trees);
        List<Node> nextGeneration = new ArrayList<>();
        for (int i = 0; i < males.size(); i++) {
            Node dad = males.get(i);
            Node mom1 = females.get(i);
            Node mom2 = females.get(females.size() - 1 - i);
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
        List<Node> winners = new ArrayList<>();
        for (int i = 0; i < trees.size() / 2; i++) {
            Node t1 = trees.get(i);
            Node t2 = trees.get(trees.size() - i - 1);
            int winner = compareTrees(t1, t2);
            winners.add(winner == 1 ? t2 : t1);
        }
        return winners;
    }

    protected int compareTrees(Node t1, Node t2) {
        game.reset();
        int toMove = 0;
        while (!game.isFinished()) {
            Node player = toMove == 0 ? t1 : t2;
            game.move(toMove, player.evaluate());
            toMove ^= 1;
        }
        return game.getWinner();
    }
}
