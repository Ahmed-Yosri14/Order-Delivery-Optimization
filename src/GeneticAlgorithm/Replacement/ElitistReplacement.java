package GeneticAlgorithm.Replacement;

import GeneticAlgorithm.Chromosomes.Chromosome;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ElitistReplacement implements ReplacementStrategy {
    
    private final int eliteCount;
    
    public ElitistReplacement(int eliteCount) {
        if (eliteCount < 1) {
            throw new IllegalArgumentException("Elite count must be at least 1");
        }
        this.eliteCount = eliteCount;
    }
    
    @Override
    public List<Chromosome> replace(List<Chromosome> currentPopulation, List<Chromosome> offspring) {
        if (currentPopulation == null || currentPopulation.isEmpty()) {
            throw new IllegalArgumentException("Current population cannot be null or empty");
        }
        if (offspring == null || offspring.isEmpty()) {
            throw new IllegalArgumentException("Offspring cannot be null or empty");
        }
        
        int populationSize = currentPopulation.size();
        
        if (eliteCount >= populationSize) {
            throw new IllegalArgumentException("Elite count must be less than population size");
        }
        
        int offspringNeeded = populationSize - eliteCount;
        if (offspring.size() < offspringNeeded) {
            throw new IllegalArgumentException("Not enough offspring");
        }
        
        // Get elite individuals (best-so-far)
        List<Chromosome> elites = getEliteIndividuals(currentPopulation, eliteCount);
        
        List<Chromosome> nextGeneration = new ArrayList<>();
        
        // Copy elites to next generation
        for (Chromosome elite : elites) {
            nextGeneration.add(elite.clone());
        }
        
        // Add offspring
        for (int i = 0; i < offspringNeeded; i++) {
            nextGeneration.add(offspring.get(i).clone());
        }
        
        return nextGeneration;
    }
    
    private List<Chromosome> getEliteIndividuals(List<Chromosome> population, int count) {
        List<Chromosome> sortedPopulation = new ArrayList<>(population);
        
        // Sort by fitness descending (best first)
        Collections.sort(sortedPopulation, new Comparator<Chromosome>() {
            @Override
            public int compare(Chromosome c1, Chromosome c2) {
                return Integer.compare(c2.getFitness(), c1.getFitness());
            }
        });
        
        List<Chromosome> elites = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            elites.add(sortedPopulation.get(i));
        }
        
        return elites;
    }
    
    public int getEliteCount() {
        return eliteCount;
    }
    
    @Override
    public String toString() {
        return "ElitistReplacement{count=" + eliteCount + "}";
    }
}
