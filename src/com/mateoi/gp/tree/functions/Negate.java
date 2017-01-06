package com.mateoi.gp.tree.functions;

import java.util.ArrayList;
import java.util.List;

import com.mateoi.gp.tree.Arity1Node;
import com.mateoi.gp.tree.Node;

public class Negate extends Arity1Node {

	public Negate(int depth) {
		super("neg", depth, d -> -d);
		createChildren();
	}
	
	public Negate(int depth, Node child) {
		super("neg", depth, d -> -d);
        List<Node> children = new ArrayList<>();
        children.add(child);
        setArguments(children);
	}

	@Override
	public Node copy() {
		return new Negate(getDepth(), getArguments().get(0).copy());
	}
}
