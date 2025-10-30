package Selection;

import Chromosomes.Chromosome;
import java.util.List;

public interface Selection {

    Chromosome select(List<Chromosome> population);


    List<Chromosome> selectMultiple(List<Chromosome> population, int count);
}