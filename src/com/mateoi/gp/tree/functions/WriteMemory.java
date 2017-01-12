package com.mateoi.gp.tree.functions;

import com.mateoi.gp.memory.Memory;
import com.mateoi.gp.tree.Arity2Node;
import com.mateoi.gp.tree.Node;

public class WriteMemory extends Arity2Node {

    public WriteMemory(int depth) {
        super("Write", depth, (indexD, value) -> {
            Memory memory = Memory.getMemorySupplier().get();
            return memory.put(indexD.intValue(), value);
        });
    }

    @Override
    public Node copy() {
        return new WriteMemory(getDepth());
    }

}
