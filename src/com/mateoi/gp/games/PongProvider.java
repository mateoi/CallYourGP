package com.mateoi.gp.games;

import java.util.function.Supplier;

import com.mateoi.gp.memory.Memory;
import com.mateoi.pong.PongGame;

public class PongProvider implements Supplier<Memory> {

    private PongGame game;
    private boolean leftTurn;
    private boolean verbose;

    private Memory leftMemory;
    private Memory rightMemory;

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
        leftMemory = new Memory();
        rightMemory = new Memory();
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

    @Override
    public Memory get() {
        return leftTurn ? leftMemory : rightMemory;
    }

}
