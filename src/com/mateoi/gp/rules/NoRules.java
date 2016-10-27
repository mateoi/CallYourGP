package com.mateoi.gp.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;
import com.mateoi.gp.tree.functions.ArithmeticNode;
import com.mateoi.gp.tree.functions.ArithmeticPredicate;
import com.mateoi.gp.tree.functions.BooleanNode;
import com.mateoi.gp.tree.functions.Constant;
import com.mateoi.gp.tree.functions.IfNode;

public class NoRules<T> implements Rules<T> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NoRules() {
		final Map<Class, List<Function<Integer, Node>>> typeConstructors = new HashMap<>();
		final Map<Class, List<Supplier<Node>>> terminalConstructors = new HashMap<>();
		final List<BiFunction<Class, Integer, Node>> genericConstructors = new ArrayList<>();

		final List<Supplier<Node>> doubleTerminals = new ArrayList<>();
		doubleTerminals.add(() -> Constant.doubleConstant(0.0));
		doubleTerminals.add(() -> Constant.doubleConstant(1.0));
		doubleTerminals.add(() -> Constant.doubleConstant(-1.0));
		doubleTerminals.add(() -> Constant.doubleConstant(2.0));
		doubleTerminals.add(() -> Constant.doubleConstant(-2.0));
		terminalConstructors.put(Double.class, doubleTerminals);

		final List<Supplier<Node>> boolTerminals = new ArrayList<>();
		boolTerminals.add(() -> Constant.boolConstant(false));
		boolTerminals.add(() -> Constant.boolConstant(true));
		terminalConstructors.put(Boolean.class, boolTerminals);

		final List<Function<Integer, Node>> doubles = new ArrayList<>();
		NodeFactory.putTerminalsInNodes(doubleTerminals, doubles);
		doubles.add(d -> ArithmeticNode.plus(d));
		doubles.add(d -> ArithmeticNode.minus(d));
		doubles.add(d -> ArithmeticNode.mod(d));
		doubles.add(d -> ArithmeticNode.times(d));
		doubles.add(d -> ArithmeticNode.div(d));
		typeConstructors.put(Double.class, doubles);

		final List<Function<Integer, Node>> bools = new ArrayList<>();
		NodeFactory.putTerminalsInNodes(boolTerminals, bools);
		bools.add(d -> ArithmeticPredicate.equalsNode(d));
		bools.add(d -> ArithmeticPredicate.gt(d));
		bools.add(d -> BooleanNode.and(d));
		bools.add(d -> BooleanNode.or(d));
		bools.add(d -> BooleanNode.xor(d));
		typeConstructors.put(Boolean.class, bools);

		genericConstructors.add((c, d) -> new IfNode(d, c));
		NodeFactory.getInstance().setConstructors(typeConstructors, terminalConstructors, genericConstructors);
	}

	@Override
	public List<Node<T>> nextGeneration(List<Node<T>> trees) {
		return trees;
	}

	@Override
	public Node<T> bestNode(List<Node<T>> trees) {
		return trees.get(0);
	}

}
