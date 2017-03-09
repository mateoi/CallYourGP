package com.mateoi.gp;

/**
 * Result of a head-to-head pairing between a Pong player and an opponent
 *
 * @author mateo
 *
 */
public class PongResult {

    /** Proportion of games won by the player */
    private final double winningRatio;
    /** Average balls returned per round */
    private final double averageRally;

    /**
     * Create a record of the result of the head-to-head matchup
     *
     * @param winningRatio
     * @param averageRally
     */
    public PongResult(double winningRatio, double averageRally) {
        this.winningRatio = winningRatio;
        this.averageRally = averageRally;
    }

    /**
     * @return The proportion of games won by the player
     */
    public double getWinningRatio() {
        return winningRatio;
    }

    /**
     * @return The average balls returned per round
     */
    public double getAverageRally() {
        return averageRally;
    }

    @Override
    public String toString() {
        return "(" + winningRatio + ";" + averageRally + ")";
    }
}
