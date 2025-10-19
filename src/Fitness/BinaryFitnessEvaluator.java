package Fitness;

import Chromosomes.BinaryChromosome;
import Chromosomes.Chromosome;
import java.util.ArrayList;
import java.util.List;

public class BinaryFitnessEvaluator implements FitnessEvaluator {
    private ArrayList<ArrayList<Integer>> distanceMatrix;
    private int timeConstraint;
    private static final int DEPOT = 0;

    public BinaryFitnessEvaluator(ArrayList<ArrayList<Integer>> distanceMatrix, int timeConstraint) {
        this.distanceMatrix = distanceMatrix;
        this.timeConstraint = timeConstraint;
    }

    @Override
    public Integer evaluate(Chromosome chromosome) {
        if (!(chromosome instanceof BinaryChromosome)) {
            throw new IllegalArgumentException("BinaryFitnessEvaluator can only evaluate BinaryChromosome");
        }
        return (Integer) evaluateBinary((BinaryChromosome) chromosome);
    }

    /**
     * Evaluates fitness for a binary chromosome
     * Strategy: Start from first order, try to deliver remaining orders in sequence until time runs out
     * Fitness = number of orders successfully delivered within time constraint
     */
    public int evaluateBinary(BinaryChromosome chromosome) {
        List<Integer> deliverySequence = chromosome.getDeliverySequence();

        if (deliverySequence.isEmpty()) {
            return 0;
        }

        if (deliverySequence.size() == 1) {
            return 1; // Just one order, no travel time
        }

        int currentTime = 0;
        int currentLocation = deliverySequence.get(0); // Start at first order
        int ordersDelivered = 1; // First order is already "delivered" (starting point)

        // Try to deliver remaining orders in sequence
        for (int i = 1; i < deliverySequence.size(); i++) {
            int nextOrder = deliverySequence.get(i);

            // Time to travel from current location to next order
            int travelTime = distanceMatrix.get(currentLocation).get(nextOrder);
            int potentialTime = currentTime + travelTime;

            if (potentialTime <= timeConstraint) {
                // We can deliver this order
                currentTime = potentialTime;
                currentLocation = nextOrder;
                ordersDelivered++;
            } else {
                // Can't deliver this order within time constraint
                break;
            }
        }

        return ordersDelivered;
    }

    /**
     * Calculate the total time for the complete route
     * (for analysis purposes)
     */
    public int calculateTotalRouteTime(BinaryChromosome chromosome) {
        List<Integer> deliverySequence = chromosome.getDeliverySequence();

        if (deliverySequence.isEmpty()) {
            return 0;
        }

        if (deliverySequence.size() == 1) {
            return 0; // Just one order, no travel
        }

        int totalTime = 0;

        // Start from first order, visit remaining orders
        for (int i = 0; i < deliverySequence.size() - 1; i++) {
            int currentOrder = deliverySequence.get(i);
            int nextOrder = deliverySequence.get(i + 1);
            totalTime += distanceMatrix.get(currentOrder).get(nextOrder);
        }

        return totalTime;
    }

    /**
     * Get the maximum number of orders that can be delivered within time constraint
     */
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