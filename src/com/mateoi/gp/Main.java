package com.mateoi.gp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.mateoi.gp.exceptions.NoConstructorsSet;
import com.mateoi.gp.games.Pong;
import com.mateoi.gp.games.PongProvider;
import com.mateoi.gp.rules.ProportionalRules;
import com.mateoi.gp.rules.Rules;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.Reproductor;
import com.mateoi.pong.PongFXApp;
import com.mateoi.pong.PongGame;

public class Main {

    public static final int POPULATION = 1000;
    public static final int DEPTH = 8;
    public static final int GENERATIONS = 500;
    public static final double CROSSOVER_RATE = 0.1;
    public static final double MUTATION_RATE = 0.85;
    public static final double INDIVIDUAL_MUTATION_RATE = 0.03;

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

        // Game game = new Nim1D(20);
        // Rules rules = new TournamentRules(CROSSOVER_RATE, MUTATION_RATE,
        // game);
        // Main main = new Main(rules);
        // Node winner = main.run();
        // System.out.println(winner);

        // Player l = new HumanPlayer(KeyCode.W, KeyCode.S);
        // Player r = new HumanPlayer(KeyCode.UP, KeyCode.DOWN);

        Pong game = new Pong(3);
        Rules rules = new ProportionalRules(game);
        Main main = new Main(rules);
        List<Node> best = main.run(new Reproductor(CROSSOVER_RATE, MUTATION_RATE, INDIVIDUAL_MUTATION_RATE), 5);

        System.out.println(best.get(0));
        System.out.println(best.get(1));

        PongProvider.getInstance().resetGame();
        PongGame pongGame = PongProvider.getInstance().getGame();
        PongFXApp.setGame(pongGame);
        PongFXApp.setPlayers(game.nodePlayer(best.get(0)), game.nodePlayer(best.get(1)));
        PongFXApp.launch(PongFXApp.class);

    }
}
