package Chromosomes;

import Fitness.FloatingPointFitnessEvaluator;
import Helpers.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FloatingPointChromosome implements Chromosome {
    List<Double> genes;
    FloatingPointFitnessEvaluator evaluator = FloatingPointFitnessEvaluator.getInstance();

    public FloatingPointChromosome(List<Double> genes) {
        this.genes = new ArrayList<>(genes);
    }
    @Override
    public void mutateMethod1(double probability) {

    }

    @Override
    public void mutateMethod2(double probability) {

    }

    @Override
    public int getFitness() {
        return evaluator.evaluate(this);
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
        for (int i = 0; i < numberOfGenes; i++) {
            genes.add(Double.valueOf(Math.random()));
        }
    }

    @Override
    public List<Integer> getDeliverySequence() {
        List<Integer>seq;
        seq = new ArrayList<Integer>();
        ArrayList<Pair> arr = new ArrayList<Pair>();
        for (int i = 0; i < genes.size(); i++) {
            arr.add(new Pair (this.genes.get(i),i+1));
        }
        Collections.sort(arr);
        for (int i = 0; i < arr.size(); i++) {
            seq.add(arr.get(i).getIdx());
        }
        return seq;
    }



}
