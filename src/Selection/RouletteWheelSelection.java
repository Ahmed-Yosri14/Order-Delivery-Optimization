package Selection;

import Chromosomes.BinaryChromosome;
import Chromosomes.Chromosome;
import Fitness.BinaryFitnessEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RouletteWheelSelection implements Selection {
    private BinaryFitnessEvaluator fitnessEvaluator;
    private static final Random random = new Random();

    public RouletteWheelSelection(BinaryFitnessEvaluator fitnessEvaluator) {
        this.fitnessEvaluator = fitnessEvaluator;
    }

    @Override
    public Chromosome select(List<Chromosome> population) {
        // Calculate total fitness of population
        double totalFitness = 0.0;
        List<Double> adjustedFitness = new ArrayList<>();

        for (Chromosome c : population) {
            BinaryChromosome bc = (BinaryChromosome) c;
            double fitness = bc.getFitness();

            // If fitness is 0 for all, give small probability to everyone
            // Otherwise use actual fitness
            adjustedFitness.add(Double.valueOf(fitness > 0 ? fitness : 0.1));
            totalFitness += adjustedFitness.get(adjustedFitness.size() - 1);
        }

        // Handle edge case where all fitness values are 0
        if (totalFitness == 0) {
            return population.get(random.nextInt(population.size()));
        }

        // Spin the roulette wheel
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