package GeneticAlgorithm.Crossover;

import GeneticAlgorithm.Chromosomes.Chromosome;
import java.util.List;

public interface Crossover {

    List<Chromosome> crossover(Chromosome parent1, Chromosome parent2, double probability);
}