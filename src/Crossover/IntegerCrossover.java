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
        int cutPoint = random.nextInt(size - 1) + 1; // cut between 1 and size-1

        List<Integer> child1Seq = new ArrayList<>();
        List<Integer> child2Seq = new ArrayList<>();

        for (int i = 0; i < cutPoint; i++) {
            child1Seq.add(seq1.get(i));
            child2Seq.add(seq2.get(i));
        }
        for (int i = cutPoint; i < size; i++) {
            child1Seq.add(seq2.get(i));
            child2Seq.add(seq1.get(i));
        }
        IntegerChromosome child1 = new IntegerChromosome(child1Seq);
        IntegerChromosome child2 = new IntegerChromosome(child2Seq);

        offspring.add(child1);
        offspring.add(child2);

        return offspring;
    }
}
