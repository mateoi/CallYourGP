package com.mateoi.gp.games;

import com.mateoi.ski.SkiGame;

public class SkiProvider {

    private SkiGame game;
    private static SkiProvider instance = new SkiProvider();

    public SkiProvider() {
        // nothing here...
    }

    public static SkiProvider getInstance() {
        return instance;
    }

    public SkiGame getGame() {
        return game;
    }

    public void setGame(SkiGame game) {
        this.game = game;
    }

    public void resetGame() {
        game = new SkiGame(10, 2.0, 0.03, 1, 0.05, 275, 420);
    }
}
