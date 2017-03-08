package com.mateoi.gp.tree;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import com.mateoi.gp.exceptions.NoConstructorsSet;

/**
 * Singleton class that holds constructors for all types of nodes.
 *
 * @author mateo
 *
 */
public class NodeFactory {

    /** Singleton class instance */
    private static final NodeFactory instance = new NodeFactory();

    private Map<Integer, List<Function<Integer, Node>>> constructors;
    /** List of nodes of arity 0 */
    // private List<Supplier<Node>> terminals;
    /** List of nodes of arity 1 */
    // private List<Function<Integer, Node>> arity1;
    /** List of nodes of arity 2 */
    // private List<Function<Integer, Node>> arity2;
    /** List of nodes of arity 3 */
    // private List<Function<Integer, Node>> arity3;
    /** List of nodes of arity 1, 2 and 3 */
    // private List<Function<Integer, Node>> functions;

    /**
     * Create a random node with the given depth.
     *
     * @param depth
     * @return
     * @throws NoConstructorsSet
     */
    public Node createRandomNode(int depth) throws NoConstructorsSet {
        if (constructors == null) {
            throw new NoConstructorsSet();
        }
        if (depth <= 0) {
            return createByArity(0);
        } else {
            int index = randInt(countConstructors());
            Function<Integer, Node> constructor = getConstructorByIndex(index);
            return constructor.apply(depth);
        }
    }

    /**
     * Create a random non-terminal node of the given depth (although may create
     * a terminal if the depth is less than or equal to zero)
     *
     * @param depth
     * @return
     * @throws NoConstructorsSet
     */
    public Node createFunction(int depth) throws NoConstructorsSet {
        if (constructors == null) {
            throw new NoConstructorsSet();
        }
        if (depth <= 0) {
            return createByArity(0);
        } else {
            int index = randInt(countNonTerminalConstructors());
            Function<Integer, Node> c = getNonTerminalConstructorByIndex(index);
            return c.apply(depth);
        }
    }

    /**
     * Create a node of the given arity.
     *
     * @param arity
     * @return
     * @throws NoConstructorsSet
     */
    public Node createByArity(int arity) throws NoConstructorsSet {
        if (constructors == null || !constructors.containsKey(0)) {
            throw new NoConstructorsSet();
        }
        List<Function<Integer, Node>> arityConstructors = constructors.get(arity);
        int index = randInt(arityConstructors.size());
        return arityConstructors.get(index).apply(0);
    }

    /**
     * Set the available constructors for the factory
     *
     * @param constructors
     */
    public void setConstructors(Map<Integer, List<Function<Integer, Node>>> constructors) {
        this.constructors = constructors;
    }

    /**
     * Create a random integer of between 0 and max
     *
     * @param max
     * @return
     */
    private int randInt(int max) {
        double rand = Math.random();
        return (int) (rand * max);
    }

    /**
     * Calculate the total number of constructors
     *
     * @return
     */
    private int countConstructors() {
        int total = 0;
        for (Entry<Integer, List<Function<Integer, Node>>> e : constructors.entrySet()) {
            total += e.getValue().size();
        }
        return total;
    }

    /**
     * Calculate the total number of non-terminal constructors
     *
     * @return
     */
    private int countNonTerminalConstructors() {
        int total = 0;
        for (Entry<Integer, List<Function<Integer, Node>>> e : constructors.entrySet()) {
            if (e.getKey() != 0) {
                total += e.getValue().size();
            }
        }
        return total;
    }

    /**
     * Get the given constructor by index.
     *
     * @param index
     * @return
     */
    private Function<Integer, Node> getConstructorByIndex(int index) {
        for (Entry<Integer, List<Function<Integer, Node>>> e : constructors.entrySet()) {
            if (index < e.getValue().size()) {
                return e.getValue().get(index);
            } else {
                index -= e.getValue().size();
            }
        }
        return null;
    }

    /**
     * Get the given non-terminal constructor by index.
     *
     * @param index
     * @return
     */
    private Function<Integer, Node> getNonTerminalConstructorByIndex(int index) {
        for (Entry<Integer, List<Function<Integer, Node>>> e : constructors.entrySet()) {
            if (e.getKey() != 0) {
                if (index < e.getValue().size()) {
                    return e.getValue().get(index);
                } else {
                    index -= e.getValue().size();
                }
            }
        }
        return null;
    }

    private NodeFactory() {
        // private to prevent instantiation
    }

    /**
     * @return The singleton instance of the NodeFactory class.
     */
    public static NodeFactory getInstance() {
        return instance;
    }
}
