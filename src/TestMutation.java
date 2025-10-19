import Chromosomes.BinaryChromosome;

public class TestMutation {
    public static void main(String[] args) {
        System.out.println("=== Testing Swap Mutation ===\n");

        int numOrders = 5;
        double mutationProbability = 1; // 100% to guarantee mutation for testing

        System.out.println("Testing with " + numOrders + " orders");
        System.out.println("Mutation probability: " + mutationProbability);
        System.out.println("-".repeat(60));

        // Test multiple times
        for (int test = 1; test <= 5; test++) {
            System.out.println("\nTest " + test + ":");

            // Create a chromosome
            BinaryChromosome chromosome = new BinaryChromosome();
            chromosome.generateGenes(numOrders);

            System.out.println("Before mutation:");
            System.out.println("  Sequence: " + chromosome.getDeliverySequence());
            printChromosome(chromosome);

            // Apply mutation
            chromosome.mutate(mutationProbability);

            System.out.println("After mutation:");
            System.out.println("  Sequence: " + chromosome.getDeliverySequence());
            printChromosome(chromosome);

            // Validate
            if (isValidChromosome(chromosome, numOrders)) {
                System.out.println("  ✅ Mutation preserved validity");
            } else {
                System.out.println("  ❌ Mutation violated constraints!");
            }
        }

        // Test with lower probability
        System.out.println("\n" + "=".repeat(60));
        System.out.println("Testing with probability 0.3 (30 mutations expected out of 100)");
        System.out.println("=".repeat(60));

        int mutationCount = 0;
        int trials = 100;

        for (int i = 0; i < trials; i++) {
            BinaryChromosome chromosome = new BinaryChromosome();
            chromosome.generateGenes(numOrders);

            String before = chromosome.getDeliverySequence().toString();
            chromosome.mutate(0.1);
            String after = chromosome.getDeliverySequence().toString();

            if (!before.equals(after)) {
                mutationCount++;
            }
        }

        System.out.println("Mutations occurred: " + mutationCount + "/" + trials);
        System.out.println("Actual rate: " + (mutationCount * 100.0 / trials) + "%");
    }

    private static void printChromosome(BinaryChromosome chromosome) {
        for (int i = 0; i < chromosome.getGenes().size(); i++) {
            System.out.print("    Order " + i + ": [");
            for (int j = 0; j < chromosome.getGenes().get(i).size(); j++) {
                System.out.print(chromosome.getGenes().get(i).get(j) ? "1" : "0");
                if (j < chromosome.getGenes().get(i).size() - 1) System.out.print(", ");
            }
            System.out.println("]");
        }
    }

    private static boolean isValidChromosome(BinaryChromosome chromosome, int numOrders) {
        // Check 1: Each row has exactly one 1
        for (int i = 0; i < numOrders; i++) {
            int count = 0;
            for (int j = 0; j < numOrders; j++) {
                if (chromosome.getGenes().get(i).get(j)) count++;
            }
            if (count != 1) return false;
        }

        // Check 2: Each column has exactly one 1
        for (int j = 0; j < numOrders; j++) {
            int count = 0;
            for (int i = 0; i < numOrders; i++) {
                if (chromosome.getGenes().get(i).get(j)) count++;
            }
            if (count != 1) return false;
        }

        // Check 3: All orders delivered
        if (chromosome.getNumberOfDeliveries() != numOrders) return false;

        return true;
    }
}