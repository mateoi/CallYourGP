package com.mateoi.gp.games;

import com.mateoi.pong.PongGame;

public class PongProvider {

    private PongGame game;
    private boolean leftTurn;
    private boolean verbose;

    private static PongProvider instance = new PongProvider();

    private PongProvider() {
        // Nothing here...
    }

    public static PongProvider getInstance() {
        return instance;
    }

    public PongGame getGame() {
        return game;
    }

    public void setGame(PongGame game) {
        this.game = game;
    }

    public void resetGame() {
        game = new PongGame(500, 300);
    }

    public boolean isLeftTurn() {
        return leftTurn;
    }

    public void setLeftTurn(boolean leftTurn) {
        this.leftTurn = leftTurn;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

}
