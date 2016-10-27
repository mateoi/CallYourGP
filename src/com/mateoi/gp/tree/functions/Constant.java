package com.mateoi.gp.tree.functions;

import com.mateoi.gp.exceptions.TypeMismatch;
import com.mateoi.gp.tree.Arity0Node;

public class Constant<T> extends Arity0Node<T> {

    private final T value;

    public Constant(T value, Class<T> type) {
        super(value.toString(), type);
        this.value = value;
    }

    @Override
    public T evaluate() throws TypeMismatch {
        return this.value;
    }

    public static Constant<Double> doubleConstant(double value) {
        return new Constant<>(value, Double.class);
    }

    public static Constant<Integer> intConstant(int value) {
        return new Constant<>(value, Integer.class);
    }

    public static Constant<Boolean> boolConstant(boolean value) {
        return new Constant<>(value, Boolean.class);
    }

}
