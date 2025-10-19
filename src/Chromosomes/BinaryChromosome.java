package Chromosomes;

import Fitness.BinaryFitnessEvaluator;
import Fitness.FitnessEvaluator;
import Fitness.IntegerFitnessEvaluator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BinaryChromosome implements Chromosome {
    private List<List<Boolean>> genes;
    private Integer fitness;
    private static final Random random = new Random();
    BinaryFitnessEvaluator evaluator = BinaryFitnessEvaluator.getInstance();

    //Constructors
    public BinaryChromosome() {
        this.genes = new ArrayList<>();
        this.fitness = Integer.valueOf(0);
    }
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
    public List<Integer> getDeliverySequence() {
        List<Integer> sequence = new ArrayList<>();
        int numOrders = genes.size();

        for (int position = 0; position < numOrders; position++) {
            for (int order = 0; order < numOrders; order++) {
                if (genes.get(order).get(position).booleanValue()) {
                    sequence.add(Integer.valueOf(order));
                    break;
                }
            }
        }

        return sequence;
    }
    @Override
    public void generateGenes(int numberOfGenes) {
        genes.clear();
        for (int i = 0; i < numberOfGenes; i++) {
            List<Boolean> row = new ArrayList<>();
            for (int j = 0; j < numberOfGenes; j++) {
                row.add(Boolean.valueOf(false));
            }
            genes.add(row);
        }
        List<Integer> orderIndices = new ArrayList<>();
        for (int i = 0; i < numberOfGenes; i++) {
            orderIndices.add(Integer.valueOf(i));
        }
        Collections.shuffle(orderIndices, random);

        for (int position = 0; position < numberOfGenes; position++) {
            int orderIndex = orderIndices.get(position).intValue();
            genes.get(orderIndex).set(position, Boolean.valueOf(true));
        }
    }

    @Override
    public void mutateMethod1(double probability) {
        if (random.nextDouble() > probability) {
            return;
        }
        int size = genes.size();
        if (size < 2) {
            return;
        }
        int pos1 = random.nextInt(size);
        int pos2;
        do {
            pos2 = random.nextInt(size);
        } while (pos2 == pos1);

        int order1 = -1, order2 = -1;
        for (int i = 0; i < size; i++) {
            if (genes.get(i).get(pos1)) {
                order1 = i;
            }
            if (genes.get(i).get(pos2)) {
                order2 = i;
            }
        }
        if (order1 != -1 && order2 != -1) {
            genes.get(order1).set(pos1, false);
            genes.get(order1).set(pos2, true);

            genes.get(order2).set(pos2, false);
            genes.get(order2).set(pos1, true);
        }
    }

    @Override
    public void mutateMethod2(double probability) {

    }

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

    public int getFitness() {
        return evaluator.evaluate(this);
    }

    @Override
    public Chromosome clone() {
        BinaryChromosome clone = new BinaryChromosome(this.genes);
        return clone;
    }

    @Override
    public int getTotalRouteTime() {
        return evaluator.calculateTotalRouteTime(this);
    }


    public int getNumberOfDeliveries() {
        return getDeliverySequence().size();
    }
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
}