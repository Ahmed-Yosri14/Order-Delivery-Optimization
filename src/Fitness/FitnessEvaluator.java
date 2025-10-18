package Fitness;

import Chromosomes.Chromosome;

public interface FitnessEvaluator {
    Integer evaluate(Chromosome chromosome);
}
