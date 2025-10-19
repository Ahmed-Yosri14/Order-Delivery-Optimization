package Fitness;

import Chromosomes.BinaryChromosome;
import Chromosomes.Chromosome;
import java.util.ArrayList;
import java.util.List;

public class BinaryFitnessEvaluator implements FitnessEvaluator {

    private static BinaryFitnessEvaluator instance;

    private ArrayList<ArrayList<Integer>> distanceMatrix;
    private int timeConstraint;
    private static final int DEPOT = 0;

    private BinaryFitnessEvaluator(ArrayList<ArrayList<Integer>> distanceMatrix, int timeConstraint) {
        this.distanceMatrix = distanceMatrix;
        this.timeConstraint = timeConstraint;
    }

    public static synchronized BinaryFitnessEvaluator getInstance(ArrayList<ArrayList<Integer>> distanceMatrix, int timeConstraint) {
        if (instance == null) {
            instance = new BinaryFitnessEvaluator(distanceMatrix, timeConstraint);
        }
        return instance;
    }

    public static synchronized BinaryFitnessEvaluator getInstance() {
        if (instance == null) {
            throw new IllegalStateException("BinaryFitnessEvaluator not initialized. Call getInstance(distanceMatrix, timeConstraint) first.");
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        instance = null;
    }

    @Override
    public Integer evaluate(Chromosome chromosome) {
        if (!(chromosome instanceof BinaryChromosome)) {
            throw new IllegalArgumentException("BinaryFitnessEvaluator can only evaluate BinaryChromosome");
        }
        return evaluateBinary((BinaryChromosome) chromosome);
    }

    public int evaluateBinary(BinaryChromosome chromosome) {
        List<Integer> deliverySequence = chromosome.getDeliverySequence();

        if (deliverySequence.isEmpty()) return 0;
        if (deliverySequence.size() == 1) return 1;

        int currentTime = 0;
        int currentLocation = deliverySequence.get(0);
        int ordersDelivered = 1;

        for (int i = 1; i < deliverySequence.size(); i++) {
            int nextOrder = deliverySequence.get(i);
            int travelTime = distanceMatrix.get(currentLocation).get(nextOrder);
            int potentialTime = currentTime + travelTime;

            if (potentialTime <= timeConstraint) {
                currentTime = potentialTime;
                currentLocation = nextOrder;
                ordersDelivered++;
            } else {
                break;
            }
        }

        return ordersDelivered;
    }
    @Override
    public int calculateTotalRouteTime(Chromosome chromosome) {
        List<Integer> deliverySequence = chromosome.getDeliverySequence();

        if (deliverySequence.isEmpty() || deliverySequence.size() == 1) {
            return 0;
        }

        int totalTime = 0;
        for (int i = 0; i < deliverySequence.size() - 1; i++) {
            int currentOrder = deliverySequence.get(i);
            int nextOrder = deliverySequence.get(i + 1);
            totalTime += distanceMatrix.get(currentOrder).get(nextOrder);
        }

        return totalTime;
    }

    public int getMaxDeliverableOrders(BinaryChromosome chromosome) {
        return evaluateBinary(chromosome);
    }

    public void setTimeConstraint(int timeConstraint) {
        this.timeConstraint = timeConstraint;
    }

    public int getTimeConstraint() {
        return timeConstraint;
    }
}
