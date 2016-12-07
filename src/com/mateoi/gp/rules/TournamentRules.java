package com.mateoi.gp.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mateoi.gp.games.Game;
import com.mateoi.gp.tree.Node;

public class TournamentRules implements Rules {

    private final Game game;

    public TournamentRules(Game game) {
        this.game = game;
    }

    @Override
    public Map<Node, Double> score(List<Node> trees) {
        Collections.shuffle(trees);
        Map<Node, Double> scores = new HashMap<>();
        List<Node> players = trees;
        while (players.size() > 0) {
            List<List<Node>> round = playRound(players);
            List<Node> winners = round.get(0);
            List<Node> losers = round.get(1);
            players = winners;
            losers.forEach(t -> scores.put(t, 1. / losers.size()));
        }
        return scores;
    }

    private List<List<Node>> playRound(List<Node> trees) {
        if (trees.size() <= 1) {
            return Arrays.asList(new ArrayList<>(), trees);
        }
        List<Node> winners = new ArrayList<>();
        List<Node> losers = new ArrayList<>();
        for (int i = 0; i < trees.size() / 2; i += 2) {
            Node t1 = trees.get(i);
            Node t2 = trees.get(i + 1);
            int winner = game.playHeadToHead(t1, t2);
            winners.add(winner == 0 ? t1 : t2);
            losers.add(winner == 0 ? t2 : t1);
        }
        return Arrays.asList(winners, losers);
    }

    @Override
    public List<Node> bestNodes(List<Node> trees, int n) {
        if (n <= 0) {
            return new ArrayList<>();
        }
        List<Node> players = trees;
        while (players.size() > n) {
            List<List<Node>> round = playRound(players);
            List<Node> winners = round.get(0);
            players = winners;
        }
        return players;
    }
}
