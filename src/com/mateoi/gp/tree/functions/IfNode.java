package com.mateoi.gp.tree.functions;

import com.mateoi.gp.exceptions.TypeMismatch;
import com.mateoi.gp.tree.Arity3Node;
import com.mateoi.gp.tree.Node;

public class IfNode<T> extends Arity3Node<Boolean, T, T, T> {

	public IfNode(int depth, Class<T> returnType) {
		super("if", depth, returnType, Boolean.class, returnType, returnType,
		        (test, ifTrue, ifFalse) -> test ? ifTrue : ifFalse);
	}

	@SuppressWarnings("unchecked")
	@Override
	public T evaluate() throws TypeMismatch { // Override to shortcut execution
		if (!checkTypes()) {
			throw new TypeMismatch();
		}
		final Node<Boolean> test = arguments.get(0);
		final boolean testValue = test.evaluate();
		final int index = testValue ? 1 : 2;
		final Node<T> result = arguments.get(index);
		return result.evaluate();
	}
}
