package GeneticAlgorithm.Replacement;

import GeneticAlgorithm.Chromosomes.Chromosome;
import java.util.ArrayList;
import java.util.List;


public class GenerationalReplacement implements ReplacementStrategy {
    
    @Override
    public List<Chromosome> replace(List<Chromosome> currentPopulation, List<Chromosome> offspring) {
        if (currentPopulation == null || currentPopulation.isEmpty()) {
            throw new IllegalArgumentException("Current population cannot be null or empty");
        }
        if (offspring == null || offspring.isEmpty()) {
            throw new IllegalArgumentException("Offspring cannot be null or empty");
        }
        
        int populationSize = currentPopulation.size();
        
        if (offspring.size() < populationSize) {
            throw new IllegalArgumentException(
                "Offspring size (" + offspring.size() + ") is less than population size (" + 
                populationSize + ")");
        }
        
        // Replace entire population with offspring
        List<Chromosome> nextGeneration = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            nextGeneration.add(offspring.get(i).clone());
        }
        
        return nextGeneration;
    }

    @Override
    public String toString() {
        return "GenerationalReplacement";
    }
}

