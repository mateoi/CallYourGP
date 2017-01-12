package com.mateoi.gp.tree.functions;

import com.mateoi.gp.memory.Memory;
import com.mateoi.gp.tree.Arity1Node;
import com.mateoi.gp.tree.Node;

public class ReadMemory extends Arity1Node {

    public ReadMemory(int depth) {
        super("Read", depth, indexD -> {
            Memory memory = Memory.getMemorySupplier().get();
            return memory.get(indexD.intValue());
        });
    }

    @Override
    public Node copy() {
        return new ReadMemory(getDepth());
    }
}
