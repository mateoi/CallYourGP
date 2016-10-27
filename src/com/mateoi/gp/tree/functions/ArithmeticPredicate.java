package com.mateoi.gp.tree.functions;

import java.util.function.BiFunction;

import com.mateoi.gp.tree.Arity2Node;

public class ArithmeticPredicate extends Arity2Node<Double, Double, Boolean> {

	public ArithmeticPredicate(String name, int depth, BiFunction<Double, Double, Boolean> function) {
		super(name, depth, Boolean.class, Double.class, Double.class, function);
	}

	public static ArithmeticPredicate equalsNode(int depth) {
		return new ArithmeticPredicate("==", depth, (a, b) -> a == b);
	}

	public static ArithmeticPredicate gt(int depth) {
		return new ArithmeticPredicate(">", depth, (a, b) -> a > b);
	}
}
