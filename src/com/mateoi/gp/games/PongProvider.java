package com.mateoi.gp.games;

import java.util.function.Supplier;

import com.mateoi.gp.memory.Memory;
import com.mateoi.pong.PongGame;

/**
 * A provider of the Pong game. Statically stores memory and game state for the
 * current game being played.
 *
 * @author mateo
 *
 */
public class PongProvider implements Supplier<Memory> {

    /** The game being currently played */
    private PongGame game;
    /** Whether the player currently considering its move is the left one */
    private boolean leftTurn;

    /** Memory for the left player */
    private Memory leftMemory;
    /** Memory for the right player */
    private Memory rightMemory;

    /** Singleton instance of this class */
    private static PongProvider instance = new PongProvider();

    private PongProvider() {
        // Nothing here...
    }

    /**
     * Get the singleton instance of this class
     *
     * @return
     */
    public static PongProvider getInstance() {
        return instance;
    }

    /**
     * @return The game currently being played
     */
    public PongGame getGame() {
        return game;
    }

    /**
     * Alter the state of the game currently being played
     *
     * @param game
     */
    public void setGame(PongGame game) {
        this.game = game;
    }

    /**
     * Reset the game and memory states
     */
    public void resetGame() {
        game = new PongGame(500, 300);
        leftMemory = new Memory();
        rightMemory = new Memory();
    }

    /**
     * @return True if the left player is currently playing
     */
    public boolean isLeftTurn() {
        return leftTurn;
    }

    /**
     * Set whether the left player is currently playing
     *
     * @param leftTurn
     */
    public void setLeftTurn(boolean leftTurn) {
        this.leftTurn = leftTurn;
    }

    @Override
    public Memory get() {
        return leftTurn ? leftMemory : rightMemory;
    }

}
