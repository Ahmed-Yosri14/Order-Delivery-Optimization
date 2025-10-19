package Chromosomes;

import java.util.List;

public class FloatingPointChromosome implements Chromosome {
    List<Double> genes;
    public void mutateMethod1(double probability) {

    }

    @Override
    public void mutateMethod2(double probability) {

    }

    @Override
    public int getFitness() {
        return 0;
    }

    @Override
    public Chromosome clone() {
        return null;
    }

    @Override
    public int getTotalRouteTime() {
        return 0;
    }

    @Override
    public void generateGenes(int numberOfGenes) {
        for (int i = 0; i < numberOfGenes; i++) {
            genes.add(Double.valueOf(Math.random()));
        }
    }

    @Override
    public List<Integer> getDeliverySequence() {
        return List.of();
    }



}
