package Crossover;

import Chromosomes.Chromosome;
import Chromosomes.FloatingPointChromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FloatingPointUniformCrossover implements Crossover {

    private static final Random random = new Random();

    @Override
    public List<Chromosome> crossover(Chromosome parent1, Chromosome parent2, double probability) {
        if (!(parent1 instanceof FloatingPointChromosome) || !(parent2 instanceof FloatingPointChromosome)) {
            throw new IllegalArgumentException("FloatingPointCrossover only works with FloatingPointChromosome");
        }
        List<Chromosome> offspring = new ArrayList<>();

        if (random.nextDouble() > probability) {
            offspring.add(parent1.clone());
            offspring.add(parent2.clone());
            return offspring;
        }

        FloatingPointChromosome p1 = (FloatingPointChromosome) parent1;
        FloatingPointChromosome p2 = (FloatingPointChromosome) parent2;

        List<Double> g1 = p1.getGenes();
        List<Double> g2 = p2.getGenes();

        if (g1.size() != g2.size()) {
            throw new IllegalArgumentException("Parents must have same gene length");
        }

        List<Double> c1 = new ArrayList<>(g1.size());
        List<Double> c2 = new ArrayList<>(g2.size());

        for (int i = 0; i < g1.size(); i++) {
            double alpha = random.nextDouble();
            if (alpha >= 0.5) {
                c1.add(g1.get(i));
                c2.add(g2.get(i));
            } else {
                c1.add(g2.get(i));
                c2.add(g1.get(i));
            }
        }

        offspring.add(new FloatingPointChromosome(c1));
        offspring.add(new FloatingPointChromosome(c2));
        return offspring;
    }
}


