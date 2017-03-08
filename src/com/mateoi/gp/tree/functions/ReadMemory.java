package com.mateoi.gp.tree.functions;

import java.util.ArrayList;
import java.util.List;

import com.mateoi.gp.memory.Memory;
import com.mateoi.gp.tree.Arity1Node;
import com.mateoi.gp.tree.Node;

/**
 * A node that reads from indexed memory using the current memory set in the
 * Memory class
 *
 * @author mateo
 *
 */
public class ReadMemory extends Arity1Node {

    /**
     * Create a new ReadMemory node with the given maximum depth and give it a
     * randomly generated child.
     *
     * @param depth
     */
    public ReadMemory(int depth) {
        super("Read", depth, indexD -> {
            Memory memory = Memory.getMemorySupplier().get();
            return memory.get(indexD.intValue());
        });
        createChildren();
    }

    /**
     * Create a new ReadMemory node with the given maximum depth and child.
     *
     * @param depth
     * @param left
     * @param right
     */
    public ReadMemory(int depth, Node child) {
        super("Read", depth, indexD -> {
            Memory memory = Memory.getMemorySupplier().get();
            return memory.get(indexD.intValue());
        });
        List<Node> children = new ArrayList<>();
        children.add(child);
        setArguments(children);
    }

    @Override
    public Node copy() {
        Node child = getArguments().get(0).copy();
        return new ReadMemory(getDepth(), child);
    }
}
