package Crossover;

import Chromosomes.Chromosome;
import Chromosomes.IntegerChromosome;
import java.util.*;

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

        int size = seq1.size();

        Map<Integer, Integer> idxP1 = new HashMap<>();
        Map<Integer, Integer> idxP2 = new HashMap<>();
        for (int i = 0; i < size; i++) {
            idxP1.put(seq1.get(i), i);
            idxP2.put(seq2.get(i), i);
        }

        List<Integer> child1 = new ArrayList<>(Collections.nCopies(size, null));
        List<Integer> child2 = new ArrayList<>(Collections.nCopies(size, null));

        for (int gene = 0; gene < size; gene++) {
            double alpha = random.nextDouble();
            if (alpha >= 0.5) {
                int pos1 = idxP2.get(gene + 1); // assuming genes are 1..n
                pos1 = findNextEmptySlot(child1, pos1);
                child1.set(pos1, gene + 1);

                int pos2 = idxP1.get(gene + 1);
                pos2 = findNextEmptySlot(child2, pos2);
                child2.set(pos2, gene + 1);
            }
        }

        fillRemaining(child1, size);
        fillRemaining(child2, size);

        offspring.add(new IntegerChromosome(child1));
        offspring.add(new IntegerChromosome(child2));

        return offspring;
    }

    private int findNextEmptySlot(List<Integer> list, int startIdx) {
        int n = list.size();
        int idx = startIdx;
        for (int i = 0; i < n; i++) {
            if (list.get(idx) == null) return idx;
            idx = (idx + 1) % n;
        }
        return startIdx;
    }

    private void fillRemaining(List<Integer> child, int size) {
        Set<Integer> present = new HashSet<>(child);
        List<Integer> missing = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            if (!present.contains(i)) missing.add(i);
        }
        int mIdx = 0;
        for (int i = 0; i < size; i++) {
            if (child.get(i) == null) {
                child.set(i, missing.get(mIdx++));
            }
        }
    }
}
