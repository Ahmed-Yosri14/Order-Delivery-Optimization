package Chromosomes;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntegerChromosome implements Chromosome{
    List<Integer> Genes;
    @Override
    public void mutate(double probability) {

    }
    Random rand = new Random();

    @Override
    public void generateGenes(int numberOfGenes) {
        Genes = new ArrayList<Integer>();
        for (int i = 0; i < numberOfGenes; i++) {
            Genes.add(Integer.valueOf(rand.nextInt(100)));
        }
    }

    @Override
    public Chromosome copy() {
        return null;
    }
}
