package Crossover;

import Chromosomes.Chromosome;
import java.util.List;

public interface Crossover {
    /**
     * Perform crossover between two parent chromosomes with given probability
     * @param parent1 First parent chromosome
     * @param parent2 Second parent chromosome
     * @param probability Probability of crossover occurring (0.0 to 1.0)
     * @return List containing two offspring chromosomes (if crossover occurs) or copies of parents (if not)
     */
    List<Chromosome> crossover(Chromosome parent1, Chromosome parent2, double probability);
}