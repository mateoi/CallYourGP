package com.mateoi.gp;

public class PongResult {

    private final double winningRatio;
    private final double averageRally;

    public PongResult(double winningRatio, double averageRally) {
        this.winningRatio = winningRatio;
        this.averageRally = averageRally;
    }

    public double getWinningRatio() {
        return winningRatio;
    }

    public double getAverageRally() {
        return averageRally;
    }

    @Override
    public String toString() {
        return "(" + winningRatio + ";" + averageRally + ")";
    }
}
