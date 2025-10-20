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
        List<Integer> seq = chromosome.getDeliverySequence();
        if (seq.isEmpty()) {
            return 0;
        }
        int total = 0;
        total += distMatrix.get(0).get(seq.get(0));
        for (int i = 0; i < seq.size() - 1; i++) {
            int cur = seq.get(i);
            int nxt = seq.get(i + 1);
            total += distMatrix.get(cur).get(nxt);
        }
        return total;
    }
}
