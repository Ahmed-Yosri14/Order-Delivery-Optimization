import Chromosomes.BinaryChromosome;
import Chromosomes.Chromosome;
import Fitness.*;
import Selection.*;
import Crossover.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose the Chromosome type");
        System.out.println("1-Binary \n2-Integer\n3-Floating Point");
        int choice, noOfOrders, popSize, timeConstraint;

        choice = sc.nextInt();

        System.out.println("Please enter the number of orders");
        noOfOrders = sc.nextInt();

        System.out.println("Please enter the population size");
        popSize = sc.nextInt();

        System.out.println("Please enter the time constraint");
        timeConstraint = sc.nextInt();

        // Generate distance matrix (n+1 for depot + orders)
        ArrayList<ArrayList<Integer>> distMatrix = getDistanceBetweenAllPoints(noOfOrders + 1);

        System.out.println("\n=== Distance Matrix ===");
        printDistanceMatrix(distMatrix);

        // Initialize population
        Initializer initializer = new Initializer();
        List<Chromosome> population = initializer.init(choice, noOfOrders, popSize);

        System.out.println("\n=== Initial Population ===");

        // For binary chromosomes, evaluate fitness
        if (choice == 1) {
            BinaryFitnessEvaluator evaluator = new BinaryFitnessEvaluator(distMatrix, timeConstraint);

            for (int i = 0; i < population.size(); i++) {
                BinaryChromosome chromosome = (BinaryChromosome) population.get(i);

                // Calculate fitness
                Integer fitness = evaluator.evaluate(chromosome);
                chromosome.setFitness(fitness);

                // Calculate total route time
                int totalTime = evaluator.calculateTotalRouteTime(chromosome);

                System.out.println("\nChromosome " + (i + 1) + ":");
                System.out.println(chromosome);
                System.out.println("  Total route time: " + totalTime + " (constraint: " + timeConstraint + ")");
                System.out.println("  Orders deliverable within time: " + fitness + "/" + noOfOrders);
            }

            // Find best chromosome
            BinaryChromosome best = (BinaryChromosome) population.get(0);
            int bestTime = evaluator.calculateTotalRouteTime(best);

            for (Chromosome c : population) {
                BinaryChromosome bc = (BinaryChromosome) c;
                int bcTime = evaluator.calculateTotalRouteTime(bc);

                // Better if: higher fitness OR (same fitness but shorter time)
                if (bc.getFitness() > best.getFitness() ||
                        (bc.getFitness() == best.getFitness() && bcTime < bestTime)) {
                    best = bc;
                    bestTime = bcTime;
                }
            }

            System.out.println("\n=== Best Initial Solution ===");
            System.out.println("Fitness: " + best.getFitness() + " orders");
            System.out.println("Total time: " + bestTime);
            System.out.println("Delivery sequence: " + best.getDeliverySequence());

            // Run Genetic Algorithm
            System.out.println("\n=== Run Genetic Algorithm? ===");
            System.out.println("1 - Yes");
            System.out.println("2 - No (just testing)");
            int runGA = sc.nextInt();

            if (runGA == 1) {
                runGeneticAlgorithm(population, evaluator, sc);
            } else {
                // Test Selection Methods
                System.out.println("\n=== Test Selection Methods? ===");
                System.out.println("1 - Yes, test Tournament Selection");
                System.out.println("2 - Yes, test Roulette Wheel Selection");
                System.out.println("3 - No, skip");
                int selectionChoice = sc.nextInt();

                if (selectionChoice == 1 || selectionChoice == 2) {
                    int numSelections = 10;
                    System.out.println("How many selections to perform? (default 10)");
                    numSelections = sc.nextInt();

                    if (selectionChoice == 1) {
                        System.out.println("Please enter tournament size");
                        int tournamentSize = sc.nextInt();
                        TournamentSelection selection = new TournamentSelection(tournamentSize, evaluator);
                        testSelectionMethod(selection, population, evaluator,
                                "Tournament (size=" + tournamentSize + ")", numSelections);
                    } else {
                        RouletteWheelSelection selection = new RouletteWheelSelection(evaluator);
                        testSelectionMethod(selection, population, evaluator,
                                "Roulette Wheel", numSelections);
                    }
                }

                // Test Mutation
                System.out.println("\n=== Test Mutation? ===");
                System.out.println("1 - Yes, test Swap Mutation");
                System.out.println("2 - No, skip");
                int mutationChoice = sc.nextInt();

                if (mutationChoice == 1) {
                    System.out.println("Enter mutation probability (0.0 to 1.0, e.g., 0.3)");
                    double mutationProb = sc.nextDouble();

                    System.out.println("How many chromosomes to mutate? (default 5)");
                    int numToMutate = sc.nextInt();

                    testMutation(population, evaluator, mutationProb, numToMutate);
                }
            }

        } else {
            // Display other chromosome types
            for (int i = 0; i < population.size(); i++) {
                System.out.println("\nChromosome " + (i + 1) + ":");
                System.out.println(population.get(i));
            }
        }

        sc.close();
    }

    private static void runGeneticAlgorithm(List<Chromosome> population,
                                            BinaryFitnessEvaluator evaluator,
                                            Scanner sc) {
        System.out.println("\n=== Genetic Algorithm Configuration ===");

        System.out.println("Number of generations:");
        int generations = sc.nextInt();

        System.out.println("Selection method (1-Tournament, 2-Roulette):");
        int selectionType = sc.nextInt();

        int tournamentSize = 3;
        if (selectionType == 1) {
            System.out.println("Tournament size:");
            tournamentSize = sc.nextInt();
        }

        System.out.println("Crossover probability (0.0-1.0, e.g., 0.8):");
        double crossoverProb = sc.nextDouble();

        System.out.println("Mutation probability (0.0-1.0, e.g., 0.1):");
        double mutationProb = sc.nextDouble();

        // Create operators
        Selection selection = selectionType == 1
                ? new TournamentSelection(tournamentSize, evaluator)
                : new RouletteWheelSelection(evaluator);

        Crossover crossover = new OrderOneCrossover();

        // Track best solution
        BinaryChromosome bestOverall = (BinaryChromosome) population.get(0);
        int bestOverallTime = evaluator.calculateTotalRouteTime(bestOverall);

        System.out.println("\n=== Starting Evolution ===\n");

        for (int gen = 0; gen < generations; gen++) {
            List<Chromosome> newPopulation = new ArrayList<>();

            // Elitism: keep best chromosome
            BinaryChromosome best = findBest(population, evaluator);
            newPopulation.add(best.copy());

            // Generate rest of population
            while (newPopulation.size() < population.size()) {
                // Selection
                Chromosome parent1 = selection.select(population);
                Chromosome parent2 = selection.select(population);

                // Crossover
                List<Chromosome> offspring = crossover.crossover(parent1, parent2, crossoverProb);

                // Mutation
                for (Chromosome child : offspring) {
                    child.mutate(mutationProb);

                    // Evaluate fitness
                    BinaryChromosome bc = (BinaryChromosome) child;
                    int fitness = evaluator.evaluate(bc);
                    bc.setFitness(fitness);

                    if (newPopulation.size() < population.size()) {
                        newPopulation.add(child);
                    }
                }
            }

            // Update population
            population = newPopulation;

            // Find best in current generation
            BinaryChromosome currentBest = findBest(population, evaluator);
            int currentBestTime = evaluator.calculateTotalRouteTime(currentBest);

            // Update overall best
            if (currentBest.getFitness() > bestOverall.getFitness() ||
                    (currentBest.getFitness() == bestOverall.getFitness() && currentBestTime < bestOverallTime)) {
                bestOverall = (BinaryChromosome) currentBest.copy();
                bestOverallTime = currentBestTime;
            }

            // Print progress every 10 generations
            if ((gen + 1) % 10 == 0 || gen == 0 || gen == generations - 1) {
                System.out.println("Generation " + (gen + 1) + ": Best Fitness=" +
                        currentBest.getFitness() + ", Time=" + currentBestTime +
                        ", Sequence=" + currentBest.getDeliverySequence());
            }
        }

        System.out.println("\n=== Final Best Solution ===");
        System.out.println("Fitness: " + bestOverall.getFitness() + " orders");
        System.out.println("Total time: " + bestOverallTime);
        System.out.println("Delivery sequence: " + bestOverall.getDeliverySequence());
    }

    private static BinaryChromosome findBest(List<Chromosome> population, BinaryFitnessEvaluator evaluator) {
        BinaryChromosome best = (BinaryChromosome) population.get(0);
        int bestTime = evaluator.calculateTotalRouteTime(best);

        for (Chromosome c : population) {
            BinaryChromosome bc = (BinaryChromosome) c;
            int bcTime = evaluator.calculateTotalRouteTime(bc);

            if (bc.getFitness() > best.getFitness() ||
                    (bc.getFitness() == best.getFitness() && bcTime < bestTime)) {
                best = bc;
                bestTime = bcTime;
            }
        }

        return best;
    }

    private static void testSelectionMethod(Selection selection,
                                            List<Chromosome> population,
                                            BinaryFitnessEvaluator evaluator,
                                            String methodName,
                                            int numSelections) {
        System.out.println("\n=== Testing Selection ===");
        System.out.println("Method: " + methodName);

        Map<String, Integer> selectionCount = new HashMap<>();

        for (int i = 0; i < numSelections; i++) {
            Chromosome selected = selection.select(population);
            BinaryChromosome bc = (BinaryChromosome) selected;
            String key = bc.getDeliverySequence().toString();
            selectionCount.put(key, selectionCount.getOrDefault(key, 0) + 1);

            System.out.println("Selection " + (i + 1) + ": Fitness=" + bc.getFitness() +
                    ", Time=" + evaluator.calculateTotalRouteTime(bc) +
                    ", Sequence=" + bc.getDeliverySequence());
        }

        System.out.println("\n=== Selection Frequency ===");
        selectionCount.forEach((seq, count) ->
                System.out.println(seq + " selected " + count + " times")
        );
    }

    private static void testMutation(List<Chromosome> population,
                                     BinaryFitnessEvaluator evaluator,
                                     double mutationProb,
                                     int numToMutate) {
        System.out.println("\n=== Testing Mutation ===");
        System.out.println("Mutation probability: " + mutationProb);
        System.out.println("Number of chromosomes to test: " + numToMutate);

        int mutationCount = 0;

        for (int i = 0; i < numToMutate && i < population.size(); i++) {
            BinaryChromosome original = (BinaryChromosome) population.get(i);
            BinaryChromosome mutated = original.clone();

            System.out.println("\n--- Chromosome " + (i + 1) + " ---");
            System.out.println("Before: " + original.getDeliverySequence() +
                    " (Fitness=" + original.getFitness() +
                    ", Time=" + evaluator.calculateTotalRouteTime(original) + ")");

            mutated.mutate(mutationProb);

            // Re-evaluate fitness after mutation
            int newFitness = evaluator.evaluate(mutated);
            mutated.setFitness(newFitness);

            System.out.println("After:  " + mutated.getDeliverySequence() +
                    " (Fitness=" + mutated.getFitness() +
                    ", Time=" + evaluator.calculateTotalRouteTime(mutated) + ")");

            if (!original.getDeliverySequence().equals(mutated.getDeliverySequence())) {
                System.out.println("✅ Mutation occurred");
                mutationCount++;
            } else {
                System.out.println("❌ No mutation (probability based)");
            }
        }

        System.out.println("\n=== Mutation Summary ===");
        System.out.println("Mutations occurred: " + mutationCount + "/" + numToMutate);
        System.out.println("Actual rate: " + (mutationCount * 100.0 / numToMutate) + "%");
    }

    public static ArrayList<ArrayList<Integer>> getDistanceBetweenAllPoints(int n) {
        ArrayList<ArrayList<Integer>> distanceMatrix = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            distanceMatrix.add(new ArrayList<>());
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    distanceMatrix.get(i).add(0);
                } else if (j < i) {
                    distanceMatrix.get(i).add(distanceMatrix.get(j).get(i));
                } else {
                    int distance = rand.nextInt(90) + 10;
                    distanceMatrix.get(i).add(distance);
                }
            }
        }
        return distanceMatrix;
    }

    public static void printDistanceMatrix(ArrayList<ArrayList<Integer>> matrix) {
        System.out.print("     ");
        for (int i = 0; i < matrix.size(); i++) {
            System.out.printf("%4d", i);
        }
        System.out.println();

        for (int i = 0; i < matrix.size(); i++) {
            System.out.printf("%4d:", i);
            for (int j = 0; j < matrix.get(i).size(); j++) {
                System.out.printf("%4d", matrix.get(i).get(j));
            }
            System.out.println();
        }
    }
}