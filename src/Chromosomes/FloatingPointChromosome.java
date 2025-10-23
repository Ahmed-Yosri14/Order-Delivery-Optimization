package Chromosomes;

import Fitness.FitnessEvaluator;
import Helpers.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FloatingPointChromosome implements Chromosome {
    private List<Double> genes;
    private final FitnessEvaluator evaluator = FitnessEvaluator.getInstance();
    private final Random rand = new Random();

    public FloatingPointChromosome(List<Double> genes) {
        this.genes = new ArrayList<>(genes);
    }

    public FloatingPointChromosome() {
        this.genes = new ArrayList<>();
    }

    @Override
    public void mutateMethod1(double probability) {
        for (int i = 0; i < genes.size(); i++) {
            uniformMutation(i, probability);
        }
    }

    public void mutateMethod2(double probability,int currentGen, int maxGen) {
        for (int i = 0; i < genes.size(); i++) {
            nonUniformMutation(i, probability, currentGen, maxGen);
        }
    }

    @Override
    public int getFitness() {
        return (int) evaluator.evaluate(this);
    }

    @Override
    public Chromosome clone() {
        return new FloatingPointChromosome(this.genes);
    }

    @Override
    public int getTotalRouteTime() {
        return evaluator.calculateTotalRouteTime(this);
    }

    @Override
    public void generateGenes(int numberOfGenes) {
        if (genes == null) {
            genes = new ArrayList<>();
        }
        for (int i = 0; i < numberOfGenes; i++) {
            genes.add(rand.nextDouble());
        }
    }

    @Override
    public List<Integer> getDeliverySequence() {
        List<Integer> seq = new ArrayList<>();
        List<Pair> arr = new ArrayList<>();

        for (int i = 0; i < genes.size(); i++) {
            arr.add(new Pair(this.genes.get(i), i + 1));
        }

        Collections.sort(arr,Collections.reverseOrder());
        for (Pair p : arr) {
            seq.add(p.getIdx());
        }

        return seq;
    }
    private void uniformMutation(int idx, double probability) {
        if (rand.nextDouble() < probability) {
            double gene = genes.get(idx);
            double LB = 0.0;
            double UB = 1.0;

            // Generate random r11 ∈ [0, 1]
            double r11 = rand.nextDouble();

            double delta;
            boolean moveLeft;

            if (r11 <= 0.5) {
                delta = gene - LB;  // ΔL
                moveLeft = true;
            } else {
                delta = UB - gene;  // ΔU
                moveLeft = false;
            }

            // Generate r12 ∈ [0, Δ]
            double r12 = rand.nextDouble() * delta;

            if (moveLeft) {
                gene -= r12;
            } else {
                gene += r12;
            }

            // keep it between LB, UB
            gene = Math.max(LB, Math.min(UB, gene));

            genes.set(idx, gene);
        }
    }

    private void nonUniformMutation(int idx, double probability, int currentGen, int maxGen) {
        if (rand.nextDouble() < probability) {
            double gene = genes.get(idx);
            double a = 0.0;
            double b = 1.0;
            double r = rand.nextDouble();
            double bFactor = 5.0;
            double delta;

            if (rand.nextBoolean()) {
                double y = b - gene;
                delta = y * (1 - Math.pow(r, Math.pow(1.0 - (double) currentGen / maxGen, bFactor)));
                gene += delta;
            } else {
                double y = gene - a;
                delta = y * (1 - Math.pow(r, Math.pow(1.0 - (double) currentGen / maxGen, bFactor)));
                gene -= delta;
            }

            gene = Math.max(a, Math.min(b, gene));
            genes.set(idx, gene);
        }
    }

    public List<Double> getGenes() {
        return new ArrayList<>(genes);
    }
    @Override
    public String printGenes() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < genes.size(); i++) {
            sb.append(String.format("%.4f", genes.get(i)));
            if (i < genes.size() - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
