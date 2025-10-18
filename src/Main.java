import Chromosomes.Chromosome;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Choose the Chromosome type");
        System.out.println("1-Binary \n 2-Integer\n 3-Floating Point");
        int choice,noOfOrders,popSize;
        choice = sc.nextInt();
        System.out.println("Please enter the number of orders");
        noOfOrders = sc.nextInt();
        System.out.println("Please enter the population size");
        popSize = sc.nextInt();
        Initializer initializer = new Initializer();
        List<Chromosome> population = initializer.init(choice,noOfOrders,popSize);
        ArrayList<ArrayList<Integer>> distMatrix = getDistanceBetweenAllPoints(noOfOrders);
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
}
