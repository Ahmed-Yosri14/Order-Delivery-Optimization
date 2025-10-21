import Chromosomes.*;
import Crossover.*;
import Fitness.*;
import Selection.*;

import java.util.*;

/**
 * Genetic Algorithm for Order Delivery Optimization
 */
public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("================================================================");
        System.out.println("    GENETIC ALGORITHM FOR ORDER DELIVERY OPTIMIZATION");
        System.out.println("================================================================");

        System.out.println("\n=== PROBLEM SETUP ===");
        
        System.out.println("Choose Chromosome Type:");
        System.out.println("1 - Binary (Matrix representation)");
        System.out.println("2 - Integer (Direct sequence)");
        System.out.println("3 - Floating Point (Continuous values)");
        int type = sc.nextInt();

        System.out.println("Enter number of delivery points (excluding depot):");
        int numOrders = sc.nextInt();

        System.out.println("Enter population size:");
        int popSize = sc.nextInt();

        System.out.println("Enter number of generations:");
        int generations = sc.nextInt();

        System.out.println("Enter crossover probability (0.0 - 1.0):");
        double crossoverProb = sc.nextDouble();

        System.out.println("Enter mutation probability (0.0 - 1.0):");
        double mutationProb = sc.nextDouble();

        System.out.println("Enter time constraint (total delivery time limit):");
        int timeConstraint = sc.nextInt();

        // Generate distance matrix
        int n = numOrders + 1; // +1 for depot
        ArrayList<ArrayList<Integer>> distanceMatrix = getDistanceBetweenAllPoints(n);
        printDistanceMatrix(distanceMatrix);

        System.out.println("\n=== INITIALIZATION ===");
        
        // Initialize all fitness evaluators with the same problem parameters
        BinaryFitnessEvaluator.getInstance(distanceMatrix, timeConstraint);
        IntegerFitnessEvaluator.getInstance(distanceMatrix, timeConstraint);
        FloatingPointFitnessEvaluator.getInstance(distanceMatrix, timeConstraint);
        
        // Display chromosome type information
        if (type == 1) {
            System.out.println("Using Binary Chromosome representation with matrix encoding");
        } else if (type == 2) {
            System.out.println("Using Integer Chromosome representation with direct sequence");
        } else {
            System.out.println("Using Floating Point Chromosome representation");
        }

        // Use Initializer class for population creation
        Initializer initializer = new Initializer();
        List<Chromosome> population = initializer.init(type, numOrders, popSize);
        System.out.println("Population initialized with " + population.size() + " individuals");

        System.out.println("\n=== FITNESS EVALUATION ===");
        
        // Evaluate initial population fitness
        for (Chromosome chromosome : population) {
            chromosome.getFitness(); // This triggers fitness evaluation
        }
        
        Chromosome initialBest = findBest(population);
        System.out.println("Initial best fitness: " + initialBest.getFitness());
        System.out.println("Initial best sequence: " + initialBest.getDeliverySequence());

        System.out.println("\n=== SELECTION METHOD ===");
        
        Crossover crossover;
        if (type == 1) {
            crossover = new OrderOneCrossover();
        } else if (type == 2) {
            crossover = IntegerCrossover.getInstance();
        } else {
            crossover = new FloatingPointUniformCrossover();
        }

        System.out.println("Choose Selection Method:");
        System.out.println("1 - Tournament Selection");
        System.out.println("2 - Roulette Wheel Selection");
        int selType = sc.nextInt();

        Selection selection;
        if (selType == 1) {
            System.out.println("Enter tournament size:");
            int tSize = sc.nextInt();
            selection = new TournamentSelection(tSize);
        } else {
            selection = new RouletteWheelSelection();
        }
        
        // Replacement Strategy
        System.out.println("\nChoose Replacement Strategy:");
        System.out.println("1 - Generational (Complete replacement)");
        System.out.println("2 - Steady-State (K parents replaced)");
        System.out.println("3 - Elitist (Keep best individuals)");
        int replaceChoice = sc.nextInt();
        
        // Note: Replacement strategies are available but not used in the current implementation
        // The main loop uses elite preservation instead
        if (replaceChoice == 1) {
            System.out.println("Generational replacement selected (using elite preservation)");
        } else if (replaceChoice == 2) {
            System.out.println("Enter K (number of parents to replace):");
            sc.nextInt(); // consume input
            System.out.println("Steady-state replacement selected (using elite preservation)");
        } else {
            System.out.println("Enter number of elite individuals:");
            sc.nextInt(); // consume input
            System.out.println("Elitist replacement selected (using elite preservation)");
        }

        // ========================================
        // PHASE 5: CROSSOVER OPERATORS DEMONSTRATION
        // ========================================
        System.out.println("\n=== PHASE 5: CROSSOVER OPERATORS DEMONSTRATION ===");
        System.out.println("Crossover probability: " + crossoverProb);
        
        // Demonstrate crossover with sample parents
        Chromosome demoParent1 = population.get(0).clone();
        Chromosome demoParent2 = population.get(1).clone();
        
        System.out.println("\n--- CROSSOVER DEMONSTRATION ---");
        System.out.println("Parent 1: " + demoParent1.getDeliverySequence() + " (Fitness: " + demoParent1.getFitness() + ")");
        System.out.println("Parent 2: " + demoParent2.getDeliverySequence() + " (Fitness: " + demoParent2.getFitness() + ")");
        
        List<Chromosome> crossoverResult = crossover.crossover(demoParent1, demoParent2, crossoverProb);
        System.out.println("Crossover Result:");
        for (int i = 0; i < crossoverResult.size(); i++) {
            System.out.println("  Child " + (i+1) + ": " + crossoverResult.get(i).getDeliverySequence() + 
                             " (Fitness: " + crossoverResult.get(i).getFitness() + ")");
        }

        // ========================================
        // PHASE 6: MUTATION OPERATORS DEMONSTRATION
        // ========================================
        System.out.println("\n=== PHASE 6: MUTATION OPERATORS DEMONSTRATION ===");
        System.out.println("Mutation probability: " + mutationProb);
        
        // Demonstrate mutation with sample chromosome
        Chromosome demoChromosome = population.get(2).clone();
        System.out.println("\n--- MUTATION DEMONSTRATION ---");
        System.out.println("Before Mutation: " + demoChromosome.getDeliverySequence() + " (Fitness: " + demoChromosome.getFitness() + ")");
        
        demoChromosome.mutateMethod1(mutationProb);
        System.out.println("After Mutation: " + demoChromosome.getDeliverySequence() + " (Fitness: " + demoChromosome.getFitness() + ")");
        
        // Demonstrate selection
        System.out.println("\n--- SELECTION DEMONSTRATION ---");
        System.out.println("Population sample for selection:");
        for (int i = 0; i < Math.min(5, population.size()); i++) {
            System.out.println("  Individual " + (i+1) + ": " + population.get(i).getDeliverySequence() + 
                             " (Fitness: " + population.get(i).getFitness() + ")");
        }
        
        Chromosome selectedParent1 = selection.select(population);
        Chromosome selectedParent2 = selection.select(population);
        System.out.println("Selected Parent 1: " + selectedParent1.getDeliverySequence() + " (Fitness: " + selectedParent1.getFitness() + ")");
        System.out.println("Selected Parent 2: " + selectedParent2.getDeliverySequence() + " (Fitness: " + selectedParent2.getFitness() + ")");

        // ========================================
        // PHASE 7: EVOLUTIONARY LOOP
        // ========================================
        System.out.println("\n=== PHASE 7: EVOLUTIONARY LOOP ===");
        
        Chromosome bestOverall = initialBest.clone();
        List<Double> fitnessHistory = new ArrayList<>();
        List<Integer> generationStats = new ArrayList<>();

        for (int gen = 0; gen < generations; gen++) {
            List<Chromosome> newPopulation = new ArrayList<>();

            // Elite preservation - keep best individual
            Chromosome best = findBest(population);
            newPopulation.add(best.clone());

            // Show detailed operations for first few generations
            boolean showDetails = (gen < 3) || (gen + 1) % 10 == 0 || gen == generations - 1;
            
            if (showDetails) {
                System.out.println("\n--- GENERATION " + (gen + 1) + " OPERATIONS ---");
                System.out.println("Elite preserved: " + best.getDeliverySequence() + " (Fitness: " + best.getFitness() + ")");
            }

            int operationCount = 0;
            // Create new population through selection, crossover, and mutation
            while (newPopulation.size() < popSize) {
                // Selection phase
                Chromosome parent1 = selection.select(population);
                Chromosome parent2 = selection.select(population);

                if (showDetails && operationCount < 3) {
                    System.out.println("Operation " + (operationCount + 1) + ":");
                    System.out.println("  Selected Parent 1: " + parent1.getDeliverySequence() + " (Fitness: " + parent1.getFitness() + ")");
                    System.out.println("  Selected Parent 2: " + parent2.getDeliverySequence() + " (Fitness: " + parent2.getFitness() + ")");
                }

                // Crossover phase
                List<Chromosome> children = crossover.crossover(parent1, parent2, crossoverProb);
                
                if (showDetails && operationCount < 3) {
                    System.out.println("  Crossover Result:");
                    for (int i = 0; i < children.size(); i++) {
                        System.out.println("    Child " + (i+1) + " (before mutation): " + children.get(i).getDeliverySequence());
                    }
                }
                
                // Mutation phase
                for (Chromosome child : children) {
                    if (showDetails && operationCount < 3) {
                        System.out.println("    Before Mutation: " + child.getDeliverySequence());
                    }
                    child.mutateMethod1(mutationProb);
                    if (showDetails && operationCount < 3) {
                        System.out.println("    After Mutation: " + child.getDeliverySequence() + " (Fitness: " + child.getFitness() + ")");
                    }
                    newPopulation.add(child);
                    if (newPopulation.size() >= popSize) break;
                }
                
                operationCount++;
                if (showDetails && operationCount >= 3) {
                    System.out.println("  ... (showing first 3 operations, continuing with " + (popSize - newPopulation.size()) + " more operations)");
                    showDetails = false; // Stop showing details for this generation
                }
            }

            population = newPopulation;

            // Track best solution
            Chromosome currentBest = findBest(population);
            if (currentBest.getFitness() > bestOverall.getFitness()) {
                bestOverall = currentBest.clone();
            }

            // Record statistics
            fitnessHistory.add((double) currentBest.getFitness());
            generationStats.add(gen + 1);

            // Progress reporting
            if ((gen + 1) % 10 == 0 || gen == 0 || gen == generations - 1) {
                System.out.println("\nGeneration " + (gen + 1) + " Summary:");
                System.out.println("  Best Fitness: " + currentBest.getFitness());
                System.out.println("  Best Sequence: " + currentBest.getDeliverySequence());
                System.out.println("  Population Average Fitness: " + String.format("%.2f", 
                    population.stream().mapToInt(Chromosome::getFitness).average().orElse(0.0)));
            }
        }

        // ========================================
        // PHASE 8: RESULTS ANALYSIS & REPORTING
        // ========================================
        System.out.println("\n=== PHASE 8: RESULTS ANALYSIS & REPORTING ===");
        
        System.out.println("\n================================================================");
        System.out.println("                    FINAL RESULTS");
        System.out.println("================================================================");
        System.out.println("Best Fitness Achieved: " + bestOverall.getFitness());
        System.out.println("Best Delivery Sequence: " + bestOverall.getDeliverySequence());
        System.out.println("Total Route Time: " + bestOverall.getTotalRouteTime());
        System.out.println("Time Constraint: " + timeConstraint);
        
        // Performance statistics
        double avgFitness = fitnessHistory.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double maxFitness = fitnessHistory.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
        double minFitness = fitnessHistory.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
        
        System.out.println("\n=== PERFORMANCE STATISTICS ===");
        System.out.println("Average Fitness: " + String.format("%.2f", avgFitness));
        System.out.println("Maximum Fitness: " + String.format("%.2f", maxFitness));
        System.out.println("Minimum Fitness: " + String.format("%.2f", minFitness));
        System.out.println("Fitness Improvement: " + (bestOverall.getFitness() - initialBest.getFitness()));
        
        System.out.println("\n=== ALGORITHM CONFIGURATION USED ===");
        System.out.println("Chromosome Type: " + (type == 1 ? "Binary" : type == 2 ? "Integer" : "Floating Point"));
        System.out.println("Population Size: " + popSize);
        System.out.println("Generations: " + generations);
        System.out.println("Crossover Probability: " + crossoverProb);
        System.out.println("Mutation Probability: " + mutationProb);
        System.out.println("Selection Method: " + (selType == 1 ? "Tournament" : "Roulette Wheel"));
        
        // Final algorithm demonstration
        System.out.println("\n=== FINAL ALGORITHM DEMONSTRATION ===");
        System.out.println("Demonstrating the complete genetic algorithm workflow:");
        
        // Show final population sample
        System.out.println("\nFinal Population Sample (top 5 individuals):");
        List<Chromosome> sortedPopulation = new ArrayList<>(population);
        sortedPopulation.sort((a, b) -> Integer.compare(b.getFitness(), a.getFitness()));
        
        for (int i = 0; i < Math.min(5, sortedPopulation.size()); i++) {
            Chromosome c = sortedPopulation.get(i);
            System.out.println("  Rank " + (i+1) + ": " + c.getDeliverySequence() + 
                             " (Fitness: " + c.getFitness() + ", Route Time: " + c.getTotalRouteTime() + ")");
        }
        
        // Show algorithm effectiveness
        System.out.println("\nAlgorithm Effectiveness Analysis:");
        System.out.println("  Initial Best Fitness: " + initialBest.getFitness());
        System.out.println("  Final Best Fitness: " + bestOverall.getFitness());
        System.out.println("  Improvement: " + (bestOverall.getFitness() - initialBest.getFitness()) + 
                         " (" + String.format("%.1f", ((double)(bestOverall.getFitness() - initialBest.getFitness()) / initialBest.getFitness() * 100)) + "% improvement)");
        
        // Show convergence analysis
        if (fitnessHistory.size() > 1) {
            double convergenceRate = (fitnessHistory.get(fitnessHistory.size()-1) - fitnessHistory.get(0)) / fitnessHistory.size();
            System.out.println("  Average Fitness Improvement per Generation: " + String.format("%.2f", convergenceRate));
        }
        
        System.out.println("\n================================================================");
        System.out.println("    GENETIC ALGORITHM EXECUTION COMPLETED SUCCESSFULLY");
        System.out.println("    All 8 phases demonstrated with detailed results");
        System.out.println("================================================================");

        sc.close();
    }


    public static Chromosome findBest(List<Chromosome> population) {
        Chromosome best = population.get(0);
        for (Chromosome c : population) {
            if (c.getFitness() > best.getFitness()) {
                best = c;
            }
        }
        return best;
    }

    public static ArrayList<ArrayList<Integer>> getDistanceBetweenAllPoints(int n) {
        ArrayList<ArrayList<Integer>> distanceMatrix = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            distanceMatrix.add(new ArrayList<>());
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    distanceMatrix.get(i).add(0);
                } else if (j < i) {
                    distanceMatrix.get(i).add(distanceMatrix.get(j).get(i));
                } else {
                    int distance = rand.nextInt(90) + 10;
                    distanceMatrix.get(i).add(distance);
                }
            }
        }
        return distanceMatrix;
    }

    public static void printDistanceMatrix(ArrayList<ArrayList<Integer>> matrix) {
        System.out.println("\n=== Distance Matrix ===");
        for (int i = 0; i < matrix.size(); i++) {
            for (int j = 0; j < matrix.get(i).size(); j++) {
                System.out.printf("%4d", matrix.get(i).get(j));
            }
            System.out.println();
        }
    }
}
