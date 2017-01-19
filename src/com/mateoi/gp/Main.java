package com.mateoi.gp;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mateoi.gp.exceptions.NoConstructorsSet;
import com.mateoi.gp.games.Ski;
import com.mateoi.gp.rules.ProportionalRules;
import com.mateoi.gp.rules.Rules;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.Reproductor;

public class Main {

    public static final int POPULATION = 1000;
    public static final int DEPTH = 6;
    public static final int GENERATIONS = 500;
    public static final double CROSSOVER_RATE = 0.05;
    public static final double MUTATION_RATE = 0.9;
    public static final double INDIVIDUAL_MUTATION_RATE = 0.015;

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

    public List<Snapshot> runWithSnapshots(Reproductor reproductor) {
        List<Snapshot> snapshots = new ArrayList<>();
        for (int i = 0; i < GENERATIONS; i++) {
            Map<Node, Double> scores = rules.score(trees);
            trees = reproductor.nextGeneration(scores);
            trees = reproductor.increasePopulation(trees, POPULATION);
            snapshots.add(takeSnapshot(scores, i));

        }
        return snapshots;
    }

    private Snapshot takeSnapshot(Map<Node, Double> scores, int round) {
        double totalFitness = 0;
        double bestFitness = Double.MIN_VALUE;
        Node bestNode = null;
        for (Entry<Node, Double> score : scores.entrySet()) {
            double fitness = score.getValue();
            totalFitness += fitness;
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestNode = score.getKey();
            }
        }
        double averageFitness = totalFitness / scores.size();
        return new Snapshot(round, averageFitness, bestFitness, bestNode);
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

        Ski game = new Ski(8);
        Rules rules = new ProportionalRules(game);
        for (int i = 0; i < 10; i++) {
            Main main = new Main(rules);
            System.out.println(i);
            List<Snapshot> snapshots = main
                    .runWithSnapshots(new Reproductor(CROSSOVER_RATE, MUTATION_RATE, INDIVIDUAL_MUTATION_RATE));
            for (Snapshot s : snapshots) {
                System.out.println(s);
            }
            writeToFile(snapshots, i);
        }
        // Main main = new Main(rules);
        // List<Node> nodes = main.run(new Reproductor(CROSSOVER_RATE,
        // MUTATION_RATE, INDIVIDUAL_MUTATION_RATE), 2);
        // System.out.println(nodes.get(0));
        // SkiProvider.getInstance().resetGame();
        // SkiGame skiGame = SkiProvider.getInstance().getGame();
        // SkiFXApp.setGame(skiGame);
        // SkiFXApp.setPlayer(game.nodePlayer(nodes.get(0)));
        // SkiFXApp.launch(SkiFXApp.class);
    }

    private static void writeToFile(List<Snapshot> snapshots, int iteration) {
        String filename = "Ski_11_" + iteration + ".csv";
        String stats = "# " + GENERATIONS + ";" + POPULATION + ";" + DEPTH + ";" + CROSSOVER_RATE + ";" + MUTATION_RATE
                + ";" + INDIVIDUAL_MUTATION_RATE;
        StringBuilder sb = new StringBuilder(stats);
        sb.append("\n");
        for (Snapshot s : snapshots) {
            sb.append(s);
            sb.append("\n");
        }

        try {
            Path path = Paths.get(filename);
            BufferedWriter writer = Files.newBufferedWriter(path);
            writer.write(sb.toString());
            writer.flush();
        } catch (Exception e) {
            System.out.println("Could not write to file " + filename);
        }
    }
}
