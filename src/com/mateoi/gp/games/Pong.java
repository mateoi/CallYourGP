package com.mateoi.gp.games;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.mateoi.gp.tree.Arity0Node;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.functions.ArithmeticNode;
import com.mateoi.gp.tree.functions.Constant;
import com.mateoi.pong.Player;
import com.mateoi.pong.PongGame;

public class Pong implements Game {

    private final int rounds;
    private int winningScore = 10;

    public Pong(int rounds) {
        this.rounds = rounds;

        List<Function<Integer, Node>> arity1 = new ArrayList<>();
        List<Function<Integer, Node>> arity2 = new ArrayList<>();
        List<Function<Integer, Node>> arity3 = new ArrayList<>();
        List<Supplier<Node>> terminals = new ArrayList<>();

        terminals.add(() -> new BallX());
        terminals.add(() -> new BallY());
        terminals.add(() -> new BallVX());
        terminals.add(() -> new BallVY());

        terminals.add(() -> new SelfX());
        terminals.add(() -> new SelfY());
        terminals.add(() -> new SelfVY());

        terminals.add(() -> new OppX());
        terminals.add(() -> new OppY());
        terminals.add(() -> new OppVY());

        terminals.add(() -> new PaddleSize());
        terminals.add(() -> new FieldWidth());
        terminals.add(() -> new FieldHeight());

        for (int i = 0; i < 20; i++) {
            terminals.add(() -> new Constant(Math.random() * 8 - 4));
        }

        arity2.add(d -> ArithmeticNode.plus(d));
        arity2.add(d -> ArithmeticNode.minus(d));
        arity2.add(d -> ArithmeticNode.times(d));
        arity2.add(d -> ArithmeticNode.div(d));

        // arity2.add(d -> ArithmeticPredicate.equalsNode(d));
        // arity2.add(d -> ArithmeticPredicate.gt(d));
        // arity2.add(d -> BooleanNode.and(d));
        // arity2.add(d -> BooleanNode.or(d));
        // arity2.add(d -> BooleanNode.xor(d));
        //
        // arity3.add(d -> new IfNode(d));

        NodeFactory.getInstance().setConstructors(arity1, arity2, arity3, terminals);

    }

    @Override
    public int playHeadToHead(Node... trees) {
        Node leftPlayer = trees[0];
        Node rightPlayer = trees[1];
        int rightScore = 0;
        int leftScore = 0;
        for (int i = 0; i < rounds; i++) {
            int winner = playRound(leftPlayer, rightPlayer);
            if (winner == 0) {
                leftScore++;
            } else {
                rightScore++;
            }
        }
        return rightScore > leftScore ? 1 : 0;
    }

    @Override
    public double[] getFitness(Node... trees) {
        Node leftPlayer = trees[0];
        Node rightPlayer = trees[1];
        double rightScore = 0;
        double leftScore = 0;
        for (int i = 0; i < rounds; i++) {
            double[] scores = grade(leftPlayer, rightPlayer);
            leftScore += scores[0];
            rightScore += scores[1];
        }
        double[] results = { leftScore, rightScore };
        return results;
    }

    private double[] grade(Node leftPlayer, Node rightPlayer) {
        PongProvider.getInstance().resetGame();
        PongGame game = PongProvider.getInstance().getGame();
        while (game.getRightScore() < winningScore && game.getLeftScore() < winningScore) {
            PongProvider.getInstance().setLeftTurn(true);
            int leftMove = (int) leftPlayer.evaluate();
            PongProvider.getInstance().setLeftTurn(false);
            int rightMove = (int) rightPlayer.evaluate();
            game.nextFrame(clipMove(leftMove), clipMove(rightMove));
        }
        double score = game.getAverageRally();
        int winner = game.getLeftScore() >= winningScore ? 0 : 1;
        double[] scores = { score, score };
        scores[winner] += 0.2;
        scores[0] += game.getLeftHits();
        scores[1] += game.getRightHits();
        return scores;
    }

    public Player nodePlayer(Node node) {
        return new Player() {
            @Override
            public int move(PongGame state) {
                double move = node.evaluate();
                return clipMove((int) move);
            }
        };
    }

    private int playRound(Node leftPlayer, Node rightPlayer) {
        PongProvider.getInstance().resetGame();
        PongGame game = PongProvider.getInstance().getGame();
        while (game.getRightScore() < winningScore && game.getLeftScore() < winningScore) {
            PongProvider.getInstance().setLeftTurn(true);
            int leftMove = (int) leftPlayer.evaluate();
            PongProvider.getInstance().setLeftTurn(false);
            int rightMove = (int) rightPlayer.evaluate();
            game.nextFrame(clipMove(leftMove), clipMove(rightMove));
        }
        return game.getLeftScore() >= winningScore ? 0 : 1;
    }

    private int clipMove(int move) {
        return move <= -1 ? -1 : move >= 1 ? 1 : 0;
    }

    public class BallX extends Arity0Node {

        protected BallX() {
            super("Ball_X");
        }

        @Override
        public double evaluate() {
            if (PongProvider.getInstance().isVerbose()) {
                System.out.println(PongProvider.getInstance().getGame().getBallPosition().getX());
            }
            return PongProvider.getInstance().getGame().getBallPosition().getX();
        }

        @Override
        public Node copy() {
            return new BallX();
        }
    }

    public class BallY extends Arity0Node {

        protected BallY() {
            super("Ball_Y");
        }

        @Override
        public double evaluate() {
            return PongProvider.getInstance().getGame().getBallPosition().getY();
        }

        @Override
        public Node copy() {
            return new BallY();
        }
    }

    public class BallVX extends Arity0Node {

        protected BallVX() {
            super("Ball_VX");
        }

        @Override
        public double evaluate() {
            return PongProvider.getInstance().getGame().getBallVelocity().getX();
        }

        @Override
        public Node copy() {
            return new BallVX();
        }
    }

    public class BallVY extends Arity0Node {

        protected BallVY() {
            super("Ball_VY");
        }

        @Override
        public double evaluate() {
            return PongProvider.getInstance().getGame().getBallVelocity().getY();
        }

        @Override
        public Node copy() {
            return new BallVY();
        }
    }

    public class SelfX extends Arity0Node {

        protected SelfX() {
            super("Self_X");
        }

        @Override
        public double evaluate() {
            boolean leftTurn = PongProvider.getInstance().isLeftTurn();
            Vector2D position = leftTurn ? PongProvider.getInstance().getGame().getLeftPaddleCenter()
                    : PongProvider.getInstance().getGame().getRightPaddleCenter();
            return position.getX();
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
            boolean leftTurn = PongProvider.getInstance().isLeftTurn();
            Vector2D position = leftTurn ? PongProvider.getInstance().getGame().getLeftPaddleCenter()
                    : PongProvider.getInstance().getGame().getRightPaddleCenter();
            return position.getY();
        }

        @Override
        public Node copy() {
            return new SelfY();
        }
    }

    public class SelfVY extends Arity0Node {

        protected SelfVY() {
            super("Self_VY");
        }

        @Override
        public double evaluate() {
            boolean leftTurn = PongProvider.getInstance().isLeftTurn();
            Vector2D velocity = leftTurn ? PongProvider.getInstance().getGame().getLeftPaddleVelocity()
                    : PongProvider.getInstance().getGame().getRightPaddleVelocity();
            return velocity.getY();
        }

        @Override
        public Node copy() {
            return new SelfVY();
        }
    }

    public class OppX extends Arity0Node {

        protected OppX() {
            super("Opp_X");
        }

        @Override
        public double evaluate() {
            boolean leftTurn = PongProvider.getInstance().isLeftTurn();
            Vector2D position = !leftTurn ? PongProvider.getInstance().getGame().getLeftPaddleCenter()
                    : PongProvider.getInstance().getGame().getRightPaddleCenter();
            return position.getX();
        }

        @Override
        public Node copy() {
            return new OppX();
        }
    }

    public class OppY extends Arity0Node {

        protected OppY() {
            super("Opp_Y");
        }

        @Override
        public double evaluate() {
            boolean leftTurn = PongProvider.getInstance().isLeftTurn();
            Vector2D position = !leftTurn ? PongProvider.getInstance().getGame().getLeftPaddleCenter()
                    : PongProvider.getInstance().getGame().getRightPaddleCenter();
            return position.getY();
        }

        @Override
        public Node copy() {
            return new OppY();
        }
    }

    public class OppVY extends Arity0Node {

        protected OppVY() {
            super("Opp_VY");
        }

        @Override
        public double evaluate() {
            boolean leftTurn = PongProvider.getInstance().isLeftTurn();
            Vector2D velocity = !leftTurn ? PongProvider.getInstance().getGame().getLeftPaddleVelocity()
                    : PongProvider.getInstance().getGame().getRightPaddleVelocity();
            return velocity.getY();
        }

        @Override
        public Node copy() {
            return new OppVY();
        }
    }

    public class PaddleSize extends Arity0Node {

        protected PaddleSize() {
            super("Paddle_Size");
        }

        @Override
        public double evaluate() {
            return PongProvider.getInstance().getGame().getPaddleRadius();
        }

        @Override
        public Node copy() {
            return new PaddleSize();
        }
    }

    public class FieldWidth extends Arity0Node {

        protected FieldWidth() {
            super("Field_Width");
        }

        @Override
        public double evaluate() {
            return PongProvider.getInstance().getGame().getFieldWidth();
        }

        @Override
        public Node copy() {
            return new FieldWidth();
        }
    }

    public class FieldHeight extends Arity0Node {

        protected FieldHeight() {
            super("Field_Height");
        }

        @Override
        public double evaluate() {
            return PongProvider.getInstance().getGame().getFieldHeight();
        }

        @Override
        public Node copy() {
            return new FieldHeight();
        }
    }

}
