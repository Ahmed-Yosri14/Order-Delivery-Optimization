package Fitness;

import Chromosomes.Chromosome;
import java.util.ArrayList;
import java.util.List;

public class FitnessEvaluator {

    private static FitnessEvaluator instance;

    private final ArrayList<ArrayList<Integer>> distanceMatrix;
    private final int timeConstraint;


    private FitnessEvaluator(ArrayList<ArrayList<Integer>> distanceMatrix, int timeConstraint) {
        this.distanceMatrix = distanceMatrix;
        this.timeConstraint = timeConstraint;
    }

    public static synchronized FitnessEvaluator getInstance(ArrayList<ArrayList<Integer>> distanceMatrix, int timeConstraint) {
        if (instance == null) {
            instance = new FitnessEvaluator(distanceMatrix, timeConstraint);
        }
        return instance;
    }

    public static synchronized FitnessEvaluator getInstance() {
        if (instance == null) {
            throw new IllegalStateException("FitnessEvaluator not initialized. Call getInstance(distanceMatrix, timeConstraint) first.");
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        instance = null;
    }
    public double evaluate(Chromosome chromosome) {
        double alpha = 0.01;
        return calculateOnTimeDeliveries(chromosome)-alpha*(calculateTotalRouteTime(chromosome)-timeConstraint);
    }

    public int calculateTotalRouteTime(Chromosome chromosome) {
        List<Integer> seq = chromosome.getDeliverySequence();
        if (seq.isEmpty() || seq.size() == 1) return 0;

        int totalTime = 0;
        for (int i = 0; i < seq.size() - 1; i++) {
            int curr = seq.get(i);
            int next = seq.get(i + 1);
            totalTime += distanceMatrix.get(curr).get(next);
        }
        return totalTime;
    }

    public int calculateOnTimeDeliveries(Chromosome chromosome) {
        List<Integer> seq = chromosome.getDeliverySequence();
        if (seq.isEmpty()) return 0;

        int currentTime = 0;
        int count = 1;

        for (int i = 1; i < seq.size(); i++) {
            int travelTime = distanceMatrix.get(seq.get(i - 1)).get(seq.get(i));
            currentTime += travelTime;

            if (currentTime <= timeConstraint) {
                count++;
            } else {
                break;
            }
        }

        return count;
    }
}
