package com.mateoi.gp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.mateoi.gp.exceptions.NoConstructorsSet;
import com.mateoi.gp.games.PongProvider;
import com.mateoi.gp.games.Ski;
import com.mateoi.gp.rules.ProportionalRules;
import com.mateoi.gp.rules.Rules;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.Parser;
import com.mateoi.gp.tree.PongParser;
import com.mateoi.gp.tree.Reproductor;
import com.mateoi.pong.Player;
import com.mateoi.pong.PongGame;

public class Main {

    public static final int POPULATION = 1000;
    public static final int DEPTH = 5;
    public static final int GENERATIONS = 500;
    public static final double CROSSOVER_RATE = 0.25;
    public static final double MUTATION_RATE = 0.7;
    public static final double INDIVIDUAL_MUTATION_RATE = 0.03;

    public static final Parser parser = new PongParser();

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
            System.err.println("No Node constructors set yet!");
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

    public List<PongResult> play(Player p1, Player p2) {
        List<PongResult> results = new ArrayList<>();
        int p1Wins = 0;
        int p1Rallies = 0;
        int p2Rallies = 0;
        double rounds = 75;

        for (int i = 0; i < rounds; i++) {
            PongProvider.getInstance().resetGame();
            PongGame game = PongProvider.getInstance().getGame();
            while (game.getLeftScore() < 10 && game.getRightScore() < 10) {
                PongProvider.getInstance().setLeftTurn(true);
                int leftMove = p1.move(game);
                PongProvider.getInstance().setLeftTurn(false);
                int rightMove = p2.move(game);
                game.nextFrame(leftMove, rightMove);
            }
            p1Wins += game.getLeftScore() > game.getRightScore() ? 1 : 0;
            p1Rallies += game.getLeftHits();
            p2Rallies += game.getRightHits();
        }
        PongResult r1 = new PongResult(p1Wins / rounds, p1Rallies / rounds);
        PongResult r2 = new PongResult(1 - (p1Wins / rounds), p2Rallies / rounds);
        results.add(r1);
        results.add(r2);
        return results;
    }

    private PongResult[][] playMany(Player... players) {
        PongResult[][] results = new PongResult[players.length][players.length];

        for (int i = 0; i < results.length; i++) {
            for (int j = i; j < results.length; j++) {
                List<PongResult> match = play(players[i], players[j]);
                if (i == j) {
                    double averageRally = (match.get(0).getAverageRally() + match.get(1).getAverageRally()) / 2;
                    PongResult head2Head = new PongResult(0.5, averageRally);
                    results[i][j] = head2Head;
                } else {
                    results[i][j] = match.get(0);
                    results[j][i] = match.get(1);
                }
            }
        }
        return results;
    }

    public static void main(String[] args) {
        Ski game = new Ski(10);
        Rules rules = new ProportionalRules(game);
        for (int i = 8; i < 10; i++) {
            Main main = new Main(rules);
            System.out.println(i);
            List<Snapshot> snapshots = main
                    .runWithSnapshots(new Reproductor(CROSSOVER_RATE, MUTATION_RATE, INDIVIDUAL_MUTATION_RATE));
            for (Snapshot s : snapshots) {
                System.out.println(s);
            }
            writeToFile(snapshots, i);
        }
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

    private List<Node> readBestFile(String code) {
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(Paths.get("data/pong/best_" + code + ".txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines.stream().map(s -> parser.parse(s, DEPTH)).collect(Collectors.toList());
    }

    private List<Node> bestByGeneration(List<List<Node>> allNodes) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < allNodes.get(0).size(); i++) {
            System.out.println(i);
            List<Node> generation = new ArrayList<>();
            for (int j = 0; j < allNodes.size(); j++) {
                generation.add(allNodes.get(j).get(i));
            }
            nodes.add(bestOfList(generation));
        }
        return nodes;
    }

    private Node bestOfList(List<Node> nodes) {
        return rules.bestNodes(nodes, 1).get(0);
    }

    public List<List<Node>> readFiles(String type) {
        List<List<Node>> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            results.add(readFile(type, i));
        }
        return results;
    }

    private Node nodeFromLine(String line) {
        String nodeAsString = line.split(",")[3].trim();
        return parser.parse(nodeAsString, DEPTH);
    }

    private List<Node> readFile(String type, int i) {
        String fname = "data/pong/Pong_" + type + "_" + i + ".csv";
        try {
            List<String> lines = Files.readAllLines(Paths.get(fname));
            return lines.stream().filter(s -> !s.startsWith("#")).map(s -> nodeFromLine(s))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private static void writeToFile(List<Snapshot> snapshots, int iteration) {
        String filename = "Ski_ct_01_" + iteration + ".csv";
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
