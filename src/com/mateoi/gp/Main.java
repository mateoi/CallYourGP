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
import com.mateoi.gp.rules.Rules;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.Reproductor;

public class Main {

    public static final int POPULATION = 1000;
    public static final int DEPTH = 6;
    public static final int GENERATIONS = 500;
    public static final double CROSSOVER_RATE = 0.25;
    public static final double MUTATION_RATE = 0.7;
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

    }

    private static void writeToFile(List<Snapshot> snapshots, int iteration) {
        String filename = "Pong_10_" + iteration + ".csv";
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
