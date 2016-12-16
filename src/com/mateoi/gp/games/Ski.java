package com.mateoi.gp.games;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.mateoi.gp.tree.Arity0Node;
import com.mateoi.gp.tree.Node;
import com.mateoi.ski.SkiGame;

public class Ski implements Game {
    private final int rounds;
    private int winningScore = 1000;

    public Ski(int rounds) {
        this.rounds = rounds;
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

    public class SelfX extends Arity0Node {

        protected SelfX() {
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

    public class SelfY extends Arity0Node {

        protected SelfY() {
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

    public class TreeX extends Arity0Node {

        protected TreeX() {
            super("Tree_X");
        }

        @Override
        public double evaluate() {
            return SkiProvider.getInstance().getGame().getTreePositions().get(0).getX();
        }

        @Override
        public Node copy() {
            return new TreeX();
        }
    }

    public class TreeY extends Arity0Node {

        protected TreeY() {
            super("Tree_Y");
        }

        @Override
        public double evaluate() {
            return SkiProvider.getInstance().getGame().getTreePositions().get(0).getY();
        }

        @Override
        public Node copy() {
            return new TreeY();
        }
    }

    public class FieldHeight extends Arity0Node {

        protected FieldHeight() {
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

    public class FieldWidth extends Arity0Node {

        protected FieldWidth() {
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

}
