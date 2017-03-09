package com.mateoi.gp.games;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.mateoi.gp.memory.Memory;
import com.mateoi.gp.tree.Arity0Node;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.functions.ArithmeticNode;
import com.mateoi.gp.tree.functions.Constant;
import com.mateoi.gp.tree.functions.Negate;
import com.mateoi.gp.tree.functions.ReadMemory;
import com.mateoi.gp.tree.functions.WriteMemory;
import com.mateoi.pong.AIPlayer;
import com.mateoi.pong.PongGame;

/**
 * A Game that implements Pong. Also contains the inner classes that specify
 * Pong-specific terminals.
 *
 * @author mateo
 *
 */
public class Pong implements Game {

    /** The number of rounds to play per matchup */
    private final int rounds;
    /** The number of points required to win a round */
    private int winningScore = 10;

    /**
     * Create an instance of Pong that plays the given number of rounds.
     *
     * @param rounds
     */
    public Pong(int rounds) {
        Memory.setMemorySupplier(PongProvider.getInstance());
        this.rounds = rounds;

        List<Function<Integer, Node>> arity1 = new ArrayList<>();
        List<Function<Integer, Node>> arity2 = new ArrayList<>();
        List<Function<Integer, Node>> arity3 = new ArrayList<>();
        List<Function<Integer, Node>> terminals = new ArrayList<>();

        terminals.add(v -> new BallX());
        terminals.add(v -> new BallY());
        terminals.add(v -> new BallVX());
        terminals.add(v -> new BallVY());

        terminals.add(v -> new SelfY());
        terminals.add(v -> new SelfVY());

        terminals.add(v -> new OppY());
        terminals.add(v -> new OppVY());

        terminals.add(v -> new PaddleSize());
        terminals.add(v -> new FieldWidth());
        terminals.add(v -> new FieldHeight());

        for (int i = 0; i < 20; i++) {
            terminals.add(v -> new Constant(Math.random() * 8 - 4));
        }

        arity2.add(d -> ArithmeticNode.plus(d));
        arity2.add(d -> ArithmeticNode.minus(d));
        arity2.add(d -> ArithmeticNode.times(d));
        arity2.add(d -> ArithmeticNode.div(d));

        terminals.add(v -> new AIGuess());

        arity1.add((d) -> new ReadMemory(d));
        arity2.add((d) -> new WriteMemory(d));

        arity1.add(d -> new Negate(d));

        Map<Integer, List<Function<Integer, Node>>> constructors = new HashMap<>();
        constructors.put(0, terminals);
        constructors.put(1, arity1);
        constructors.put(2, arity2);
        constructors.put(3, arity3);
        NodeFactory.getInstance().setConstructors(constructors);
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
    public List<Double> getFitness(Node... trees) {
        Node leftPlayer = trees[0];
        Node rightPlayer = trees[1];
        double rightScore = 0;
        double leftScore = 0;
        for (int i = 0; i < rounds; i++) {
            double[] scores = grade(leftPlayer, rightPlayer);
            leftScore += scores[0];
            rightScore += scores[1];
        }
        return Arrays.asList(leftScore, rightScore);
    }

    /**
     * Assign a score to two nodes depending on the winner and the number of
     * balls played
     *
     * @param leftPlayer
     * @param rightPlayer
     * @return
     */
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
        scores[winner] += 0.1;
        scores[0] += game.getLeftHits();
        scores[1] += game.getRightHits();
        return scores;
    }

    /**
     * Play a single round of Pong and determine the winner between the two
     * nodes.
     *
     * @param leftPlayer
     * @param rightPlayer
     * @return
     */
    private int playRound(Node leftPlayer, Node rightPlayer) {
        PongProvider.getInstance().resetGame();
        PongGame game = PongProvider.getInstance().getGame();
        while (game.getRightScore() < winningScore && game.getLeftScore() < winningScore) {
            PongProvider.getInstance().setLeftTurn(true);
            double leftMove = leftPlayer.evaluate();
            PongProvider.getInstance().setLeftTurn(false);
            double rightMove = rightPlayer.evaluate();
            game.nextFrame(clipMove(leftMove), clipMove(rightMove));
        }
        return game.getLeftScore() >= winningScore ? 0 : 1;
    }

    /**
     * Clip a move into the set of valid moves {-1, 0, 1}.
     *
     * @param move
     * @return
     */
    private int clipMove(double move) {
        return move <= -1 ? -1 : move >= 1 ? 1 : 0;
    }

    /**
     * A terminal node that evaluates to the x-coordinate of the ball
     *
     * @author mateo
     *
     */
    public static class BallX extends Arity0Node {

        public BallX() {
            super("Ball_X");
        }

        @Override
        public double evaluate() {
            return PongProvider.getInstance().getGame().getBallPosition().getX();
        }

        @Override
        public Node copy() {
            return new BallX();
        }
    }

    /**
     * A terminal node that evaluates to the y-coordinate of the ball
     *
     * @author mateo
     *
     */
    public static class BallY extends Arity0Node {

        public BallY() {
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

    /**
     * A terminal node that evaluates to the x-velocity of the ball
     *
     * @author mateo
     *
     */
    public static class BallVX extends Arity0Node {

        public BallVX() {
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

    /**
     * A terminal node that evaluates to the y-velocity of the ball
     *
     * @author mateo
     *
     */
    public static class BallVY extends Arity0Node {

        public BallVY() {
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

    /**
     * A terminal node that evaluates to the y-coordinate of the center of the
     * player's paddle
     *
     * @author mateo
     *
     */
    public static class SelfY extends Arity0Node {

        public SelfY() {
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

    /**
     * A terminal node that evaluates to the velocity of the player's paddle
     *
     * @author mateo
     *
     */
    public static class SelfVY extends Arity0Node {

        public SelfVY() {
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

    /**
     * A terminal node that evaluates to the y-coordinate of the center of the
     * opponent's paddle
     *
     * @author mateo
     *
     */
    public static class OppY extends Arity0Node {

        public OppY() {
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

    /**
     * A terminal node that evaluates to the y-velocity of the opponent's paddle
     *
     * @author mateo
     *
     */
    public static class OppVY extends Arity0Node {

        public OppVY() {
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

    /**
     * A terminal node that evaluates to the size of the paddle from the center
     * to the edge
     *
     * @author mateo
     *
     */
    public static class PaddleSize extends Arity0Node {

        public PaddleSize() {
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

    /**
     * A terminal node that evaluates to the total width of the playing field
     *
     * @author mateo
     *
     */
    public static class FieldWidth extends Arity0Node {

        public FieldWidth() {
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

    /**
     * A terminal node that evaluates to the total height of the playing field
     *
     * @author mateo
     *
     */
    public static class FieldHeight extends Arity0Node {

        public FieldHeight() {
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

    /**
     * A terminal node that evaluates to the Pong AI player's best guess
     *
     * @author mateo
     *
     */
    public static class AIGuess extends Arity0Node {
        private static AIPlayer leftPlayer = new AIPlayer(true);
        private static AIPlayer rightPlayer = new AIPlayer(false);

        public AIGuess() {
            super("AI_guess");
        }

        @Override
        public double evaluate() {
            boolean isLeftTurn = PongProvider.getInstance().isLeftTurn();
            AIPlayer player = isLeftTurn ? leftPlayer : rightPlayer;
            return player.move(PongProvider.getInstance().getGame());
        }

        @Override
        public Node copy() {
            return new AIGuess();
        }

    }

}
