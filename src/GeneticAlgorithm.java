import Chromosomes.*;
import Crossover.*;
import Fitness.*;
import Selection.*;
import Replacement.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Genetic Algorithm Engine - Configurable GA library for optimization problems
 * Supports multiple chromosome types, selection methods, crossover operators, and replacement strategies
 */
public class GeneticAlgorithm {
    
    // GA Configuration Parameters
    private int populationSize = 50;
    private int chromosomeLength = 10;
    private int generations = 100;
    private double crossoverRate = 0.7;
    private double mutationRate = 0.01;
    private int eliteCount = 1;
    
    // Problem-specific components
    private FitnessEvaluator fitnessFunction;
    private ChromosomeType chromosomeType = ChromosomeType.INTEGER;
    private Selection selectionMethod;
    private Crossover crossoverOperator;
    private ReplacementStrategy replacementStrategy;
    
    // Population and results
    private List<Chromosome> population;
    private Chromosome bestSolution;
    private List<Double> fitnessHistory;
    private boolean initialized = false;
    
    public enum ChromosomeType {
        BINARY, INTEGER, FLOATING_POINT
    }
    
    /**
     * Default constructor with preset values
     */
    public GeneticAlgorithm() {
        this.fitnessHistory = new ArrayList<>();
        this.selectionMethod = new TournamentSelection(3);
        this.replacementStrategy = new ElitistReplacement(1);
    }
    
    /**
     * Initialize the GA population
     */
    public void initialize() {
        if (fitnessFunction == null) {
            throw new IllegalStateException("Fitness function must be set before initialization");
        }
        
        Initializer initializer = new Initializer();
        int typeChoice = chromosomeType == ChromosomeType.BINARY ? 1 :
                        chromosomeType == ChromosomeType.INTEGER ? 2 : 3;
        
        population = initializer.init(typeChoice, chromosomeLength, populationSize);
        
        // Set default crossover based on chromosome type if not set
        if (crossoverOperator == null) {
            switch (chromosomeType) {
                case BINARY:
                    crossoverOperator = new OrderOneCrossover();
                    break;
                case INTEGER:
                    crossoverOperator = IntegerCrossover.getInstance();
                    break;
                case FLOATING_POINT:
                    crossoverOperator = new FloatingPointUniformCrossover();
                    break;
            }
        }
        
        // Evaluate initial population
        for (Chromosome chromosome : population) {
            chromosome.getFitness();
        }
        
        bestSolution = findBest(population);
        initialized = true;
    }
    
    /**
     * Run the genetic algorithm
     */
    public void run() {
        if (!initialized) {
            initialize();
        }
        
        System.out.println("\n=== Starting Genetic Algorithm ===");
        System.out.println("Population Size: " + populationSize);
        System.out.println("Generations: " + generations);
        System.out.println("Crossover Rate: " + crossoverRate);
        System.out.println("Mutation Rate: " + mutationRate);
        System.out.println("Selection: " + selectionMethod.getClass().getSimpleName());
        System.out.println("Replacement: " + replacementStrategy.getClass().getSimpleName());
        System.out.println("Initial Best Fitness: " + bestSolution.getFitness());
        
        for (int gen = 0; gen < generations; gen++) {
            // Create offspring through genetic operators
            List<Chromosome> offspring = createOffspring();
            
            // Replace population using replacement strategy
            population = replacementStrategy.replace(population, offspring);
            
            // Track best solution
            Chromosome currentBest = findBest(population);
            if (currentBest.getFitness() > bestSolution.getFitness()) {
                bestSolution = currentBest.clone();
            }
            
            // Record statistics
            fitnessHistory.add((double) currentBest.getFitness());
            
            // Progress reporting
            if ((gen + 1) % 10 == 0 || gen == 0 || gen == generations - 1) {
                System.out.println("Generation " + (gen + 1) + 
                                 " - Best Fitness: " + currentBest.getFitness() +
                                 " - Avg Fitness: " + String.format("%.2f", getAverageFitness()));
            }
        }
        
        System.out.println("\n=== Genetic Algorithm Completed ===");
        System.out.println("Final Best Fitness: " + bestSolution.getFitness());
        System.out.println("Best Solution: " + bestSolution.getDeliverySequence());
    }
    
    /**
     * Create offspring through selection, crossover, and mutation
     */
    private List<Chromosome> createOffspring() {
        List<Chromosome> offspring = new ArrayList<>();
        
        // Keep creating offspring until we have enough
        while (offspring.size() < populationSize) {
            // Selection
            Chromosome parent1 = selectionMethod.select(population);
            Chromosome parent2 = selectionMethod.select(population);
            
            // Crossover
            List<Chromosome> children = crossoverOperator.crossover(parent1, parent2, crossoverRate);
            
            // Mutation
            for (Chromosome child : children) {
                child.mutateMethod1(mutationRate);
                offspring.add(child);
                if (offspring.size() >= populationSize) break;
            }
        }
        
        return offspring;
    }
    
    /**
     * Find the best chromosome in the population
     */
    private Chromosome findBest(List<Chromosome> pop) {
        Chromosome best = pop.get(0);
        for (Chromosome c : pop) {
            if (c.getFitness() > best.getFitness()) {
                best = c;
            }
        }
        return best;
    }
    
    /**
     * Calculate average fitness of current population
     */
    private double getAverageFitness() {
        return population.stream()
                .mapToInt(Chromosome::getFitness)
                .average()
                .orElse(0.0);
    }
    
    // ==================== Getters ====================
    
    public Chromosome getBestSolution() {
        return bestSolution;
    }
    
    public List<Chromosome> getPopulation() {
        return new ArrayList<>(population);
    }
    
    public List<Double> getFitnessHistory() {
        return new ArrayList<>(fitnessHistory);
    }
    
    public int getPopulationSize() {
        return populationSize;
    }
    
    public int getChromosomeLength() {
        return chromosomeLength;
    }
    
    public int getGenerations() {
        return generations;
    }
    
    public double getCrossoverRate() {
        return crossoverRate;
    }
    
    public double getMutationRate() {
        return mutationRate;
    }
    
    // ==================== Setters ====================
    
    public void setPopulationSize(int populationSize) {
        if (populationSize < 2) {
            throw new IllegalArgumentException("Population size must be at least 2");
        }
        this.populationSize = populationSize;
    }
    
    public void setChromosomeLength(int chromosomeLength) {
        if (chromosomeLength < 1) {
            throw new IllegalArgumentException("Chromosome length must be at least 1");
        }
        this.chromosomeLength = chromosomeLength;
    }
    
    public void setGenerations(int generations) {
        if (generations < 1) {
            throw new IllegalArgumentException("Generations must be at least 1");
        }
        this.generations = generations;
    }
    
    public void setCrossoverRate(double crossoverRate) {
        if (crossoverRate < 0.0 || crossoverRate > 1.0) {
            throw new IllegalArgumentException("Crossover rate must be between 0.0 and 1.0");
        }
        this.crossoverRate = crossoverRate;
    }
    
    public void setMutationRate(double mutationRate) {
        if (mutationRate < 0.0 || mutationRate > 1.0) {
            throw new IllegalArgumentException("Mutation rate must be between 0.0 and 1.0");
        }
        this.mutationRate = mutationRate;
    }
    
    public void setFitnessFunction(FitnessEvaluator fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }
    
    public void setChromosomeType(ChromosomeType chromosomeType) {
        this.chromosomeType = chromosomeType;
    }
    
    public void setSelectionMethod(Selection selectionMethod) {
        this.selectionMethod = selectionMethod;
    }
    
    public void setCrossoverOperator(Crossover crossoverOperator) {
        this.crossoverOperator = crossoverOperator;
    }
    
    public void setReplacementStrategy(ReplacementStrategy replacementStrategy) {
        this.replacementStrategy = replacementStrategy;
    }
    
    public void setEliteCount(int eliteCount) {
        if (eliteCount < 0) {
            throw new IllegalArgumentException("Elite count must be non-negative");
        }
        this.eliteCount = eliteCount;
    }
    
    /**
     * Print detailed statistics about the GA run
     */
    public void printStatistics() {
        if (bestSolution == null) {
            System.out.println("No solution available. Run the algorithm first.");
            return;
        }
        
        System.out.println("\n=== GENETIC ALGORITHM STATISTICS ===");
        System.out.println("Best Fitness: " + bestSolution.getFitness());
        System.out.println("Best Solution: " + bestSolution.getDeliverySequence());
        System.out.println("Total Route Time: " + bestSolution.getTotalRouteTime());
        
        if (!fitnessHistory.isEmpty()) {
            double avgFitness = fitnessHistory.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double maxFitness = fitnessHistory.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
            double minFitness = fitnessHistory.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            
            System.out.println("\nFitness Evolution:");
            System.out.println("  Average: " + String.format("%.2f", avgFitness));
            System.out.println("  Maximum: " + String.format("%.2f", maxFitness));
            System.out.println("  Minimum: " + String.format("%.2f", minFitness));
        }
        
        System.out.println("\nConfiguration:");
        System.out.println("  Chromosome Type: " + chromosomeType);
        System.out.println("  Population Size: " + populationSize);
        System.out.println("  Generations: " + generations);
        System.out.println("  Crossover Rate: " + crossoverRate);
        System.out.println("  Mutation Rate: " + mutationRate);
        System.out.println("=====================================\n");
    }
}

