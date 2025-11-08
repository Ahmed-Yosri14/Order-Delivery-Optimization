package GeneticAlgorithm.Selection;

import GeneticAlgorithm.Chromosomes.Chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RouletteWheelSelection implements Selection {
    private static final Random random = new Random();

    public RouletteWheelSelection() {
    }

    @Override
    public Chromosome select(List<Chromosome> population) {
        double totalFitness = 0.0;
        List<Double> adjustedFitness = new ArrayList<>();

        for (Chromosome c : population) {
            double fitness = c.getFitness();

            adjustedFitness.add(Double.valueOf(fitness > 0 ? fitness : 0.1));
            totalFitness += adjustedFitness.get(adjustedFitness.size() - 1);
        }

        if (totalFitness == 0) {
            return population.get(random.nextInt(population.size()));
        }

        double randomValue = random.nextDouble() * totalFitness;
        double cumulativeFitness = 0.0;

        for (int i = 0; i < population.size(); i++) {
            cumulativeFitness += adjustedFitness.get(i);
            if (cumulativeFitness >= randomValue) {
                return population.get(i);
            }
        }

        // Fallback (should not reach here)
        return population.get(population.size() - 1);
    }

    @Override
    public List<Chromosome> selectMultiple(List<Chromosome> population, int count) {
        List<Chromosome> selected = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            selected.add(select(population));
        }

        return selected;
    }
}