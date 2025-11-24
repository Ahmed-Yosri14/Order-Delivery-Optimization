package GeneticAlgorithm.Chromosomes;

import GeneticAlgorithm.Fitness.FitnessEvaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class IntegerChromosome implements Chromosome {
    private List<Integer> genes;
    private static final Random rand = new Random();
    private FitnessEvaluator fitnessEvaluator = FitnessEvaluator.getInstance();

    public IntegerChromosome(List<Integer> genes) {
        this.genes = new ArrayList<>(genes);
    }

    public IntegerChromosome() {
        this.genes = new ArrayList<>();
    }

    public IntegerChromosome(IntegerChromosome other) {
        this.genes = new ArrayList<>(other.genes); // deep copy
        this.fitnessEvaluator = FitnessEvaluator.getInstance();
    }

    @Override
    public void generateGenes(int numberOfGenes) {
        genes = new ArrayList<>();
        for (int i = 0; i < numberOfGenes; i++) {
            genes.add(i + 1);
        }

        for (int i = 0; i < genes.size(); i++) {
            int first = rand.nextInt(genes.size());
            int second = rand.nextInt(genes.size());
            Collections.swap(genes, first, second);
        }
    }

    @Override
    public List<Integer> getDeliverySequence() {
        return genes;
    }

    @Override
    public void mutateMethod1(double probability) {
        for (int i = 0; i < genes.size(); i++) {
            mutateBySwapping(i, probability);
        }
    }

    @Override
    public int getFitness() {
        return (int) fitnessEvaluator.evaluate(this);
    }

    @Override
    public Chromosome clone() {
        return new IntegerChromosome(this);
    }

    @Override
    public int getTotalRouteTime() {
        return fitnessEvaluator.calculateTotalRouteTime(this);
    }

    public void mutateBySwapping(int idx, double probability) {
        if (rand.nextDouble() < probability && genes.size() > 1) {
            int second = rand.nextInt(genes.size());
            Collections.swap(genes, idx, second);
        }
    }

    public void mutateByBecomingLast(int idx, double probability) {
        if (rand.nextDouble() < probability && !genes.isEmpty()) {
            int val = genes.get(idx);
            genes.remove(idx);
            genes.add(val);
        }
    }

    @Override
    public String printGenes() {
        return genes.toString();
    }
}
