package com.mateoi.gp.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mateoi.gp.tree.functions.Constant;

@SuppressWarnings("rawtypes")
public class NodeFactory {

    private static final NodeFactory instance = new NodeFactory();
    private final Map<Class, List<Function<Integer, Node>>> typeConstructors = new HashMap<>();
    private final Map<Class, List<Supplier<Node>>> terminalConstructors = new HashMap<>();
    private final List<BiFunction<Class, Integer, Node>> genericConstructors = new ArrayList<>();

    public Node createRandomNode(Class type, int depth) {
        if (depth <= 0) {
            return createTerminal(type);
        } else {
            return createAnyNode(type, depth);
        }
    }

    private Node createTerminal(Class type) {
        final List<Supplier<Node>> list = this.terminalConstructors.get(type);
        final int index = randInt(list.size());
        return list.get(index).get();
    }

    private Node createAnyNode(Class type, int depth) {
        final List<Function<Integer, Node>> constructors = this.typeConstructors.get(type);
        final int total = constructors.size() + this.genericConstructors.size();
        final int index = randInt(total);
        if (index < constructors.size()) {
            final Function<Integer, Node> c = constructors.get(index);
            return c.apply(depth);
        } else {
            final BiFunction<Class, Integer, Node> c = this.genericConstructors.get(index - constructors.size());
            return c.apply(type, depth);
        }
    }

    private int randInt(int max) {
        final double rand = Math.random();
        return (int) (rand * max);
    }

    private NodeFactory() {
        // private to prevent instantiation
    	List<Function<Integer, Node>> doubles = new ArrayList<>();
    	doubles.add(d->Constant.doubleConstant(0.0));
    	doubles.add(d->Constant.doubleConstant(1.0));
    	doubles.add(d->Constant.doubleConstant(-1.0));
    	doubles.add(d->Constant.doubleConstant(2.0));
    	doubles.add(d->Constant.doubleConstant(-2.0));
    	typeConstructors.put(Double.class, doubles);
    	
    	List<Supplier<Node>> doubleTerminals = new ArrayList<>();
    	doubleTerminals.add(() -> Constant.doubleConstant(0.0));
    	doubleTerminals.add(() -> Constant.doubleConstant(1.0));
    	doubleTerminals.add(() -> Constant.doubleConstant(-1.0));
    	doubleTerminals.add(() -> Constant.doubleConstant(2.0));
    	doubleTerminals.add(() -> Constant.doubleConstant(-2.0));
    	terminalConstructors.put(Double.class, doubleTerminals);
    }

    public static NodeFactory getInstance() {
        return instance;
    }

}
