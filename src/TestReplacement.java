import Chromosomes.BinaryChromosome;
import Chromosomes.Chromosome;
import Crossover.OrderOneCrossover;
import Fitness.BinaryFitnessEvaluator;
import Replacement.*;
import Selection.TournamentSelection;

import java.util.*;

/**
 * Comprehensive test suite for all three replacement strategies:
 * 1. Generational Replacement (GGA)
 * 2. Steady-State Replacement (SSGA)
 * 3. Elitist Replacement
 * 
 * This test demonstrates how each strategy works with the genetic algorithm
 * and shows the differences in population evolution.
 */
public class TestReplacement {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.println("================================================================");
        System.out.println("         TESTING REPLACEMENT STRATEGIES");
        System.out.println("================================================================");
        System.out.println("This test demonstrates three replacement strategies:");
        System.out.println("1. Generational Replacement (GGA)");
        System.out.println("2. Steady-State Replacement (SSGA)");
        System.out.println("3. Elitist Replacement");
        System.out.println("================================================================\n");
        
        // Setup parameters
        System.out.println("Enter number of delivery points:");
        int numOrders = sc.nextInt();
        
        System.out.println("Enter population size:");
        int popSize = sc.nextInt();
        
        System.out.println("Enter time constraint:");
        int timeConstraint = sc.nextInt();
        
        System.out.println("Enter number of generations to test:");
        int generations = sc.nextInt();
        
        // Generate distance matrix
        ArrayList<ArrayList<Integer>> distMatrix = getDistanceBetweenAllPoints(numOrders + 1);
        
        // Initialize fitness evaluator
        BinaryFitnessEvaluator.getInstance(distMatrix, timeConstraint);
        
        // Test each replacement strategy
        System.out.println("\n" + "=".repeat(70));
        System.out.println("Choose which replacement strategy to test:");
        System.out.println("1 - Generational Replacement (GGA)");
        System.out.println("2 - Steady-State Replacement (SSGA)");
        System.out.println("3 - Elitist Replacement");
        System.out.println("4 - Test All Three Strategies");
        int choice = sc.nextInt();
        
        switch (choice) {
            case 1:
                testGenerationalReplacement(numOrders, popSize, generations);
                break;
            case 2:
                testSteadyStateReplacement(numOrders, popSize, generations);
                break;
            case 3:
                testElitistReplacement(numOrders, popSize, generations);
                break;
            case 4:
                compareAllStrategies(numOrders, popSize, generations);
                break;
            default:
                System.out.println("Invalid choice!");
        }
        
        sc.close();
    }
    
    /**
     * Test Generational Replacement Strategy
     */
    private static void testGenerationalReplacement(int numOrders, int popSize, 
                                                   int generations) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST 1: GENERATIONAL REPLACEMENT (GGA)");
        System.out.println("=".repeat(70));
        System.out.println("Strategy: Replace ENTIRE population with offspring each generation");
        System.out.println("No individuals survive from one generation to the next");
        System.out.println("=".repeat(70));
        
        ReplacementStrategy replacement = new GenerationalReplacement();

        // Initialize population
        List<Chromosome> population = initializePopulation(numOrders, popSize);
        
        // Setup GA components
        TournamentSelection selection = new TournamentSelection(3);
        OrderOneCrossover crossover = new OrderOneCrossover();
        
        // Track best fitness over generations
        List<Integer> fitnessHistory = new ArrayList<>();
        
        System.out.println("\n--- INITIAL POPULATION ---");
        printPopulationStats(population);
        
        // Run GA with generational replacement
        for (int gen = 0; gen < generations; gen++) {
            // Generate offspring (need exactly popSize offspring)
            List<Chromosome> offspring = new ArrayList<>();
            
            while (offspring.size() < popSize) {
                Chromosome parent1 = selection.select(population);
                Chromosome parent2 = selection.select(population);
                
                List<Chromosome> children = crossover.crossover(parent1, parent2, 0.8);
                
                for (Chromosome child : children) {
                    child.mutateMethod1(0.1);
                    offspring.add(child);
                    if (offspring.size() >= popSize) break;
                }
            }
            
            // GENERATIONAL REPLACEMENT: Replace entire population
            population = replacement.replace(population, offspring);
            
            // Track progress
            Chromosome best = findBest(population);
            fitnessHistory.add(best.getFitness());
            
            if ((gen + 1) % 5 == 0 || gen == 0 || gen == generations - 1) {
                System.out.println("\n--- GENERATION " + (gen + 1) + " ---");
                printPopulationStats(population);
            }
        }
        
        System.out.println("\n--- FINAL RESULTS ---");
        Chromosome best = findBest(population);
        System.out.println("Best Fitness: " + best.getFitness());
        System.out.println("Best Sequence: " + best.getDeliverySequence());
        System.out.println("Fitness Progression: " + fitnessHistory);
    }
    
    /**
     * Test Steady-State Replacement Strategy
     */
    private static void testSteadyStateReplacement(int numOrders, int popSize, 
                                                  int generations) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST 2: STEADY-STATE REPLACEMENT (SSGA)");
        System.out.println("=".repeat(70));
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter K (number of parents to select for reproduction):");
        int k = sc.nextInt();
        
        System.out.println("=".repeat(70));
        System.out.println("Strategy: K individuals selected, K offspring replace their parents");
        System.out.println("K = " + k);
        System.out.println("Most individuals survive to next generation");
        System.out.println("=".repeat(70));
        
        ReplacementStrategy replacement = new SteadyStateReplacement(k);
        System.out.println("\nUsing Steady-State Replacement with K=" + k);
        
        // Initialize population
        List<Chromosome> population = initializePopulation(numOrders, popSize);
        
        // Setup GA components
        TournamentSelection selection = new TournamentSelection(3);
        OrderOneCrossover crossover = new OrderOneCrossover();
        
        List<Integer> fitnessHistory = new ArrayList<>();
        
        System.out.println("\n--- INITIAL POPULATION ---");
        printPopulationStats(population);
        
        // Run GA with steady-state replacement
        for (int gen = 0; gen < generations; gen++) {
            // Generate K offspring
            List<Chromosome> offspring = new ArrayList<>();
            
            while (offspring.size() < k) {
                Chromosome parent1 = selection.select(population);
                Chromosome parent2 = selection.select(population);
                
                List<Chromosome> children = crossover.crossover(parent1, parent2, 0.8);
                
                for (Chromosome child : children) {
                    child.mutateMethod1(0.1);
                    offspring.add(child);
                    if (offspring.size() >= k) break;
                }
            }
            
            // STEADY-STATE REPLACEMENT: Replace only K individuals
            population = replacement.replace(population, offspring);
            
            // Track progress
            Chromosome best = findBest(population);
            fitnessHistory.add(best.getFitness());
            
            if ((gen + 1) % 5 == 0 || gen == 0 || gen == generations - 1) {
                System.out.println("\n--- GENERATION " + (gen + 1) + " ---");
                printPopulationStats(population);
            }
        }
        
        System.out.println("\n--- FINAL RESULTS ---");
        Chromosome best = findBest(population);
        System.out.println("Best Fitness: " + best.getFitness());
        System.out.println("Best Sequence: " + best.getDeliverySequence());
        System.out.println("Fitness Progression: " + fitnessHistory);
    }
    
    /**
     * Test Elitist Replacement Strategy
     */
    private static void testElitistReplacement(int numOrders, int popSize, 
                                              int generations) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("TEST 3: ELITIST REPLACEMENT");
        System.out.println("=".repeat(70));
        
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter number of elite individuals to preserve:");
        int eliteCount = sc.nextInt();
        
        ReplacementStrategy replacement = new ElitistReplacement(eliteCount);
        
        System.out.println("=".repeat(70));
        System.out.println("Strategy: Keep best-so-far individuals and copy to next generation");
        System.out.println("Elite Count: " + eliteCount);
        System.out.println("Prevents best solutions from being lost");
        System.out.println("=".repeat(70));
        
        // Initialize population
        List<Chromosome> population = initializePopulation(numOrders, popSize);
        
        // Setup GA components
        TournamentSelection selection = new TournamentSelection(3);
        OrderOneCrossover crossover = new OrderOneCrossover();
        
        List<Integer> fitnessHistory = new ArrayList<>();
        List<Chromosome> bestOverallList = new ArrayList<>();
        
        System.out.println("\n--- INITIAL POPULATION ---");
        printPopulationStats(population);
        Chromosome bestOverall = findBest(population).clone();
        
        // Run GA with elitist replacement
        for (int gen = 0; gen < generations; gen++) {
            // Calculate how many offspring we need
            ElitistReplacement elitist = (ElitistReplacement) replacement;
            int eliteCount1 = elitist.getEliteCount();
            int offspringNeeded = popSize - eliteCount;
            
            // Generate offspring
            List<Chromosome> offspring = new ArrayList<>();
            
            while (offspring.size() < offspringNeeded) {
                Chromosome parent1 = selection.select(population);
                Chromosome parent2 = selection.select(population);
                
                List<Chromosome> children = crossover.crossover(parent1, parent2, 0.8);
                
                for (Chromosome child : children) {
                    child.mutateMethod1(0.1);
                    offspring.add(child);
                    if (offspring.size() >= offspringNeeded) break;
                }
            }
            
            // ELITIST REPLACEMENT: Preserve elites + add offspring
            population = replacement.replace(population, offspring);
            
            // Track best solution ever found
            Chromosome currentBest = findBest(population);
            if (currentBest.getFitness() > bestOverall.getFitness()) {
                bestOverall = currentBest.clone();
            }
            
            fitnessHistory.add(currentBest.getFitness());
            bestOverallList.add(bestOverall.clone());
            
            if ((gen + 1) % 5 == 0 || gen == 0 || gen == generations - 1) {
                System.out.println("\n--- GENERATION " + (gen + 1) + " ---");
                ElitistReplacement elitistStrategy = (ElitistReplacement) replacement;
                System.out.println("Elites preserved: " + elitistStrategy.getEliteCount());
                printPopulationStats(population);
                System.out.println("Best-so-far Fitness: " + bestOverall.getFitness());
            }
        }
        
        System.out.println("\n--- FINAL RESULTS ---");
        System.out.println("Best Fitness Ever: " + bestOverall.getFitness());
        System.out.println("Best Sequence: " + bestOverall.getDeliverySequence());
        System.out.println("Fitness Progression: " + fitnessHistory);
        
        // Verify best solution was never lost
        System.out.println("\n--- ELITISM VERIFICATION ---");
        System.out.println("Checking that best solution was never lost...");
        boolean neverLost = true;
        int maxFitnessSoFar = 0;
        for (int i = 0; i < fitnessHistory.size(); i++) {
            if (fitnessHistory.get(i) < maxFitnessSoFar) {
                neverLost = false;
                System.out.println("WARNING: Best fitness decreased at generation " + (i+1));
            }
            maxFitnessSoFar = Math.max(maxFitnessSoFar, fitnessHistory.get(i));
        }
        if (neverLost) {
            System.out.println("✅ SUCCESS: Best solution was NEVER lost!");
        }
    }
    
    /**
     * Compare all three strategies side-by-side
     */
    private static void compareAllStrategies(int numOrders, int popSize, 
                                            int generations) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("COMPARATIVE ANALYSIS: ALL THREE STRATEGIES");
        System.out.println("=".repeat(70));
        
        // Setup strategies
        ReplacementStrategy generational = new GenerationalReplacement();
        ReplacementStrategy steadyState = new SteadyStateReplacement(popSize / 4);
        ReplacementStrategy elitist = new ElitistReplacement(2);
        
        // Run each strategy
        Map<String, List<Integer>> results = new HashMap<>();
        
        System.out.println("\nRunning Generational Replacement...");
        results.put("Generational", runStrategy(generational, numOrders, popSize, generations));
        
        System.out.println("Running Steady-State Replacement...");
        results.put("Steady-State", runStrategy(steadyState, numOrders, popSize, generations));
        
        System.out.println("Running Elitist Replacement...");
        results.put("Elitist", runStrategy(elitist, numOrders, popSize, generations));
        
        // Display comparison
        System.out.println("\n" + "=".repeat(70));
        System.out.println("COMPARISON RESULTS");
        System.out.println("=".repeat(70));
        
        for (Map.Entry<String, List<Integer>> entry : results.entrySet()) {
            String strategyName = entry.getKey();
            List<Integer> history = entry.getValue();
            
            int initialFitness = history.get(0);
            int finalFitness = history.get(history.size() - 1);
            int maxFitness = Collections.max(history);
            double avgFitness = history.stream().mapToInt(Integer::intValue).average().orElse(0.0);
            
            System.out.println("\n" + strategyName + " Replacement:");
            System.out.println("  Initial Fitness: " + initialFitness);
            System.out.println("  Final Fitness: " + finalFitness);
            System.out.println("  Max Fitness: " + maxFitness);
            System.out.println("  Average Fitness: " + String.format("%.2f", avgFitness));
            System.out.println("  Improvement: " + (finalFitness - initialFitness));
        }
    }
    
    /**
     * Run a strategy and return fitness history
     */
    private static List<Integer> runStrategy(ReplacementStrategy replacement, int numOrders, 
                                            int popSize, int generations) {
        List<Chromosome> population = initializePopulation(numOrders, popSize);
        TournamentSelection selection = new TournamentSelection(3);
        OrderOneCrossover crossover = new OrderOneCrossover();
        List<Integer> fitnessHistory = new ArrayList<>();
        
        for (int gen = 0; gen < generations; gen++) {
            int offspringNeeded = popSize;
            if (replacement instanceof ElitistReplacement) {
                ElitistReplacement elitist = (ElitistReplacement) replacement;
                offspringNeeded = popSize - elitist.getEliteCount();
            } else if (replacement instanceof SteadyStateReplacement) {
                SteadyStateReplacement ss = (SteadyStateReplacement) replacement;
                offspringNeeded = ss.getK();
            }
            
            List<Chromosome> offspring = new ArrayList<>();
            while (offspring.size() < offspringNeeded) {
                Chromosome parent1 = selection.select(population);
                Chromosome parent2 = selection.select(population);
                List<Chromosome> children = crossover.crossover(parent1, parent2, 0.8);
                for (Chromosome child : children) {
                    child.mutateMethod1(0.1);
                    offspring.add(child);
                    if (offspring.size() >= offspringNeeded) break;
                }
            }
            
            population = replacement.replace(population, offspring);
            fitnessHistory.add(findBest(population).getFitness());
        }
        
        return fitnessHistory;
    }
    
    // Helper methods
    private static List<Chromosome> initializePopulation(int numOrders, int popSize) {
        List<Chromosome> population = new ArrayList<>();
        for (int i = 0; i < popSize; i++) {
            BinaryChromosome chromosome = new BinaryChromosome();
            chromosome.generateGenes(numOrders);
            population.add(chromosome);
        }
        return population;
    }
    
    private static Chromosome findBest(List<Chromosome> population) {
        Chromosome best = population.get(0);
        for (Chromosome c : population) {
            if (c.getFitness() > best.getFitness()) {
                best = c;
            }
        }
        return best;
    }
    
    private static void printPopulationStats(List<Chromosome> population) {
        int maxFitness = Integer.MIN_VALUE;
        int minFitness = Integer.MAX_VALUE;
        double avgFitness = 0.0;
        
        for (Chromosome c : population) {
            int fitness = c.getFitness();
            maxFitness = Math.max(maxFitness, fitness);
            minFitness = Math.min(minFitness, fitness);
            avgFitness += fitness;
        }
        avgFitness /= population.size();
        
        System.out.println("Population Size: " + population.size());
        System.out.println("Best Fitness: " + maxFitness);
        System.out.println("Worst Fitness: " + minFitness);
        System.out.println("Average Fitness: " + String.format("%.2f", avgFitness));
    }
    
    private static ArrayList<ArrayList<Integer>> getDistanceBetweenAllPoints(int n) {
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
}

