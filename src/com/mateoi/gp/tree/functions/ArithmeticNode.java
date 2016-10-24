package com.mateoi.gp.tree.functions;

import java.util.function.BiFunction;

import com.mateoi.gp.tree.Arity2Node;

public class ArithmeticNode extends Arity2Node<Double, Double, Double> {

    public ArithmeticNode(String name, int depth, BiFunction<Double, Double, Double> function) {
        super(name, depth, Double.class, Double.class, Double.class, function);
    }

    public static ArithmeticNode plus(int depth) {
        return new ArithmeticNode("+", depth, (a, b) -> a + b);
    }

    public static ArithmeticNode minus(int depth) {
        return new ArithmeticNode("-", depth, (a, b) -> a - b);
    }

    public static ArithmeticNode times(int depth) {
        return new ArithmeticNode("*", depth, (a, b) -> a * b);
    }

    public static ArithmeticNode div(int depth) {
        return new ArithmeticNode("/", depth, (a, b) -> b == 0 ? a : a / b);
    }

    public static ArithmeticNode mod(int depth) {
        return new ArithmeticNode("mod", depth, (a, b) -> b == 0 ? a : a % b);
    }
}
