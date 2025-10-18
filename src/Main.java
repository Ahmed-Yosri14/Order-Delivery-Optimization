import Chromosomes.BinaryChromosome;
import Chromosomes.Chromosome;
import Fitness.*;

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
            FitnessEvaluator evaluator = new BinaryFitnessEvaluator(distMatrix, timeConstraint);

            for (int i = 0; i < population.size(); i++) {
                BinaryChromosome chromosome = (BinaryChromosome) population.get(i);

                // Calculate fitness
                Integer fitness = evaluator.evaluate(chromosome);
                chromosome.setFitness(fitness);

                // Calculate total route time
                int totalTime = ((BinaryFitnessEvaluator) evaluator).calculateTotalRouteTime(chromosome);

                System.out.println("\nChromosome " + (i + 1) + ":");
                System.out.println(chromosome);
                System.out.println("  Total route time: " + totalTime + " (constraint: " + timeConstraint + ")");
                System.out.println("  Orders deliverable within time: " + (int)fitness + "/" + noOfOrders);
            }

            // Find best chromosome
            BinaryChromosome best = (BinaryChromosome) population.get(0);
            for (Chromosome c : population) {
                BinaryChromosome bc = (BinaryChromosome) c;
                if (bc.getFitness() > best.getFitness()) {
                    best = bc;
                }
            }

            System.out.println("\n=== Best Initial Solution ===");
            System.out.println("Fitness: " + best.getFitness() + " orders");
            System.out.println("Delivery sequence: " + best.getDeliverySequence());
        } else {
            // Display other chromosome types
            for (int i = 0; i < population.size(); i++) {
                System.out.println("\nChromosome " + (i + 1) + ":");
                System.out.println(population.get(i));
            }
        }

        sc.close();
    }

    public static ArrayList<ArrayList<Integer>> getDistanceBetweenAllPoints(int n) {
        ArrayList<ArrayList<Integer>> distanceMatrix = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < n; i++) {
            distanceMatrix.add(new ArrayList<>());
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    distanceMatrix.get(i).add(Integer.valueOf(0));
                } else if (j < i) {
                    distanceMatrix.get(i).add(distanceMatrix.get(j).get(i));
                } else {
                    int distance = rand.nextInt(90) + 10;
                    distanceMatrix.get(i).add(Integer.valueOf(distance));
                }
            }
        }
        return distanceMatrix;
    }

    public static void printDistanceMatrix(ArrayList<ArrayList<Integer>> matrix) {
        System.out.print("     ");
        for (int i = 0; i < matrix.size(); i++) {
            System.out.printf("%4d", Integer.valueOf(i));
        }
        System.out.println();

        for (int i = 0; i < matrix.size(); i++) {
            System.out.printf("%4d:", Integer.valueOf(i));
            for (int j = 0; j < matrix.get(i).size(); j++) {
                System.out.printf("%4d", matrix.get(i).get(j));
            }
            System.out.println();
        }
    }
}