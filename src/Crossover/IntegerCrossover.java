package Crossover;

import Chromosomes.Chromosome;
import Chromosomes.IntegerChromosome;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntegerCrossover implements Crossover {

    private static IntegerCrossover instance;
    private static final Random random = new Random();

    private IntegerCrossover() {}

    public static synchronized IntegerCrossover getInstance() {
        if (instance == null) {
            instance = new IntegerCrossover();
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        instance = null;
    }

    @Override
    public List<Chromosome> crossover(Chromosome parent1, Chromosome parent2, double probability) {
        if (!(parent1 instanceof IntegerChromosome) || !(parent2 instanceof IntegerChromosome)) {
            throw new IllegalArgumentException("IntegerCrossover only works with IntegerChromosome");
        }

        List<Chromosome> offspring = new ArrayList<>();

        if (random.nextDouble() > probability) {
            offspring.add(parent1.clone());
            offspring.add(parent2.clone());
            return offspring;
        }

        IntegerChromosome p1 = (IntegerChromosome) parent1;
        IntegerChromosome p2 = (IntegerChromosome) parent2;

        List<Integer> seq1 = p1.getDeliverySequence();
        List<Integer> seq2 = p2.getDeliverySequence();

        if (seq1.size() != seq2.size() || seq1.size() < 2) {
            throw new IllegalArgumentException("Parents must have same size and at least 2 genes");
        }

        int size = seq1.size();
        int cut1 = random.nextInt(size);
        int cut2 = random.nextInt(size);
        if (cut1 > cut2) { int t = cut1; cut1 = cut2; cut2 = t; }

        List<Integer> child1Seq = new ArrayList<>();
        List<Integer> child2Seq = new ArrayList<>();
        for (int i = 0; i < size; i++) { child1Seq.add(null); child2Seq.add(null); }

        for (int i = cut1; i <= cut2; i++) {
            child1Seq.set(i, seq1.get(i));
            child2Seq.set(i, seq2.get(i));
        }

        int idx1 = (cut2 + 1) % size;
        int idx2 = (cut2 + 1) % size;

        for (int i = 0; i < size; i++) {
            int p2gene = seq2.get((cut2 + 1 + i) % size);
            if (!child1Seq.contains(p2gene)) {
                child1Seq.set(idx1, p2gene);
                idx1 = (idx1 + 1) % size;
            }
        }

        for (int i = 0; i < size; i++) {
            int p1gene = seq1.get((cut2 + 1 + i) % size);
            if (!child2Seq.contains(p1gene)) {
                child2Seq.set(idx2, p1gene);
                idx2 = (idx2 + 1) % size;
            }
        }

        IntegerChromosome child1 = new IntegerChromosome(child1Seq);
        IntegerChromosome child2 = new IntegerChromosome(child2Seq);

        offspring.add(child1);
        offspring.add(child2);

        return offspring;
    }
}
