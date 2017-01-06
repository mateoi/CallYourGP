package com.mateoi.gp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mateoi.gp.exceptions.NoConstructorsSet;
import com.mateoi.gp.games.Ski;
import com.mateoi.gp.games.SkiProvider;
import com.mateoi.gp.rules.ProportionalRules;
import com.mateoi.gp.rules.Rules;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.Reproductor;
import com.mateoi.ski.SkiFXApp;
import com.mateoi.ski.SkiGame;

public class Main {

    public static final int POPULATION = 1000;
    public static final int DEPTH = 6;
    public static final int GENERATIONS = 30;
    public static final double CROSSOVER_RATE = 0.05;
    public static final double MUTATION_RATE = 0.85;
    public static final double INDIVIDUAL_MUTATION_RATE = 0.02;

    private List<Node> trees = new ArrayList<>();
    private final Rules rules;

    public Main(Rules rules) {
        this.rules = rules;
        initializeNodes();
    }

    private void initializeNodes() {
        try {
            for (int i = 0; i < POPULATION; i++) {
                trees.add(NodeFactory.getInstance().createFunction(DEPTH));
            }
        } catch (NoConstructorsSet e) {
            System.exit(1);
        }
    }

    public List<Node> run(Reproductor reproductor, int nodes) {
        for (int i = 0; i < GENERATIONS; i++) {
            System.out.println("-----------------------");
            System.out.println("Generation " + i);
            System.out.println("Population: " + trees.size());
            Map<Node, Double> scores = rules.score(trees);
            trees = reproductor.nextGeneration(scores);
            trees = reproductor.increasePopulation(trees, POPULATION);
        }
        return rules.bestNodes(trees, nodes);
    }

    public static void main(String[] args) {

        // Pong game = new Pong(3);
        // Rules rules = new ProportionalRules(game);
        // Main main = new Main(rules);
        // List<Node> best = main.run(new Reproductor(CROSSOVER_RATE,
        // MUTATION_RATE, INDIVIDUAL_MUTATION_RATE), 5);
        //
        // System.out.println(best.get(0));
        // System.out.println(best.get(1));
        //
        // PongProvider.getInstance().resetGame();
        // PongGame pongGame = PongProvider.getInstance().getGame();
        // PongFXApp.setGame(pongGame);
        // PongFXApp.setPlayers(game.nodePlayer(best.get(0)),
        // game.nodePlayer(best.get(1)));
        // PongFXApp.launch(PongFXApp.class);

        Ski game = new Ski(10);
        Rules rules = new ProportionalRules(game);
        Main main = new Main(rules);
        List<Node> best = main.run(new Reproductor(CROSSOVER_RATE, MUTATION_RATE, INDIVIDUAL_MUTATION_RATE), 2);
        System.out.println(best.get(0));
        System.out.println(best.get(1));

        SkiProvider.getInstance().resetGame();
        SkiGame skiGame = SkiProvider.getInstance().getGame();
        SkiFXApp.setGame(skiGame);
        SkiFXApp.setPlayer(game.nodePlayer(best.get(0)));
        SkiFXApp.launch(SkiFXApp.class);

    }
}
