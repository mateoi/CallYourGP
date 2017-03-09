package com.mateoi.gp.games;

import java.util.function.Supplier;

import com.mateoi.gp.memory.Memory;
import com.mateoi.ski.SkiGame;

/**
 * A provider of the Ski game. Statically stores memory and game state for the
 * current game being played.
 *
 * @author mateo
 *
 */
public class SkiProvider implements Supplier<Memory> {

    /** The game being currently played */
    private SkiGame game;
    /** Singleton instance of this class */
    private static SkiProvider instance = new SkiProvider();

    /** Memory for the player */
    private Memory memory;

    private SkiProvider() {
        // nothing here...
    }

    public static SkiProvider getInstance() {
        return instance;
    }

    /**
     * Get the singleton instance of this class
     *
     * @return
     */
    public SkiGame getGame() {
        return game;
    }

    /**
     * Alter the state of the game currently being played
     *
     * @param game
     */
    public void setGame(SkiGame game) {
        this.game = game;
    }

    /**
     * Reset the game and memory states
     */
    public void resetGame() {
        game = new SkiGame(4, 2.0, 0.03, 1.5, 0.05, 150, 250);
        memory = new Memory();
    }

    @Override
    public Memory get() {
        return memory;
    }
}
