package Crossover;

import Chromosomes.BinaryChromosome;
import Chromosomes.Chromosome;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class OrderOneCrossover implements Crossover {
    private static final Random random = new Random();

    @Override
    public List<Chromosome> crossover(Chromosome parent1, Chromosome parent2, double probability) {
        if (!(parent1 instanceof BinaryChromosome) || !(parent2 instanceof BinaryChromosome)) {
            throw new IllegalArgumentException("OrderOneCrossover only works with BinaryChromosome");
        }
        List<Chromosome> offspring = new ArrayList<>();
        if (random.nextDouble() > probability) {
            offspring.add(parent1.clone());
            offspring.add(parent2.clone());
            return offspring;
        }

        BinaryChromosome p1 = (BinaryChromosome) parent1;
        BinaryChromosome p2 = (BinaryChromosome) parent2;

        List<Integer> seq1 = p1.getDeliverySequence();
        List<Integer> seq2 = p2.getDeliverySequence();

        if (seq1.size() != seq2.size() || seq1.size() < 2) {
            throw new IllegalArgumentException("Parents must have same size and at least 2 orders");
        }
        int size = seq1.size();
        int cutPoint1 = random.nextInt(size - 1);
        int cutPoint2 = random.nextInt(size - cutPoint1 - 1) + cutPoint1 + 1;
        List<Integer> child1Seq = createOffspring(seq1, seq2, cutPoint1, cutPoint2);
        List<Integer> child2Seq = createOffspring(seq2, seq1, cutPoint1, cutPoint2);
        BinaryChromosome child1 = sequenceToChromosome(child1Seq);
        BinaryChromosome child2 = sequenceToChromosome(child2Seq);

        offspring.add(child1);
        offspring.add(child2);

        return offspring;
    }

    private List<Integer> createOffspring(List<Integer> parent1, List<Integer> parent2,
                                          int cutPoint1, int cutPoint2) {
        int size = parent1.size();
        List<Integer> offspring = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            offspring.add(-1);
        }

        Set<Integer> used = new HashSet<>();
        for (int i = cutPoint1; i <= cutPoint2; i++) {
            offspring.set(i, parent1.get(i));
            used.add(parent1.get(i));
        }

        int currentPos = (cutPoint2 + 1) % size;
        int parent2Pos = (cutPoint2 + 1) % size;

        while (offspring.contains(-1)) {
            int gene = parent2.get(parent2Pos);

            if (!used.contains(gene)) {
                offspring.set(currentPos, gene);
                used.add(gene);
                currentPos = (currentPos + 1) % size;
            }

            parent2Pos = (parent2Pos + 1) % size;
        }

        return offspring;
    }

    private BinaryChromosome sequenceToChromosome(List<Integer> sequence) {
        int size = sequence.size();
        BinaryChromosome chromosome = new BinaryChromosome();

        List<List<Boolean>> genes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<Boolean> row = new ArrayList<>();
            for (int j = 0; j < size; j++) {
                row.add(false);
            }
            genes.add(row);
        }

        for (int position = 0; position < size; position++) {
            int order = sequence.get(position);
            genes.get(order).set(position, true);
        }

        chromosome.setGenes(genes);
        return chromosome;
    }

    @Override
    public String toString() {
        return "OrderOneCrossover (OX1)";
    }
}