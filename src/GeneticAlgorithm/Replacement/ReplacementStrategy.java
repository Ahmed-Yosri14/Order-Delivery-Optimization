package GeneticAlgorithm.Replacement;

import GeneticAlgorithm.Chromosomes.Chromosome;
import java.util.List;


public interface ReplacementStrategy {
    List<Chromosome> replace(List<Chromosome> currentPopulation, List<Chromosome> offspring);
}

