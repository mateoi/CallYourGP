package com.mateoi.gp.tree;

import com.mateoi.gp.games.Ski;

public class SkiParser extends Parser {

    public SkiParser() {
        // Do nothing
    }

    @Override
    protected Node gameSpecificNode(String name, int depth) {
        if ("Self_X".equals(name)) {
            return new Ski.SelfX();
        } else if ("Self_Y".equals(name)) {
            return new Ski.SelfY();
        } else if ("Tree_X".equals(name)) {
            return new Ski.TreeX();
        } else if ("Tree_Y".equals(name)) {
            return new Ski.TreeY();
        } else if ("FieldWidth".equals(name)) {
            return new Ski.FieldWidth();
        } else if ("FieldHeight".equals(name)) {
            return new Ski.FieldHeight();
        } else if ("AI_guess".equals(name)) {
            return new Ski.AIGuess();
        } else {
            return null;
        }
    }

}
