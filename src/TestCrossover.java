//import Chromosomes.BinaryChromosome;
//import Chromosomes.Chromosome;
//import Crossover.OrderOneCrossover;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//public class TestCrossover {
//    public static void main(String[] args) {
//        System.out.println("=== Testing Order-1 Crossover (OX1) ===\n");
//
//        // Test 1: Manual example from our discussion
//        System.out.println("Test 1: Manual Example");
//        System.out.println("-".repeat(60));
//        testManualExample();
//
//        // Test 2: Random chromosomes
//        System.out.println("\n\nTest 2: Random Chromosomes");
//        System.out.println("-".repeat(60));
//        testRandomChromosomes(5, 5);
//
//        System.out.println("\n\nTest 3: Larger chromosomes");
//        System.out.println("-".repeat(60));
//        testRandomChromosomes(8, 3);
//    }
//
//    private static void testManualExample() {
//        // Create Parent1: [1, 2, 3, 4, 5, 6, 7, 8, 9]
//        BinaryChromosome parent1 = createChromosomeFromSequence(
//                List.of(1, 2, 3, 4, 5, 6, 7, 8, 9)
//        );
//
//        // Create Parent2: [9, 3, 7, 8, 2, 6, 5, 1, 4]
//        BinaryChromosome parent2 = createChromosomeFromSequence(
//                List.of(9, 3, 7, 8, 2, 6, 5, 1, 4)
//        );
//
//        System.out.println("Parent1: " + parent1.getDeliverySequence());
//        System.out.println("Parent2: " + parent2.getDeliverySequence());
//
//        OrderOneCrossover crossover = new OrderOneCrossover();
//        List<Chromosome> offspring = crossover.crossover(parent1, parent2, 0.8);
//
//        BinaryChromosome child1 = (BinaryChromosome) offspring.get(0);
//        BinaryChromosome child2 = (BinaryChromosome) offspring.get(1);
//
//        System.out.println("\nChild1:  " + child1.getDeliverySequence());
//        System.out.println("Child2:  " + child2.getDeliverySequence());
//
//        // Validate
//        validateOffspring(child1, 9);
//        validateOffspring(child2, 9);
//    }
//
//    private static void testRandomChromosomes(int numOrders, int numTests) {
//        OrderOneCrossover crossover = new OrderOneCrossover();
//
//        for (int test = 1; test <= numTests; test++) {
//            System.out.println("\nTest " + test + ":");
//
//            BinaryChromosome parent1 = new BinaryChromosome();
//            parent1.generateGenes(numOrders);
//
//            BinaryChromosome parent2 = new BinaryChromosome();
//            parent2.generateGenes(numOrders);
//
//            System.out.println("Parent1: " + parent1.getDeliverySequence());
//            System.out.println("Parent2: " + parent2.getDeliverySequence());
//
//            List<Chromosome> offspring = crossover.crossover(parent1, parent2, 0.8);
//
//            BinaryChromosome child1 = (BinaryChromosome) offspring.get(0);
//            BinaryChromosome child2 = (BinaryChromosome) offspring.get(1);
//
//            System.out.println("Child1:  " + child1.getDeliverySequence());
//            System.out.println("Child2:  " + child2.getDeliverySequence());
//
//            // Validate
//            boolean valid1 = validateOffspring(child1, numOrders);
//            boolean valid2 = validateOffspring(child2, numOrders);
//
//            if (valid1 && valid2) {
//                System.out.println("✅ Both offspring are valid");
//            } else {
//                System.out.println("❌ Invalid offspring detected!");
//            }
//        }
//    }
//
//    private static BinaryChromosome createChromosomeFromSequence(List<Integer> sequence) {
//        int size = sequence.size();
//        BinaryChromosome chromosome = new BinaryChromosome();
//
//        List<List<Boolean>> genes = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            List<Boolean> row = new ArrayList<>();
//            for (int j = 0; j < size; j++) {
//                row.add(false);
//            }
//            genes.add(row);
//        }
//
//        for (int position = 0; position < size; position++) {
//            int order = sequence.get(position) - 1;
//            genes.get(order).set(position, true);
//        }
//
//        chromosome.setGenes(genes);
//        return chromosome;
//    }
//
//    private static boolean validateOffspring(BinaryChromosome chromosome, int expectedSize) {
//        List<Integer> sequence = chromosome.getDeliverySequence();
//
//        // Check size
//        if (sequence.size() != expectedSize) {
//            System.out.println("  ❌ Wrong size: " + sequence.size() + " (expected " + expectedSize + ")");
//            return false;
//        }
//
//        // Check all elements are unique
//        Set<Integer> uniqueElements = new HashSet<>(sequence);
//        if (uniqueElements.size() != expectedSize) {
//            System.out.println("  ❌ Duplicate elements found");
//            return false;
//        }
//
//        // Check all elements are in valid range
//        for (int order : sequence) {
//            if (order < 0 || order >= expectedSize) {
//                System.out.println("  ❌ Invalid order value: " + order);
//                return false;
//            }
//        }
//
//        System.out.println("  ✅ Valid permutation");
//        return true;
//    }
//}