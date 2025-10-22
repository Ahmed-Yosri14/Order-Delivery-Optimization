package Chromosomes;

import java.util.List;

public interface Chromosome {
    void generateGenes(int numberOfGenes);
    List<Integer> getDeliverySequence();
    void mutateMethod1(double probability);
    int getFitness();
    Chromosome clone();
    int getTotalRouteTime();
    String printGenes();
}
