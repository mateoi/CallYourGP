package com.mateoi.gp.tree;

import java.util.ArrayList;
import java.util.List;

import com.mateoi.gp.tree.functions.ArithmeticNode;
import com.mateoi.gp.tree.functions.BooleanNode;
import com.mateoi.gp.tree.functions.Constant;
import com.mateoi.gp.tree.functions.IfNode;
import com.mateoi.gp.tree.functions.Negate;
import com.mateoi.gp.tree.functions.ReadMemory;
import com.mateoi.gp.tree.functions.WriteMemory;

/**
 * Abstract class for parsers that convert strings to nodes.
 *
 * @author mateo
 *
 */
public abstract class Parser {
    /**
     * Parse the given string and create a Node that matches it, with the given
     * maximum depth. The string must be in the same format as that returned by
     * the {@link Node#toString()} method.
     * 
     * @param s
     * @param depth
     * @return
     */
    public Node parse(String s, int depth) {
        s = s.trim();
        int firstSpace = s.indexOf(' ');
        String name = s.substring(1, firstSpace == -1 ? s.indexOf(')') : firstSpace);
        List<Node> children = new ArrayList<>();
        int bracketLevel = 0;
        int lastOpenBracket = firstSpace;
        if (firstSpace == -1) {
            return toNode(name, depth);
        }
        for (int i = firstSpace; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                bracketLevel++;
                if (bracketLevel == 1) {
                    lastOpenBracket = i;
                }
            } else if (c == ')') {
                bracketLevel--;
                if (bracketLevel == 0) {
                    String child = s.substring(lastOpenBracket, i + 1);
                    Node childAsNode = parse(child, depth - 1);
                    children.add(childAsNode);
                }
            }

        }
        Node asNode = toNode(name, depth);
        asNode.setArguments(children);
        return asNode;
    }

    /**
     * Convert a node name to a node with the given depth
     *
     * @param name
     * @param depth
     * @return
     */
    private Node toNode(String name, int depth) {
        if ("+".equals(name)) {
            return ArithmeticNode.plus(depth);
        } else if ("-".equals(name)) {
            return ArithmeticNode.minus(depth);
        } else if ("*".equals(name)) {
            return ArithmeticNode.times(depth);
        } else if ("/".equals(name)) {
            return ArithmeticNode.div(depth);
        } else if ("and".equals(name)) {
            return BooleanNode.and(depth);
        } else if ("or".equals(name)) {
            return BooleanNode.or(depth);
        } else if ("xor".equals(name)) {
            return BooleanNode.xor(depth);
        } else if ("if".equals(name)) {
            return new IfNode(depth, null, null, null);
        } else if ("neg".equals(name)) {
            return new Negate(depth, null);
        } else if ("Read".equals(name)) {
            return new ReadMemory(depth);
        } else if ("Write".equals(name)) {
            return new WriteMemory(depth);
        } else if (name != null && name.matches("-?\\d+\\.\\d+(E[+-]\\d+)?")) {
            return new Constant(Double.parseDouble(name));
        } else {
            return gameSpecificNode(name, depth);
        }
    }

    /**
     * Parse game-specific nodes
     *
     * @param name
     * @param depth
     * @return
     */
    protected abstract Node gameSpecificNode(String name, int depth);

}
