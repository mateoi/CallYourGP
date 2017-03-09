package com.mateoi.gp.rules;

import java.util.List;
import java.util.Map;

import com.mateoi.gp.tree.Node;

/**
 * The Rules interface defines selection rules for GP evolution. That is, how
 * nodes should be selected based on their fitness value
 *
 * @author mateo
 *
 */
public interface Rules {
    /**
     * Grade each node according to the selection procedure; Nodes will be
     * selected proportionally according to this score.
     *
     * @param trees
     * @return
     */
    public Map<Node, Double> score(List<Node> trees);

    /**
     * Get the best n nodes from the given list
     * 
     * @param trees
     * @param n
     * @return
     */
    public List<Node> bestNodes(List<Node> trees, int n);
}
