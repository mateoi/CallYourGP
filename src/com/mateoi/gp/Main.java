package com.mateoi.gp;

import java.util.ArrayList;
import java.util.List;

import com.mateoi.gp.exceptions.NoConstructorsSet;
import com.mateoi.gp.rules.NoRules;
import com.mateoi.gp.rules.Rules;
import com.mateoi.gp.tree.Node;
import com.mateoi.gp.tree.NodeFactory;

public class Main<T> {

	public static final int POPULATION = 5000;
	public static final int DEPTH = 3;
	public static final int GENERATIONS = 100;
	public static final double CROSSOVER_RATE = 0.9;
	public static final double MUTATION_RATE = 0.015;

	private List<Node<T>> trees = new ArrayList<>();
	private final Class<T> type;
	private final Rules<T> rules;

	public Main(Class<T> type, Rules<T> rules) {
		this.rules = rules;
		this.type = type;
		initializeNodes();
	}

	@SuppressWarnings("unchecked")
	private void initializeNodes() {
		try {
			for (int i = 0; i < POPULATION; i++) {
				this.trees.add(NodeFactory.getInstance().createRandomNode(this.type, DEPTH));
			}
		} catch (final NoConstructorsSet e) {
			System.exit(1);
		}
	}

	public Node<T> run() {
		for (int i = 0; i < GENERATIONS; i++) {
			this.trees = this.rules.nextGeneration(this.trees);
		}
		return this.rules.bestNode(this.trees);
	}

	public static void main(String[] args) {
		final Rules<Double> rules = new NoRules<>();
		final Main<Double> main = new Main<>(Double.class, rules);
		final Node<Double> winner = main.run();
		System.out.println(winner);
	}
}
