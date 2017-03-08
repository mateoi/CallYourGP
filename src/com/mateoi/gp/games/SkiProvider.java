package com.mateoi.gp.games;

import java.util.function.Supplier;

import com.mateoi.gp.memory.Memory;
import com.mateoi.ski.SkiGame;

public class SkiProvider implements Supplier<Memory> {

    private SkiGame game;
    private static SkiProvider instance = new SkiProvider();

    private Memory memory;

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
        game = new SkiGame(4, 2.0, 0.03, 1.5, 0.05, 150, 250);
        memory = new Memory();
    }

    @Override
    public Memory get() {
        return memory;
    }
}
