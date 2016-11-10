package com.mateoi.gp.games;

public interface Game {

    public void reset();

    public boolean isFinished();

    public int getWinner();

    public void move(int player, int move);
}
