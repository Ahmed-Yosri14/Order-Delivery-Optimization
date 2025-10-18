package Chromosomes;

import java.util.List;

public class BinaryChromosome implements Chromosome {
    List<Boolean>genes;
    @Override
    public void mutate(double probability) {

    }

    @Override
    public void generateGenes(int numberOfGenes) {
        for (int i = 0; i < numberOfGenes; i++) {
            genes.add(Math.random()>0.5);
        }
    }
}
