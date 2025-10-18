package Selection;

import Chromosomes.BinaryChromosome;
import Chromosomes.Chromosome;
import Fitness.BinaryFitnessEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TournamentSelection implements Selection {
    private int tournamentSize;
    private BinaryFitnessEvaluator fitnessEvaluator;
    private static final Random random = new Random();

    public TournamentSelection(int tournamentSize, BinaryFitnessEvaluator fitnessEvaluator) {
        this.tournamentSize = tournamentSize;
        this.fitnessEvaluator = fitnessEvaluator;
    }

    @Override
    public Chromosome select(List<Chromosome> population) {
        // Randomly pick tournamentSize chromosomes
        List<Chromosome> tournament = new ArrayList<>();

        for (int i = 0; i < tournamentSize; i++) {
            int randomIndex = random.nextInt(population.size());
            tournament.add(population.get(randomIndex));
        }

        // Find the best chromosome in the tournament
        BinaryChromosome best = (BinaryChromosome) tournament.get(0);
        int bestTime = fitnessEvaluator.calculateTotalRouteTime(best);

        for (int i = 1; i < tournament.size(); i++) {
            BinaryChromosome current = (BinaryChromosome) tournament.get(i);
            int currentTime = fitnessEvaluator.calculateTotalRouteTime(current);

            // Better if: higher fitness OR (same fitness but shorter time)
            if (current.getFitness() > best.getFitness() ||
                    (current.getFitness() == best.getFitness() && currentTime < bestTime)) {
                best = current;
                bestTime = currentTime;
            }
        }

        return best;
    }

    @Override
    public List<Chromosome> selectMultiple(List<Chromosome> population, int count) {
        List<Chromosome> selected = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            selected.add(select(population));
        }

        return selected;
    }

    public void setTournamentSize(int tournamentSize) {
        this.tournamentSize = tournamentSize;
    }

    public int getTournamentSize() {
        return tournamentSize;
    }
}