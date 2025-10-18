package Chromosomes;

import java.util.List;

public class FloatingPointChromosome implements Chromosome {
    List<Double> genes;
    @Override
    public void mutate(double probability) {

    }

    @Override
    public void generateGenes(int numberOfGenes) {
        for (int i = 0; i < numberOfGenes; i++) {
            genes.add(Math.random());
        }
    }
}
