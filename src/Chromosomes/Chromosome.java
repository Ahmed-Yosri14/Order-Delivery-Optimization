package Chromosomes;

public interface Chromosome {
    void mutate(double probability);
    void generateGenes(int numberOfGenes);
}
