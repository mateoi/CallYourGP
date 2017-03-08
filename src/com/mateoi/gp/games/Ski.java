package com.mateoi.gp.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mateoi.gp.memory.Memory;
import com.mateoi.gp.tree.Arity0Node;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.functions.ArithmeticNode;
import com.mateoi.gp.tree.functions.Constant;
import com.mateoi.gp.tree.functions.Negate;
import com.mateoi.gp.tree.functions.ReadMemory;
import com.mateoi.gp.tree.functions.WriteMemory;
import com.mateoi.ski.AiPlayer;
import com.mateoi.ski.Player;
import com.mateoi.ski.Position;
import com.mateoi.ski.SkiGame;

public class Ski implements Game {
    private final int rounds;
    private int winningScore = 1000;

    public Ski(int rounds) {
        Memory.setMemorySupplier(SkiProvider.getInstance());
        this.rounds = rounds;

        List<Function<Integer, Node>> arity1 = new ArrayList<>();
        List<Function<Integer, Node>> arity2 = new ArrayList<>();
        List<Function<Integer, Node>> arity3 = new ArrayList<>();
        List<Function<Integer, Node>> terminals = new ArrayList<>();

        terminals.add(v -> new Constant(-1));
        terminals.add(v -> new Constant(0));
        terminals.add(v -> new Constant(1));

        for (int i = 0; i < 10; i++) {
            terminals.add(v -> new Constant(Math.random() * 6 - 3));
        }

        terminals.add(v -> new SelfX());
        terminals.add(v -> new SelfY());
        terminals.add(v -> new TreeX());
        terminals.add(v -> new TreeY());
        terminals.add(v -> new FieldWidth());
        terminals.add(v -> new FieldHeight());

        terminals.add(v -> new AIGuess());

        arity1.add(d -> new ReadMemory(d));
        arity2.add(d -> new WriteMemory(d));

        arity1.add(d -> new Negate(d));

        arity2.add(d -> ArithmeticNode.plus(d));
        arity2.add(d -> ArithmeticNode.minus(d));
        arity2.add(d -> ArithmeticNode.times(d));
        arity2.add(d -> ArithmeticNode.div(d));

        Map<Integer, List<Function<Integer, Node>>> constructors = new HashMap<>();
        constructors.put(0, terminals);
        constructors.put(1, arity1);
        constructors.put(2, arity2);
        constructors.put(3, arity3);
        NodeFactory.getInstance().setConstructors(constructors);
    }

    @Override
    public int playHeadToHead(Node... trees) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Double> getFitness(Node... trees) {
        return Arrays.stream(trees).map(t -> playRounds(t)).collect(Collectors.toList());
    }

    private double playRounds(Node tree) {
        double total = 0;
        for (int i = 0; i < rounds; i++) {
            total += score(tree);
        }
        return total;
    }

    private double score(Node tree) {
        SkiProvider.getInstance().resetGame();
        SkiGame game = SkiProvider.getInstance().getGame();
        while (game.getScore() < winningScore && !game.isDone()) {
            int move = (int) tree.evaluate();
            game.advanceFrame(clipMove(move));
        }
        return game.getScore();
    }

    private int clipMove(int move) {
        return move <= -1 ? -1 : move >= 1 ? 1 : 0;
    }

    private static Position closestTree(List<Position> trees, Position player) {
        double minDistance = Double.MAX_VALUE;
        Position closest = null;
        for (Position tree : trees) {
            double dist = distanceSquared(tree, player);
            if (dist < minDistance) {
                minDistance = dist;
                closest = tree;
            }
        }
        return closest;
    }

    private static double distanceSquared(Position tree, Position player) {
        double dx = tree.getX() - player.getX();
        double dy = tree.getY() - player.getY();
        return Math.abs(dx * dx + dy * dy);
    }

    public Player nodePlayer(Node node) {
        return new Player() {

            @Override
            public int move(SkiGame game) {
                double move = node.evaluate();
                return clipMove((int) move);
            }
        };
    }

    public static class SelfX extends Arity0Node {

        public SelfX() {
            super("Self_X");
        }

        @Override
        public double evaluate() {
            return SkiProvider.getInstance().getGame().getPlayerX();
        }

        @Override
        public Node copy() {
            return new SelfX();
        }
    }

    public static class SelfY extends Arity0Node {

        public SelfY() {
            super("Self_Y");
        }

        @Override
        public double evaluate() {
            return SkiProvider.getInstance().getGame().getPlayerY();
        }

        @Override
        public Node copy() {
            return new SelfY();
        }
    }

    public static class TreeX extends Arity0Node {

        public TreeX() {
            super("Tree_X");
        }

        @Override
        public double evaluate() {
            List<Position> trees = SkiProvider.getInstance().getGame().getTreePositions();
            return trees.size() > 0 ? trees.get(0).getX() : 0;
        }

        @Override
        public Node copy() {
            return new TreeX();
        }
    }

    public static class TreeY extends Arity0Node {

        public TreeY() {
            super("Tree_Y");
        }

        @Override
        public double evaluate() {
            List<Position> trees = SkiProvider.getInstance().getGame().getTreePositions();
            return trees.size() > 0 ? trees.get(0).getY() : 0;
        }

        @Override
        public Node copy() {
            return new TreeY();
        }
    }

    public static class ClosestTreeX extends Arity0Node {

        public ClosestTreeX() {
            super("ClosestTreeX");
        }

        @Override
        public double evaluate() {
            List<Position> trees = SkiProvider.getInstance().getGame().getTreePositions();
            double playerX = SkiProvider.getInstance().getGame().getPlayerX();
            double playerY = SkiProvider.getInstance().getGame().getPlayerY();
            Position player = new Position(playerX, playerY);
            Position closest = closestTree(trees, player);
            return closest == null ? 0 : closest.getX();
        }

        @Override
        public Node copy() {
            return new ClosestTreeX();
        }
    }

    public static class ClosestTreeY extends Arity0Node {

        public ClosestTreeY() {
            super("ClosestTreeY");
        }

        @Override
        public double evaluate() {
            List<Position> trees = SkiProvider.getInstance().getGame().getTreePositions();
            double playerX = SkiProvider.getInstance().getGame().getPlayerX();
            double playerY = SkiProvider.getInstance().getGame().getPlayerY();
            Position player = new Position(playerX, playerY);
            Position closest = closestTree(trees, player);
            return closest == null ? 0 : closest.getY();
        }

        @Override
        public Node copy() {
            return new ClosestTreeY();
        }
    }

    public static class FieldHeight extends Arity0Node {

        public FieldHeight() {
            super("FieldHeight");
        }

        @Override
        public double evaluate() {
            return SkiProvider.getInstance().getGame().getMaxY();
        }

        @Override
        public Node copy() {
            return new FieldHeight();
        }
    }

    public static class FieldWidth extends Arity0Node {

        public FieldWidth() {
            super("FieldWidth");
        }

        @Override
        public double evaluate() {
            return SkiProvider.getInstance().getGame().getMaxX();
        }

        @Override
        public Node copy() {
            return new FieldWidth();
        }
    }

    public static class AIGuess extends Arity0Node {
        private static AiPlayer player = new AiPlayer();

        public AIGuess() {
            super("AI_guess");
        }

        @Override
        public double evaluate() {
            return player.move(SkiProvider.getInstance().getGame());
        }

        @Override
        public Node copy() {
            return new AIGuess();
        }

    }
}
