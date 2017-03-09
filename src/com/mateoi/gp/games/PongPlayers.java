package com.mateoi.gp.games;

import com.mateoi.gp.tree.Node;
import com.mateoi.pong.AIPlayer;
import com.mateoi.pong.Player;
import com.mateoi.pong.PongGame;

/**
 * Implementations of Langdon and Poli's Pong players
 *
 * @author mateo
 *
 */
public class PongPlayers {

    private PongPlayers() {
        // private to prevent instantiation
    }

    /**
     * Clip a move into the set of valid moves {-1, 0, 1}.
     *
     * @param move
     * @return
     */
    private static int clipMove(double move) {
        return move <= -1 ? -1 : move >= 1 ? 1 : 0;
    }

    /**
     * Convert a Node into a Player that can play a graphical game of Pong.
     *
     * @param node
     * @return
     */
    public static Player nodePlayer(Node node) {
        return new Player() {
            @Override
            public int move(PongGame state) {
                double move = node.evaluate();
                return clipMove(move);
            }
        };
    }

    /**
     * A Player equivalent to Langdon and Poli's best GP player
     *
     * @return
     */
    public static Player langdonGP() {
        return new Player() {
            @Override
            public int move(PongGame state) {
                boolean left = PongProvider.getInstance().isLeftTurn();
                double ballY = state.getBallPosition().getY();
                double ballVY = state.getBallVelocity().getY();
                double selfY = left ? state.getLeftPaddleCenter().getY() : state.getRightPaddleCenter().getY();
                double selfVY = left ? state.getLeftPaddleVelocity().getY() : state.getRightPaddleVelocity().getY();
                double constant = 0.62;

                double move = ballVY + constant * ((ballY - selfY) + (ballVY - selfVY));
                return clipMove(move);
            }
        };
    }

    /**
     * A Player equivalent to Langdon and Poli's best hybrid GP player
     *
     * @return
     */
    public static Player langdonHybridGP() {
        return new Player() {
            @Override
            public int move(PongGame state) {
                boolean left = PongProvider.getInstance().isLeftTurn();
                Player aiPlayer = new AIPlayer(left);
                double ballY = state.getBallPosition().getY();
                double ballVY = state.getBallVelocity().getY();
                double selfVY = left ? state.getLeftPaddleVelocity().getY() : state.getRightPaddleVelocity().getY();
                double guess = aiPlayer.move(state);

                double move = 1.34 * ((guess - ballY) + (ballVY - selfVY) + ballVY + 0.69);
                return clipMove(move);
            }
        };
    }

    /**
     * A Player that plays as the Pong AI, but can play on either side of the
     * board
     * 
     * @return
     */
    public static Player versatileAI() {
        return new Player() {
            private AIPlayer leftPlayer = new AIPlayer(true);
            private AIPlayer rightPlayer = new AIPlayer(false);

            @Override
            public int move(PongGame state) {
                boolean isLeftTurn = PongProvider.getInstance().isLeftTurn();
                return isLeftTurn ? leftPlayer.move(state) : rightPlayer.move(state);
            }
        };
    }

}
