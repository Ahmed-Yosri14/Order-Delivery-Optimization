package Fitness;

import Chromosomes.Chromosome;
import java.util.ArrayList;
import java.util.List;

public class IntegerFitnessEvaluator implements FitnessEvaluator {

    private static IntegerFitnessEvaluator instance;

    private ArrayList<ArrayList<Integer>> distMatrix;
    private int timeConstraint;

    private IntegerFitnessEvaluator(ArrayList<ArrayList<Integer>> distMatrix, int timeConstraint) {
        this.distMatrix = distMatrix;
        this.timeConstraint = timeConstraint;
    }

    public static synchronized IntegerFitnessEvaluator getInstance(ArrayList<ArrayList<Integer>> distMatrix, int timeConstraint) {
        if (instance == null) {
            instance = new IntegerFitnessEvaluator(distMatrix, timeConstraint);
        }
        return instance;
    }

    public static synchronized IntegerFitnessEvaluator getInstance() {
        if (instance == null) {
            throw new IllegalStateException("IntegerFitnessEvaluator not initialized. Call getInstance(distMatrix, timeConstraint) first.");
        }
        return instance;
    }

    @Override
    public Integer evaluate(Chromosome chromosome) {
        int fitness = 0;
        List<Integer> seq = chromosome.getDeliverySequence();
        int prev = 0, prevTime = 0;

        for (int next : seq) {
            int nextTime = distMatrix.get(prev).get(next) + prevTime;
            if (nextTime > timeConstraint) {
                return fitness;
            } else {
                fitness++;
                prevTime = nextTime;
                prev = next;
            }
        }
        return fitness;
    }

    @Override
    public int calculateTotalRouteTime(Chromosome chromosome) {
        return 0;
    }
}
