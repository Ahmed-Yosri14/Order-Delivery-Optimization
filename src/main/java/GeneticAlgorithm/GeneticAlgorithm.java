package GeneticAlgorithm;

import GeneticAlgorithm.Chromosomes.*;
import GeneticAlgorithm.Crossover.*;
import GeneticAlgorithm.Fitness.*;
import GeneticAlgorithm.Selection.*;
import GeneticAlgorithm.Replacement.*;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithm {
    
    private int populationSize = 50;
    private int chromosomeLength = 10;
    private int generations = 100;
    private double crossoverRate = 0.7;
    private double mutationRate = 0.01;
    private int eliteCount = 1;
    
    private FitnessEvaluator fitnessFunction;
    private ChromosomeType chromosomeType = ChromosomeType.INTEGER;
    private Selection selectionMethod;
    private Crossover crossoverOperator;
    private ReplacementStrategy replacementStrategy;
    private int floatingPointMutationMethod = 1; // 1 = uniform (mutateMethod1), 2 = non-uniform (mutateMethod2)
    
    private List<Chromosome> population;
    private Chromosome bestSolution;
    private List<Double> fitnessHistory;
    private boolean initialized = false;
    
    public enum ChromosomeType {
        BINARY, INTEGER, FLOATING_POINT
    }
    
    public GeneticAlgorithm() {
        this.fitnessHistory = new ArrayList<>();
        this.selectionMethod = new TournamentSelection(3);
        this.replacementStrategy = new ElitistReplacement(1);
    }
    
    public void initialize() {
        if (fitnessFunction == null) {
            throw new IllegalStateException("GeneticAlgorithm.Fitness function must be set before initialization");
        }
        
        Initializer initializer = new Initializer();
        int typeChoice = chromosomeType == ChromosomeType.BINARY ? 1 :
                        chromosomeType == ChromosomeType.INTEGER ? 2 : 3;
        
        population = initializer.init(typeChoice, chromosomeLength, populationSize);
        
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
        
        for (Chromosome chromosome : population) {
            chromosome.getFitness();
        }
        
        bestSolution = findBest(population);
        initialized = true;
    }
    
    public void run() {
        if (!initialized) {
            initialize();
        }
        
        System.out.println("\n=== Starting Genetic Algorithm ===");
        System.out.println("Population Size: " + populationSize);
        System.out.println("Generations: " + generations);
        System.out.println("GeneticAlgorithm.Crossover Rate: " + crossoverRate);
        System.out.println("Mutation Rate: " + mutationRate);
        System.out.println("GeneticAlgorithm.Selection: " + selectionMethod.getClass().getSimpleName());
        System.out.println("GeneticAlgorithm.Replacement: " + replacementStrategy.getClass().getSimpleName());
        System.out.println("Initial Best GeneticAlgorithm.Fitness: " + bestSolution.getFitness());
        
        for (int gen = 0; gen < generations; gen++) {
            List<Chromosome> offspring = createOffspring(gen, generations);
            
            population = replacementStrategy.replace(population, offspring);
            
            Chromosome currentBest = findBest(population);
            if (currentBest.getFitness() > bestSolution.getFitness()) {
                bestSolution = currentBest.clone();
            }
            
            fitnessHistory.add((double) currentBest.getFitness());
            
            if ((gen + 1) % 10 == 0 || gen == 0 || gen == generations - 1) {
                System.out.println("Generation " + (gen + 1) + 
                                 " - Best GeneticAlgorithm.Fitness: " + currentBest.getFitness() +
                                 " - Avg GeneticAlgorithm.Fitness: " + String.format("%.2f", getAverageFitness()));
            }
        }
        
        System.out.println("\n=== Genetic Algorithm Completed ===");
        System.out.println("Final Best GeneticAlgorithm.Fitness: " + bestSolution.getFitness());
        System.out.println("Best Solution: " + bestSolution.getDeliverySequence());
    }
    
    private List<Chromosome> createOffspring(int currentGen, int maxGen) {
        List<Chromosome> offspring = new ArrayList<>();
        
        while (offspring.size() < populationSize) {
            Chromosome parent1 = selectionMethod.select(population);
            Chromosome parent2 = selectionMethod.select(population);
            
            List<Chromosome> children = crossoverOperator.crossover(parent1, parent2, crossoverRate);
            
            for (Chromosome child : children) {
                try {
                    if (child.getClass().getName().equals("GeneticAlgorithm.Chromosomes.FloatingPointChromosome")) {
                        if (floatingPointMutationMethod == 1) {
                            child.getClass().getMethod("mutateMethod1", double.class).invoke(child, mutationRate);
                        } else {
                            child.getClass().getMethod("mutateMethod2", double.class, int.class, int.class)
                                    .invoke(child, mutationRate, currentGen, maxGen);
                        }
                    } else {
                        child.mutateMethod1(mutationRate);
                    }
                } catch (Exception e) {
                    child.mutateMethod1(mutationRate);
                }
                offspring.add(child);
                if (offspring.size() >= populationSize) break;
            }
        }
        
        return offspring;
    }
    
    private Chromosome findBest(List<Chromosome> pop) {
        Chromosome best = pop.get(0);
        for (Chromosome c : pop) {
            if (c.getFitness() > best.getFitness()) {
                best = c;
            }
        }
        return best;
    }
    
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
            throw new IllegalArgumentException("GeneticAlgorithm.Crossover rate must be between 0.0 and 1.0");
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
    
    public void printStatistics() {
        if (bestSolution == null) {
            System.out.println("No solution available. Run the algorithm first.");
            return;
        }
        
        System.out.println("\n=== GENETIC ALGORITHM STATISTICS ===");
        System.out.println("Best GeneticAlgorithm.Fitness: " + bestSolution.getFitness());
        System.out.println("Best Solution: " + bestSolution.getDeliverySequence());
        System.out.println("Total Route Time: " + bestSolution.getTotalRouteTime());
        
        if (!fitnessHistory.isEmpty()) {
            double avgFitness = fitnessHistory.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double maxFitness = fitnessHistory.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
            double minFitness = fitnessHistory.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            
            System.out.println("\nGeneticAlgorithm.Fitness Evolution:");
            System.out.println("  Average: " + String.format("%.2f", avgFitness));
            System.out.println("  Maximum: " + String.format("%.2f", maxFitness));
            System.out.println("  Minimum: " + String.format("%.2f", minFitness));
        }
        
        System.out.println("\nConfiguration:");
        System.out.println("  Chromosome Type: " + chromosomeType);
        System.out.println("  Population Size: " + populationSize);
        System.out.println("  Generations: " + generations);
        System.out.println("  GeneticAlgorithm.Crossover Rate: " + crossoverRate);
        System.out.println("  Mutation Rate: " + mutationRate);
        System.out.println("=====================================\n");
    }
}

