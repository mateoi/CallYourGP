package com.mateoi.gp.tree;

import com.mateoi.gp.games.Pong;

public class PongParser extends Parser {

    public PongParser() {
        // Do nothing
    }

    @Override
    protected Node gameSpecificNode(String name, int depth) {
        if ("Self_Y".equals(name)) {
            return new Pong.SelfY();
        } else if ("Self_VY".equals(name)) {
            return new Pong.SelfVY();
        } else if ("Ball_X".equals(name)) {
            return new Pong.BallX();
        } else if ("Ball_Y".equals(name)) {
            return new Pong.BallY();
        } else if ("Ball_VX".equals(name)) {
            return new Pong.BallVX();
        } else if ("Ball_VY".equals(name)) {
            return new Pong.BallVY();
        } else if ("Opp_Y".equals(name)) {
            return new Pong.OppY();
        } else if ("Opp_VY".equals(name)) {
            return new Pong.OppVY();
        } else if ("Field_Width".equals(name)) {
            return new Pong.FieldWidth();
        } else if ("Field_Height".equals(name)) {
            return new Pong.FieldHeight();
        } else if ("Paddle_Size".equals(name)) {
            return new Pong.PaddleSize();
        } else if ("AI_guess".equals(name)) {
            return new Pong.AIGuess();
        } else {
            System.out.println(name);
            return null;
        }
    }

}
