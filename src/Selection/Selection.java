package Selection;

import Chromosomes.Chromosome;
import java.util.List;

public interface Selection {
    /**
     * Select one chromosome from the population
     * @param population The current population
     * @return Selected chromosome
     */
    Chromosome select(List<Chromosome> population);

    /**
     * Select multiple chromosomes from the population
     * @param population The current population
     * @param count Number of chromosomes to select
     * @return List of selected chromosomes
     */
    List<Chromosome> selectMultiple(List<Chromosome> population, int count);
}