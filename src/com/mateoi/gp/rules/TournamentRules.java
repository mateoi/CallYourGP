package com.mateoi.gp.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mateoi.gp.games.Game;
import com.mateoi.gp.tree.Node;

/**
 * An implementation of tournament selection. Players are randomly matched
 * against one another, and the winners of each round advance to the next one.
 * The overall winner receives a score of, and the losers in each round with n
 * players receive a score of 1/n.
 *
 * @author mateo
 *
 */
public class TournamentRules implements Rules {

    /** Game to score individuals by */
    private final Game game;

    /**
     * Create a Rules object that ranks players according to their position in a
     * tournament
     *
     * @param game
     */
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

    /**
     * Play a single tournament round and return two lists: one of the winners
     * and one of the losers.
     * 
     * @param trees
     * @return
     */
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
