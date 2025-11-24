package GeneticAlgorithm.Replacement;

import GeneticAlgorithm.Chromosomes.Chromosome;
import java.util.ArrayList;
import java.util.List;


public class SteadyStateReplacement implements ReplacementStrategy {
    
    private final int k;
    private List<Integer> parentIndices;
    
    public SteadyStateReplacement(int k) {
        if (k < 1) {
            throw new IllegalArgumentException("K must be at least 1");
        }
        this.k = k;
        this.parentIndices = new ArrayList<>();
    }
    
    public void setParentIndices(List<Integer> parentIndices) {
        this.parentIndices = new ArrayList<>(parentIndices);
    }
    
    @Override
    public List<Chromosome> replace(List<Chromosome> currentPopulation, List<Chromosome> offspring) {
        if (currentPopulation == null || currentPopulation.isEmpty()) {
            throw new IllegalArgumentException("Current population cannot be null or empty");
        }
        if (offspring == null || offspring.isEmpty()) {
            throw new IllegalArgumentException("Offspring cannot be null or empty");
        }
        if (k > currentPopulation.size()) {
            throw new IllegalArgumentException("K cannot be larger than population size");
        }
        if (offspring.size() < k) {
            throw new IllegalArgumentException("Not enough offspring");
        }
        
        List<Chromosome> nextGeneration = new ArrayList<>();
        for (Chromosome c : currentPopulation) {
            nextGeneration.add(c.clone());
        }
        
        // Determine which parents to replace
        List<Integer> indicesToReplace;
        if (parentIndices != null && parentIndices.size() >= k) {
            indicesToReplace = parentIndices.subList(0, k);
        } else {
            indicesToReplace = new ArrayList<>();
            for (int i = 0; i < k; i++) {
                indicesToReplace.add(i);
            }
        }
        
        // K offspring replace K parents
        for (int i = 0; i < k; i++) {
            int indexToReplace = indicesToReplace.get(i);
            nextGeneration.set(indexToReplace, offspring.get(i).clone());
        }
        
        return nextGeneration;
    }
    
    public int getK() {
        return k;
    }
    
    @Override
    public String toString() {
        return "SteadyStateReplacement{k=" + k + "}";
    }
}

