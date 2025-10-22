import Chromosomes.Chromosome;
import Fitness.*;
import Selection.*;
import Replacement.*;

import java.util.ArrayList;
import java.util.Random;

/**
 * Case Study: Order Delivery Optimization using Genetic Algorithm
 * 
 * Problem Description:
 * A delivery company needs to optimize the sequence of delivering orders to multiple locations.
 * The goal is to maximize the number of orders delivered within a time constraint.
 * 
 * Constraints:
 * - Time limit for all deliveries
 * - Must start from depot (location 0)
 * - Travel time between locations varies
 * 
 * Solution Approach:
 * Using Genetic Algorithm to find the optimal delivery sequence that maximizes
 * the number of orders delivered within the time constraint.
 */
public class CaseStudyDemo {
    
    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("   CASE STUDY: ORDER DELIVERY OPTIMIZATION");
        System.out.println("================================================================\n");
        
        // ==================== Problem Setup ====================
        
        // Problem parameters
        int numberOfDeliveryPoints = 8;  // Excluding depot
        int timeConstraint = 200;        // Maximum total delivery time
        
        System.out.println("Problem Configuration:");
        System.out.println("  Number of delivery points: " + numberOfDeliveryPoints);
        System.out.println("  Time constraint: " + timeConstraint + " units");
        
        // Generate distance matrix (travel times between locations)
        ArrayList<ArrayList<Integer>> distanceMatrix = generateDistanceMatrix(numberOfDeliveryPoints + 1);
        
        System.out.println("\nDistance Matrix (Travel Times):");
        printDistanceMatrix(distanceMatrix);
        
        // ==================== Fitness Function Definition ====================
        
        // Initialize the fitness evaluator (problem-dependent)
        IntegerFitnessEvaluator.getInstance(distanceMatrix, timeConstraint);
        
        // Define the fitness function using the interface
        FitnessEvaluator fitnessFunction = IntegerFitnessEvaluator.getInstance();
        
        // ==================== Configure Genetic Algorithm ====================
        
        System.out.println("\n=== Configuring Genetic Algorithm ===");
        
        // Create and configure the GA engine
        GeneticAlgorithm ga_engine = new GeneticAlgorithm();
        
        // Set GA parameters
        ga_engine.setPopulationSize(50);
        ga_engine.setChromosomeLength(numberOfDeliveryPoints);
        ga_engine.setFitnessFunction(fitnessFunction);
        ga_engine.setChromosomeType(GeneticAlgorithm.ChromosomeType.INTEGER);
        
        // Set genetic operators parameters
        ga_engine.setCrossoverRate(0.7);
        ga_engine.setMutationRate(0.02);
        ga_engine.setGenerations(100);
        
        // Optional: Customize selection and replacement strategies
        ga_engine.setSelectionMethod(new TournamentSelection(3));
        ga_engine.setReplacementStrategy(new ElitistReplacement(2));
        
        System.out.println("GA Configuration:");
        System.out.println("  Population Size: " + ga_engine.getPopulationSize());
        System.out.println("  Chromosome Length: " + ga_engine.getChromosomeLength());
        System.out.println("  Crossover Rate: " + ga_engine.getCrossoverRate());
        System.out.println("  Mutation Rate: " + ga_engine.getMutationRate());
        System.out.println("  Generations: " + ga_engine.getGenerations());
        
        // ==================== Run the Algorithm ====================
        
        ga_engine.run();
        
        // ==================== Get and Display Results ====================
        
        Chromosome bestSolution = ga_engine.getBestSolution();
        
        System.out.println("\n================================================================");
        System.out.println("                    FINAL SOLUTION");
        System.out.println("================================================================");
        System.out.println("Best Delivery Sequence: " + bestSolution.getDeliverySequence());
        System.out.println("Number of Orders Delivered: " + bestSolution.getFitness());
        System.out.println("Total Route Time: " + bestSolution.getTotalRouteTime() + " units");
        System.out.println("Time Constraint: " + timeConstraint + " units");
        System.out.println("Feasible: " + (bestSolution.getTotalRouteTime() <= timeConstraint ? "Yes" : "No"));
        
        // Show detailed route information
        System.out.println("\n=== Detailed Route Information ===");
        printDetailedRoute(bestSolution, distanceMatrix);
        
        // Print detailed statistics
        ga_engine.printStatistics();
        
        // ==================== Analysis ====================
        
        System.out.println("=== Analysis ===");
        System.out.println("The genetic algorithm successfully found a delivery sequence that");
        System.out.println("maximizes the number of orders delivered within the time constraint.");
        System.out.println("The solution handles the infeasibility constraint by only counting");
        System.out.println("orders that can be delivered within the time limit.");
        
        System.out.println("\n================================================================");
        System.out.println("           CASE STUDY DEMONSTRATION COMPLETED");
        System.out.println("================================================================");
    }
    
    /**
     * Generate a symmetric distance matrix with random values
     */
    private static ArrayList<ArrayList<Integer>> generateDistanceMatrix(int n) {
        ArrayList<ArrayList<Integer>> distanceMatrix = new ArrayList<>();
        Random rand = new Random(42); // Fixed seed for reproducibility
        
        for (int i = 0; i < n; i++) {
            distanceMatrix.add(new ArrayList<>());
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    distanceMatrix.get(i).add(0);
                } else if (j < i) {
                    distanceMatrix.get(i).add(distanceMatrix.get(j).get(i));
                } else {
                    int distance = rand.nextInt(40) + 10; // Random distance between 10-50
                    distanceMatrix.get(i).add(distance);
                }
            }
        }
        return distanceMatrix;
    }
    
    /**
     * Print the distance matrix in a formatted way
     */
    private static void printDistanceMatrix(ArrayList<ArrayList<Integer>> matrix) {
        System.out.print("     ");
        for (int i = 0; i < matrix.size(); i++) {
            System.out.printf("%4s", (i == 0 ? "D" : i));
        }
        System.out.println();
        
        for (int i = 0; i < matrix.size(); i++) {
            System.out.printf("%4s ", (i == 0 ? "D" : i));
            for (int j = 0; j < matrix.get(i).size(); j++) {
                System.out.printf("%4d", matrix.get(i).get(j));
            }
            System.out.println();
        }
    }
    
    /**
     * Print detailed route information including step-by-step travel times
     */
    private static void printDetailedRoute(Chromosome solution, ArrayList<ArrayList<Integer>> distanceMatrix) {
        var sequence = solution.getDeliverySequence();
        
        if (sequence.isEmpty()) {
            System.out.println("No deliveries in sequence");
            return;
        }
        
        System.out.println("Route: Depot -> " + String.join(" -> ", 
            sequence.stream().map(String::valueOf).toArray(String[]::new)));
        
        System.out.println("\nStep-by-step breakdown:");
        int currentLocation = 0; // Start at depot
        int cumulativeTime = 0;
        
        for (int i = 0; i < sequence.size(); i++) {
            int nextLocation = sequence.get(i);
            int travelTime = distanceMatrix.get(currentLocation).get(nextLocation);
            cumulativeTime += travelTime;
            
            System.out.printf("  Step %d: Location %d -> Location %d | Travel time: %d | Cumulative: %d%n",
                i + 1, currentLocation, nextLocation, travelTime, cumulativeTime);
            
            currentLocation = nextLocation;
        }
        
        System.out.println("Total travel time: " + cumulativeTime + " units");
    }
}

