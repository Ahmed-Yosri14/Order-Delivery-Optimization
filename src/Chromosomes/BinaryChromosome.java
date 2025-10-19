package Chromosomes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BinaryChromosome implements Chromosome {
    private List<List<Boolean>> genes; // 2D array: rows=orders, cols=positions
    private Integer fitness;
    private static final Random random = new Random();

    public BinaryChromosome() {
        this.genes = new ArrayList<>();
        this.fitness = Integer.valueOf(0);
    }

    // Constructor for deep copy
    public BinaryChromosome(List<List<Boolean>> genes) {
        this.genes = new ArrayList<>();
        for (List<Boolean> row : genes) {
            List<Boolean> newRow = new ArrayList<>();
            for (Boolean value : row) {
                newRow.add(Boolean.valueOf(value.booleanValue()));
            }
            this.genes.add(newRow);
        }
    }

    @Override
    public Chromosome copy() {
        // Create a deep copy of the 2D genes array
        List<List<Boolean>> copiedGenes = new ArrayList<>();
        for (List<Boolean> row : this.genes) {
            List<Boolean> copiedRow = new ArrayList<>();
            for (Boolean value : row) {
                copiedRow.add(Boolean.valueOf(value.booleanValue()));
            }
            copiedGenes.add(copiedRow);
        }

        BinaryChromosome copy = new BinaryChromosome(copiedGenes);

        // Copy the fitness value
        if (this.fitness != null) {
            copy.setFitness(Integer.valueOf(this.fitness.intValue()));
        }

        return copy;
    }

    @Override
    public void generateGenes(int numberOfGenes) {
        genes.clear();

        // Initialize 2D array with all false
        for (int i = 0; i < numberOfGenes; i++) {
            List<Boolean> row = new ArrayList<>();
            for (int j = 0; j < numberOfGenes; j++) {
                row.add(Boolean.valueOf(false));
            }
            genes.add(row);
        }

        // Create a random permutation of ALL orders
        List<Integer> orderIndices = new ArrayList<>();
        for (int i = 0; i < numberOfGenes; i++) {
            orderIndices.add(Integer.valueOf(i));
        }

        // Shuffle to create random permutation
        Collections.shuffle(orderIndices, random);

        // Assign each order to its position in the permutation
        for (int position = 0; position < numberOfGenes; position++) {
            int orderIndex = orderIndices.get(position).intValue();
            genes.get(orderIndex).set(position, Boolean.valueOf(true));
        }
    }

    @Override
    public void mutate(double probability) {
        // Mutation implementation will be added later
    }

    // Getters
    public List<List<Boolean>> getGenes() {
        return genes;
    }

    public void setGenes(List<List<Boolean>> genes) {
        this.genes = new ArrayList<>();
        for (List<Boolean> row : genes) {
            List<Boolean> newRow = new ArrayList<>();
            for (Boolean value : row) {
                newRow.add(Boolean.valueOf(value.booleanValue()));
            }
            this.genes.add(newRow);
        }
    }

    public Integer getFitness() {
        return fitness;
    }

    public void setFitness(Integer fitness) {
        this.fitness = fitness;
    }

    // Get delivery sequence as list of order indices
    public List<Integer> getDeliverySequence() {
        List<Integer> sequence = new ArrayList<>();
        int numOrders = genes.size();

        // For each position (column)
        for (int position = 0; position < numOrders; position++) {
            // Find which order (row) has 1 at this position
            for (int order = 0; order < numOrders; order++) {
                if (genes.get(order).get(position).booleanValue()) {
                    sequence.add(Integer.valueOf(order));
                    break;
                }
            }
        }

        return sequence;
    }

    // Get number of orders to be delivered
    public int getNumberOfDeliveries() {
        return getDeliverySequence().size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("BinaryChromosome{\n");

        for (int i = 0; i < genes.size(); i++) {
            sb.append("  Order ").append(i).append(": [");
            for (int j = 0; j < genes.get(i).size(); j++) {
                sb.append(genes.get(i).get(j).booleanValue() ? "1" : "0");
                if (j < genes.get(i).size() - 1) sb.append(", ");
            }
            sb.append("]\n");
        }

        sb.append("  Delivery sequence: ").append(getDeliverySequence()).append("\n");
        sb.append("  Number of deliveries: ").append(getNumberOfDeliveries()).append("\n");
        sb.append("  Fitness: ").append(fitness).append("\n");
        sb.append("}");

        return sb.toString();
    }

    // Optional: Override clone() method as well
    @Override
    public BinaryChromosome clone() {
        return (BinaryChromosome) this.copy();
    }
}