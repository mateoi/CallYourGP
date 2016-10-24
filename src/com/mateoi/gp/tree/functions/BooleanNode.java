package com.mateoi.gp.tree.functions;

import java.util.function.BiFunction;

import com.mateoi.gp.tree.Arity1Node;
import com.mateoi.gp.tree.Arity2Node;

public class BooleanNode extends Arity2Node<Boolean, Boolean, Boolean> {

    public BooleanNode(String name, int depth, BiFunction<Boolean, Boolean, Boolean> function) {
        super(name, depth, Boolean.class, Boolean.class, Boolean.class, function);
    }

    public static BooleanNode and(int depth) {
        return new BooleanNode("and", depth, (a, b) -> a && b);
    }

    public static BooleanNode or(int depth) {
        return new BooleanNode("or", depth, (a, b) -> a || b);
    }

    public static BooleanNode xor(int depth) {
        return new BooleanNode("xor", depth, (a, b) -> a != b);
    }

    public static Arity1Node<Boolean, Boolean> not(int depth) {
        return new Arity1Node<>("not", depth, Boolean.class, Boolean.class, b -> !b);
    }

}
