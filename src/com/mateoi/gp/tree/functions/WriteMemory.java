package com.mateoi.gp.tree.functions;

import java.util.ArrayList;
import java.util.List;

import com.mateoi.gp.memory.Memory;
import com.mateoi.gp.tree.Arity2Node;
import com.mateoi.gp.tree.Node;

/**
 * A node that writes to indexed memory using the current memory set in the
 * Memory class
 *
 * @author mateo
 *
 */
public class WriteMemory extends Arity2Node {

    /**
     * Create a new WriteMemory node with the given maximum depth and give it
     * randomly generated children.
     *
     * @param depth
     */
    public WriteMemory(int depth) {
        super("Write", depth, (indexD, value) -> {
            Memory memory = Memory.getMemorySupplier().get();
            return memory.put(indexD.intValue(), value);
        });
        createChildren();
    }

    /**
     * Create a new WriteMemory node with the given maximum depth and children.
     * 
     * @param depth
     * @param left
     * @param right
     */
    public WriteMemory(int depth, Node left, Node right) {
        super("Write", depth, (indexD, value) -> {
            Memory memory = Memory.getMemorySupplier().get();
            return memory.put(indexD.intValue(), value);
        });
        List<Node> children = new ArrayList<>();
        children.add(left);
        children.add(right);
        setArguments(children);
    }

    @Override
    public Node copy() {
        Node left = getArguments().get(0).copy();
        Node right = getArguments().get(1).copy();
        return new WriteMemory(getDepth(), left, right);
    }

}
