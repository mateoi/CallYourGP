package com.mateoi.gp.games;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mateoi.gp.tree.Arity0Node;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.functions.ArithmeticNode;
import com.mateoi.gp.tree.functions.ArithmeticPredicate;
import com.mateoi.gp.tree.functions.BooleanNode;
import com.mateoi.gp.tree.functions.Constant;
import com.mateoi.gp.tree.functions.IfNode;

public class Nim1D implements Game {

    private final int numberOfSticks;
    private int currentNumberOfSticks;
    private int lastMove;

    public Nim1D(int numberOfSticks) {
        this.numberOfSticks = numberOfSticks;
        populateNodeLists();
    }

    private void populateNodeLists() {
        List<Function<Integer, Node>> functions = new ArrayList<>();
        List<Supplier<Node>> terminals = new ArrayList<>();

        // functions
        functions.add(d -> ArithmeticNode.plus(d));
        functions.add(d -> ArithmeticNode.minus(d));
        functions.add(d -> ArithmeticNode.mod(d));
        functions.add(d -> ArithmeticNode.times(d));
        functions.add(d -> ArithmeticNode.div(d));

        functions.add(d -> ArithmeticPredicate.equalsNode(d));
        functions.add(d -> ArithmeticPredicate.gt(d));
        functions.add(d -> BooleanNode.and(d));
        functions.add(d -> BooleanNode.or(d));

        functions.add(d -> new IfNode(d));

        // constants
        terminals.add(() -> new Constant(-2));
        terminals.add(() -> new Constant(-1));
        terminals.add(() -> new Constant(0));
        terminals.add(() -> new Constant(1));
        terminals.add(() -> new Constant(2));

        // terminals
        terminals.add(() -> gameStateConstructor());

        NodeFactory.getInstance().setConstructors(functions, terminals);
    }

    private Node gameStateConstructor() {
        return new Arity0Node("n") {

            @Override
            public int evaluate() {
                return currentNumberOfSticks;
            }

            @Override
            public Node copy() {
                return gameStateConstructor();
            }
        };
    }

    @Override
    public void reset() {
        currentNumberOfSticks = numberOfSticks;
    }

    @Override
    public boolean isFinished() {
        return currentNumberOfSticks == 0;
    }

    @Override
    public int getWinner() {
        return lastMove ^= 1;
    }

    @Override
    public void move(int player, int move) {
        lastMove = player;
        int toSubtract = move <= 1 ? 1 : 2;
        currentNumberOfSticks -= toSubtract;
        currentNumberOfSticks = Math.max(currentNumberOfSticks, 0);
    }

}
