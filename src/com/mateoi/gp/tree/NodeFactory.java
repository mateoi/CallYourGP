package com.mateoi.gp.tree;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import com.mateoi.gp.exceptions.NoConstructorsSet;

@SuppressWarnings("rawtypes")
public class NodeFactory {

	private static final NodeFactory instance = new NodeFactory();
	private Map<Class, List<Function<Integer, Node>>> typeConstructors;
	private Map<Class, List<Supplier<Node>>> terminalConstructors;
	private List<BiFunction<Class, Integer, Node>> genericConstructors;

	public Node createRandomNode(Class type, int depth) throws NoConstructorsSet {
		if (typeConstructors == null || terminalConstructors == null || genericConstructors == null) {
			throw new NoConstructorsSet();
		}
		if (depth <= 0) {
			return createTerminal(type);
		} else {
			return createAnyNode(type, depth);
		}
	}

	private Node createTerminal(Class type) {
		final List<Supplier<Node>> list = terminalConstructors.get(type);
		final int index = randInt(list.size());
		return list.get(index).get();
	}

	private Node createAnyNode(Class type, int depth) {
		final List<Function<Integer, Node>> constructors = typeConstructors.get(type);
		final int total = constructors.size() + genericConstructors.size();
		final int index = randInt(total);
		if (index < constructors.size()) {
			final Function<Integer, Node> c = constructors.get(index);
			return c.apply(depth);
		} else {
			final BiFunction<Class, Integer, Node> c = genericConstructors.get(index - constructors.size());
			return c.apply(type, depth);
		}
	}

	public void setConstructors(Map<Class, List<Function<Integer, Node>>> typeConstructors,
	        Map<Class, List<Supplier<Node>>> terminalConstructors,
	        List<BiFunction<Class, Integer, Node>> genericConstructors) {
		this.typeConstructors = typeConstructors;
		this.terminalConstructors = terminalConstructors;
		this.genericConstructors = genericConstructors;
	}

	private int randInt(int max) {
		final double rand = Math.random();
		return (int) (rand * max);
	}

	private NodeFactory() {
		// private to prevent instantiation
	}

	public static void putTerminalsInNodes(List<Supplier<Node>> terminals, List<Function<Integer, Node>> nodes) {
		for (final Supplier<Node> terminal : terminals) {
			nodes.add(i -> terminal.get());
		}
	}

	public static NodeFactory getInstance() {
		return instance;
	}

}
