package Selection;

import Chromosomes.Chromosome;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TournamentSelection implements Selection {
    private int tournamentSize;
    private static final Random random = new Random();

    public TournamentSelection(int tournamentSize) {
        if (tournamentSize < 1) {
            throw new IllegalArgumentException("Tournament size must be at least 1");
        }
        this.tournamentSize = tournamentSize;
    }

    @Override
    public Chromosome select(List<Chromosome> population) {
        validatePopulation(population);

        List<Chromosome> tournament = createTournament(population);
        return findTournamentWinner(tournament);
    }

    @Override
    public List<Chromosome> selectMultiple(List<Chromosome> population, int count) {
        validatePopulation(population);
        if (count < 1) {
            throw new IllegalArgumentException("Count must be at least 1");
        }

        return createMatingPool(population, count);
    }

    // Implementation that exactly follows your methodology
    public List<Chromosome> createMatingPool(List<Chromosome> population) {
        return createMatingPool(population, population.size());
    }

    public List<Chromosome> createMatingPool(List<Chromosome> population, int targetSize) {
        List<Chromosome> matingPool = new ArrayList<>();

        // Repeat till we have targetSize individuals in mating pool
        while (matingPool.size() < targetSize) {
            // Choose n individuals randomly
            List<Chromosome> tournament = createTournament(population);

            // Pick the one with highest fitness
            Chromosome winner = findTournamentWinner(tournament);

            // Place n copies of this individual in the mating pool
            for (int j = 0; j < tournamentSize && matingPool.size() < targetSize; j++) {
                matingPool.add(winner.clone());
            }
        }

        return matingPool;
    }

    // Helper method to create a tournament
    private List<Chromosome> createTournament(List<Chromosome> population) {
        List<Chromosome> tournament = new ArrayList<>();
        for (int j = 0; j < tournamentSize; j++) {
            int randomIndex = random.nextInt(population.size());
            tournament.add(population.get(randomIndex));
        }
        return tournament;
    }

    // Helper method to find the tournament winner
    private Chromosome findTournamentWinner(List<Chromosome> tournament) {
        Chromosome best = tournament.get(0);
        int bestTime = best.getTotalRouteTime();

        for (int i = 1; i < tournament.size(); i++) {
            Chromosome current = tournament.get(i);
            int currentTime = current.getTotalRouteTime();

            if (current.getFitness() > best.getFitness() ||
                    (current.getFitness() == best.getFitness() && currentTime < bestTime)) {
                best = current;
                bestTime = currentTime;
            }
        }
        return best;
    }

    // Validation method
    private void validatePopulation(List<Chromosome> population) {
        if (population == null || population.isEmpty()) {
            throw new IllegalArgumentException("Population cannot be null or empty");
        }
        if (tournamentSize > population.size()) {
            throw new IllegalArgumentException(
                    "Tournament size (" + tournamentSize + ") cannot be larger than population size (" + population.size() + ")");
        }
    }

    public void setTournamentSize(int tournamentSize) {
        if (tournamentSize < 1) {
            throw new IllegalArgumentException("Tournament size must be at least 1");
        }
        this.tournamentSize = tournamentSize;
    }

    public int getTournamentSize() {
        return tournamentSize;
    }

    @Override
    public String toString() {
        return "TournamentSelection{tournamentSize=" + tournamentSize + "}";
    }
}