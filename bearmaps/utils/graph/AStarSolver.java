package bearmaps.utils.graph;

import bearmaps.utils.pq.DoubleMapPQ;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.*;

public class AStarSolver<Vertex> implements ShortestPathsSolver<Vertex> {
    DoubleMapPQ<Vertex> fringe;
    HashMap<Vertex, Double> distances;
    HashMap<Vertex, Vertex> predecessors;
    int numStatesExplored;
    List<Vertex> solution;
    SolverOutcome outcome;
    double solutionWeight;
    double explorationTime;

    public AStarSolver(AStarGraph<Vertex> input, Vertex start, Vertex end, double timeout) {
        Stopwatch sw = new Stopwatch();
        distances = new HashMap<>();
        predecessors = new HashMap<>();
        fringe = new DoubleMapPQ<>();
        numStatesExplored = 0;
        distances.put(start, 0.0);
        explorationTime = sw.elapsedTime();
        predecessors.put(start, start);
        fringe.insert(start,
                distances.get(start) + input.estimatedDistanceToGoal(start, end));
        while (fringe.size() != 0 && !fringe.peek().equals(end) && explorationTime < timeout) {
            Vertex curr = fringe.poll();
            numStatesExplored++;
            for (WeightedEdge<Vertex> edge : input.neighbors(curr)) {
                Vertex p = edge.from();
                Vertex q = edge.to();
                double weight = edge.weight();
                if (!distances.containsKey(q) || (distances.get(p) + weight < distances.get(q))) {
                    distances.put(q, distances.get(p) + weight);
                    predecessors.put(q, p);
                    if (fringe.contains(q)) {
                        fringe.changePriority(q,
                                distances.get(q) + input.estimatedDistanceToGoal(q, end));
                    }
                    else {
                        fringe.insert(q,
                                distances.get(q) + input.estimatedDistanceToGoal(q, end));
                    }
                }
            }
            explorationTime = sw.elapsedTime();
        }
        solution = new ArrayList<>();
        solutionWeight = 0;
        if (explorationTime > timeout) {
            outcome = SolverOutcome.TIMEOUT;
            return;
        }
        if (!distances.containsKey(end)) {
            outcome = SolverOutcome.UNSOLVABLE;
            return;
        }
        solutionWeight = distances.get(end);
        Vertex prev = end;
        while (!prev.equals(start)) {
            solution.add(0, prev);
            prev = predecessors.get(prev);
        }
        solution.add(0, start);
        outcome = SolverOutcome.SOLVED;
    }


    @Override
    public SolverOutcome outcome() {
        return outcome;
    }

    @Override
    public List<Vertex> solution() {
        return solution;
    }

    @Override
    public double solutionWeight() {
        return solutionWeight;
    }

    @Override
    public int numStatesExplored() {
        return numStatesExplored;
    }

    @Override
    public double explorationTime() {
        return explorationTime;
    }
}