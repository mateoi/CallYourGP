package com.mateoi.gp.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.functions.ArithmeticNode;
import com.mateoi.gp.tree.functions.ArithmeticPredicate;
import com.mateoi.gp.tree.functions.BooleanNode;
import com.mateoi.gp.tree.functions.Constant;
import com.mateoi.gp.tree.functions.IfNode;

public class NoRules implements Rules {

    public NoRules() {
        List<Function<Integer, Node>> functions = new ArrayList<>();
        List<Supplier<Node>> terminals = new ArrayList<>();

        terminals.add(() -> new Constant(-2));
        terminals.add(() -> new Constant(-1));
        terminals.add(() -> new Constant(0));
        terminals.add(() -> new Constant(1));
        terminals.add(() -> new Constant(2));

        functions.add(d -> ArithmeticNode.plus(d));
        functions.add(d -> ArithmeticNode.minus(d));
        functions.add(d -> ArithmeticNode.mod(d));
        functions.add(d -> ArithmeticNode.times(d));
        functions.add(d -> ArithmeticNode.div(d));

        functions.add(d -> ArithmeticPredicate.equalsNode(d));
        functions.add(d -> ArithmeticPredicate.gt(d));
        functions.add(d -> BooleanNode.and(d));
        functions.add(d -> BooleanNode.or(d));
        functions.add(d -> BooleanNode.xor(d));

        functions.add(d -> new IfNode(d));

        NodeFactory.getInstance().setConstructors(functions, terminals);
    }

    @Override
    public List<Node> nextGeneration(List<Node> trees) {
        return trees;
    }

    @Override
    public Node bestNode(List<Node> trees) {
        return trees.get(0);
    }

}
