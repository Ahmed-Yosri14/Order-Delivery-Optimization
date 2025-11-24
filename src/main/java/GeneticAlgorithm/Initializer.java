package GeneticAlgorithm;

import GeneticAlgorithm.Chromosomes.BinaryChromosome;
import GeneticAlgorithm.Chromosomes.Chromosome;
import GeneticAlgorithm.Chromosomes.FloatingPointChromosome;
import GeneticAlgorithm.Chromosomes.IntegerChromosome;

import java.util.ArrayList;
import java.util.List;

public class Initializer {

    List<Chromosome> init(int choice, int noOfOrders, int popSize){
        if (choice==1){
            return this.initBinary(noOfOrders,popSize);
        }
        else if (choice==2){
            return this.initInteger(noOfOrders,popSize);
        }
        else if (choice==3){
            return this.initFloatingPoint(noOfOrders,popSize);
        }
        else {
            throw new IllegalArgumentException("Invalid choice");
        }
    }

    List<Chromosome> initBinary(int noOfOrders,int popSize){
        List<Chromosome> ret = new ArrayList<Chromosome>();
        while (popSize>0){
            BinaryChromosome chromosome = new BinaryChromosome();
            chromosome.generateGenes(noOfOrders);
            ret.add(chromosome);
            popSize--;
        }
        return ret;
    }


    List<Chromosome> initInteger(int noOfOrders,int popSize){
        List<Chromosome> ret = new ArrayList<Chromosome>();
        while (popSize>0){
            IntegerChromosome chromosome = new IntegerChromosome();
            chromosome.generateGenes(noOfOrders);
            ret.add(chromosome);
            popSize--;
        }
        return ret;
    }

    List<Chromosome> initFloatingPoint(int noOfOrders,int popSize){
        List<Chromosome> ret = new ArrayList<Chromosome>();
        while (popSize>0){
            FloatingPointChromosome chromosome = new FloatingPointChromosome();
            chromosome.generateGenes(noOfOrders);
            ret.add(chromosome);
            popSize--;
        }
        return ret;
    }
}
