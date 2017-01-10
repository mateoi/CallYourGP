package com.mateoi.gp.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.mateoi.gp.tree.Arity0Node;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.functions.ArithmeticNode;
import com.mateoi.gp.tree.functions.Constant;
import com.mateoi.gp.tree.functions.Negate;
import com.mateoi.ski.AiPlayer;
import com.mateoi.ski.Player;
import com.mateoi.ski.Position;
import com.mateoi.ski.SkiGame;

public class Ski implements Game {
    private final int rounds;
    private int winningScore = 1000;

    public Ski(int rounds) {
        this.rounds = rounds;

        List<Function<Integer, Node>> arity1 = new ArrayList<>();
        List<Function<Integer, Node>> arity2 = new ArrayList<>();
        List<Function<Integer, Node>> arity3 = new ArrayList<>();
        List<Supplier<Node>> terminals = new ArrayList<>();

        terminals.add(() -> new Constant(-1));
        terminals.add(() -> new Constant(0));
        terminals.add(() -> new Constant(1));

        for (int i = 0; i < 10; i++) {
            terminals.add(() -> new Constant(Math.random() * 6 - 3));
        }

        terminals.add(() -> new SelfX());
        terminals.add(() -> new SelfY());
        terminals.add(() -> new TreeX());
        terminals.add(() -> new TreeY());
        terminals.add(() -> new FieldWidth());
        terminals.add(() -> new FieldHeight());

        arity1.add(d -> new Negate(d));

        arity2.add(d -> ArithmeticNode.plus(d));
        arity2.add(d -> ArithmeticNode.minus(d));
        arity2.add(d -> ArithmeticNode.times(d));
        arity2.add(d -> ArithmeticNode.div(d));

        NodeFactory.getInstance().setConstructors(arity1, arity2, arity3, terminals);

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
