package com.mateoi.gp.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.functions.ArithmeticNode;
import com.mateoi.gp.tree.functions.ArithmeticPredicate;
import com.mateoi.gp.tree.functions.BooleanNode;
import com.mateoi.gp.tree.functions.Constant;
import com.mateoi.gp.tree.functions.IfNode;

public class NoRules implements Rules {

    public NoRules() {
        List<Function<Integer, Node>> arity1 = new ArrayList<>();
        List<Function<Integer, Node>> arity2 = new ArrayList<>();
        List<Function<Integer, Node>> arity3 = new ArrayList<>();
        List<Supplier<Node>> terminals = new ArrayList<>();

        terminals.add(() -> new Constant(-2));
        terminals.add(() -> new Constant(-1));
        terminals.add(() -> new Constant(0));
        terminals.add(() -> new Constant(1));
        terminals.add(() -> new Constant(2));

        arity2.add(d -> ArithmeticNode.plus(d));
        arity2.add(d -> ArithmeticNode.minus(d));
        arity2.add(d -> ArithmeticNode.mod(d));
        arity2.add(d -> ArithmeticNode.times(d));
        arity2.add(d -> ArithmeticNode.div(d));

        arity2.add(d -> ArithmeticPredicate.equalsNode(d));
        arity2.add(d -> ArithmeticPredicate.gt(d));
        arity2.add(d -> BooleanNode.and(d));
        arity2.add(d -> BooleanNode.or(d));
        arity2.add(d -> BooleanNode.xor(d));

        arity3.add(d -> new IfNode(d));

        NodeFactory.getInstance().setConstructors(arity1, arity2, arity3, terminals);
    }

    @Override
    public Map<Node, Double> score(List<Node> trees) {
        return trees.stream().collect(Collectors.toMap(Function.identity(), v -> 1.));
    }

    @Override
    public List<Node> bestNodes(List<Node> trees, int n) {
        return trees.subList(0, n);
    }

}
