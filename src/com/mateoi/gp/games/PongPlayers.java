package com.mateoi.gp.games;

import com.mateoi.gp.tree.Node;
import com.mateoi.pong.AIPlayer;
import com.mateoi.pong.Player;
import com.mateoi.pong.PongGame;

public class PongPlayers {

    private PongPlayers() {
        // private to prevent instantiation
    }

    private static int clipMove(int move) {
        return move <= -1 ? -1 : move >= 1 ? 1 : 0;
    }

    public static Player nodePlayer(Node node) {
        return new Player() {
            @Override
            public int move(PongGame state) {
                double move = node.evaluate();
                return clipMove((int) move);
            }
        };
    }

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
                return clipMove((int) move);
            }
        };
    }

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
                return clipMove((int) move);
            }
        };
    }

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
