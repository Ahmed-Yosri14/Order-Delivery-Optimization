//import Chromosomes.BinaryChromosome;
//import Chromosomes.Chromosome;
//import Fitness.BinaryFitnessEvaluator;
//import Selection.TournamentSelection;
//import Selection.RouletteWheelSelection;
//
//import java.util.*;
//
//public class TestSelection {
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//
//        System.out.println("=== Testing Selection Methods ===\n");
//
//        System.out.println("Please enter the number of orders");
//        int noOfOrders = sc.nextInt();
//
//        System.out.println("Please enter the population size");
//        int popSize = sc.nextInt();
//
//        System.out.println("Please enter the time constraint");
//        int timeConstraint = sc.nextInt();
//
//        System.out.println("Choose selection method:");
//        System.out.println("1 - Tournament Selection");
//        System.out.println("2 - Roulette Wheel Selection");
//        int selectionChoice = sc.nextInt();
//
//        int tournamentSize = 3;
//        if (selectionChoice == 1) {
//            System.out.println("Please enter tournament size");
//            tournamentSize = sc.nextInt();
//        }
//
//        // Generate distance matrix
//        ArrayList<ArrayList<Integer>> distMatrix = getDistanceBetweenAllPoints(noOfOrders + 1);
//
//        // Initialize population
//        Initializer initializer = new Initializer();
//        List<Chromosome> population = initializer.init(1, noOfOrders, popSize);
//
//        // Evaluate fitness
//        BinaryFitnessEvaluator evaluator = new BinaryFitnessEvaluator(distMatrix, timeConstraint);
//
//        System.out.println("\n=== Initial Population ===");
//        for (int i = 0; i < population.size(); i++) {
//            BinaryChromosome chromosome = (BinaryChromosome) population.get(i);
//            int fitness = evaluator.evaluate(chromosome);
//            chromosome.setFitness(Integer.valueOf(fitness));
//            int totalTime = evaluator.calculateTotalRouteTime(chromosome);
//
//            System.out.println("Chromosome " + (i + 1) + ": Fitness=" + fitness +
//                    ", Time=" + totalTime + ", Sequence=" + chromosome.getDeliverySequence());
//        }
//
//        // Test selection
//        System.out.println("\n=== Testing Selection (10 selections) ===");
//
//        if (selectionChoice == 1) {
//            TournamentSelection selection = new TournamentSelection(tournamentSize, evaluator);
//            testSelectionMethod(selection, population, evaluator, "Tournament (size=" + tournamentSize + ")");
//        } else {
//            RouletteWheelSelection selection = new RouletteWheelSelection(evaluator);
//            testSelectionMethod(selection, population, evaluator, "Roulette Wheel");
//        }
//
//        sc.close();
//    }
//
//    private static void testSelectionMethod(Selection.Selection selection,
//                                            List<Chromosome> population,
//                                            BinaryFitnessEvaluator evaluator,
//                                            String methodName) {
//        System.out.println("Method: " + methodName);
//
//        Map<String, Integer> selectionCount = new HashMap<>();
//
//        for (int i = 0; i < 10; i++) {
//            Chromosome selected = selection.select(population);
//            BinaryChromosome bc = (BinaryChromosome) selected;
//            String key = bc.getDeliverySequence().toString();
//            selectionCount.put(key, Integer.valueOf(selectionCount.getOrDefault(key, Integer.valueOf(0)) + 1));
//
//            System.out.println("Selection " + (i + 1) + ": Fitness=" + bc.getFitness() +
//                    ", Time=" + evaluator.calculateTotalRouteTime(bc) +
//                    ", Sequence=" + bc.getDeliverySequence());
//        }
//
//        System.out.println("\n=== Selection Frequency ===");
//        selectionCount.forEach((seq, count) ->
//                System.out.println(seq + " selected " + count + " times")
//        );
//    }
//
//    public static ArrayList<ArrayList<Integer>> getDistanceBetweenAllPoints(int n) {
//        ArrayList<ArrayList<Integer>> distanceMatrix = new ArrayList<>();
//        Random rand = new Random();
//
//        for (int i = 0; i < n; i++) {
//            distanceMatrix.add(new ArrayList<>());
//            for (int j = 0; j < n; j++) {
//                if (i == j) {
//                    distanceMatrix.get(i).add(Integer.valueOf(0));
//                } else if (j < i) {
//                    distanceMatrix.get(i).add(distanceMatrix.get(j).get(i));
//                } else {
//                    int distance = rand.nextInt(90) + 10;
//                    distanceMatrix.get(i).add(Integer.valueOf(distance));
//                }
//            }
//        }
//        return distanceMatrix;
//    }
//}