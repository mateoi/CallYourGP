package com.mateoi.gp;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mateoi.gp.exceptions.NoConstructorsSet;
import com.mateoi.gp.games.Pong;
import com.mateoi.gp.games.PongProvider;
import com.mateoi.gp.rules.Rules;
import com.mateoi.gp.rules.TournamentRules;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.Parser;
import com.mateoi.gp.tree.PongParser;
import com.mateoi.gp.tree.Reproductor;
import com.mateoi.pong.Player;
import com.mateoi.pong.PongGame;

public class Main {

    public static final int POPULATION = 1000;
    public static final int DEPTH = 6;
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

        for (int i = 0; i < 100; i++) {
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
        PongResult r1 = new PongResult(p1Wins / 100., p1Rallies / 100.);
        PongResult r2 = new PongResult(1 - (p1Wins / 100.), p2Rallies / 100.);
        results.add(r1);
        results.add(r2);
        return results;
    }

    public static void main(String[] args) {
        Pong game = new Pong(10);

        Main main = new Main(new TournamentRules(game));
        String[] types = { "10", "11", "01" };
        for (String type : types) {
            List<List<Node>> allNodes = main.readFiles(type);
            List<Node> bestNodes = main.bestByGeneration(allNodes);
            try {
                Files.write(Paths.get("best_" + type + ".txt"),
                        bestNodes.stream().map(n -> n.toString()).collect(Collectors.toList()));
            } catch (IOException e) {
                System.out.println("oh no!");
            }

        }
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

    private static void writeToFile(List<PongResult[][]> results) {
        String filename = "Pong_Results.txt";
        String comment = "# 1: GP, 2: GPAI, 3: GPM, 4: GPAIM, 5: AI, 6: BL";
        StringBuilder sb = new StringBuilder(comment);
        sb.append("\n");
        for (PongResult[][] arr : results) {
            sb.append('[');
            for (PongResult[] line : arr) {
                sb.append('[');
                Arrays.stream(line).forEach(r -> sb.append(r + ","));
                sb.append("]");
            }
            sb.append("]\n");
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
