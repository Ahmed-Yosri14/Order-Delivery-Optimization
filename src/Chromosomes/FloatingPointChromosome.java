package Chromosomes;

import Fitness.FloatingPointFitnessEvaluator;
import Helpers.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class FloatingPointChromosome implements Chromosome {
    List<Double> genes;
    FloatingPointFitnessEvaluator evaluator = FloatingPointFitnessEvaluator.getInstance();
    Random rand = new Random();
    public FloatingPointChromosome(List<Double> genes) {
        this.genes = new ArrayList<>(genes);
    }
    public FloatingPointChromosome(){
        this.genes = new ArrayList<>();
    }
    @Override
    public void mutateMethod1(double probability) {
        for (int i = 0; i < genes.size(); i++) {
            mutateByAverage(i, probability);
        }
    }

    @Override
    public void mutateMethod2(double probability) {
        for (int i = 0; i < genes.size(); i++) {
            mutateByInvertingFraction(i, probability);
        }
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
        if (genes == null) {
            genes = new ArrayList<>();
        }
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
    void mutateByInvertingFraction(int idx , Double probability) {
        if (rand.nextDouble()<probability){
            Double val = 1-genes.get(idx);
            genes.set(idx,val);
        }
    }
    void mutateByAverage(int idx , Double probability) {
        if (rand.nextDouble()<probability){
            Double val = rand.nextDouble()+genes.get(idx);
            val/=2;
            genes.set(idx,val);
        }
    }

    public List<Double> getGenes() {
        return new ArrayList<>(genes);
    }



}
