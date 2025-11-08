# Genetic Algorithm Library for Java

A comprehensive Java library implementing Genetic Algorithm (GA) for optimization problems, with a case study on Order Delivery Optimization.

## 📋 Project Overview

This library provides a complete implementation of Genetic Algorithms with support for multiple chromosome types, selection methods, crossover operators, mutation strategies, and replacement mechanisms. The implementation follows clean code principles and uses proper software architecture patterns.

## 🏗️ Architecture

The library follows a modular architecture with clear separation of concerns:

```
src/
├── GeneticAlgorithm.Chromosomes/           # Different chromosome representations
│   ├── Chromosome.java           (Interface)
│   ├── BinaryChromosome.java
│   ├── IntegerChromosome.java
│   └── FloatingPointChromosome.java
├── GeneticAlgorithm.Selection/             # GeneticAlgorithm.Selection strategies
│   ├── GeneticAlgorithm.Selection.java             (Interface)
│   ├── TournamentSelection.java
│   └── RouletteWheelSelection.java
├── GeneticAlgorithm.Crossover/             # GeneticAlgorithm.Crossover operators
│   ├── GeneticAlgorithm.Crossover.java             (Interface)
│   ├── OrderOneCrossover.java
│   ├── IntegerCrossover.java
│   └── FloatingPointUniformCrossover.java
├── GeneticAlgorithm.Replacement/           # GeneticAlgorithm.Replacement strategies
│   ├── ReplacementStrategy.java   (Interface)
│   ├── GenerationalReplacement.java
│   ├── SteadyStateReplacement.java
│   └── ElitistReplacement.java
├── GeneticAlgorithm.Fitness/               # GeneticAlgorithm.Fitness evaluation (problem-dependent)
│   ├── FitnessEvaluator.java      (Interface)
│   ├── BinaryFitnessEvaluator.java
│   ├── IntegerFitnessEvaluator.java
│   └── FloatingPointFitnessEvaluator.java
├── GeneticAlgorithm.Helpers/               # Utility classes
│   └── Pair.java
├── GeneticAlgorithm.GeneticAlgorithm.java  # GeneticAlgorithm.Main GA engine (configurable)
├── GeneticAlgorithm.Initializer.java       # Population initialization
├── GeneticAlgorithm.CaseStudyDemo.java     # Simple demonstration following template
└── GeneticAlgorithm.Main.java              # Interactive comprehensive demo
```

## ✨ Features

### 1. Multiple Chromosome Types
- **Binary Chromosome**: Matrix representation for permutation problems
- **Integer Chromosome**: Direct sequence representation
- **Floating Point Chromosome**: Continuous value representation

### 2. GeneticAlgorithm.Selection Methods
- **Tournament GeneticAlgorithm.Selection**: Configurable tournament size
- **Roulette Wheel GeneticAlgorithm.Selection**: GeneticAlgorithm.Fitness-proportionate selection

### 3. GeneticAlgorithm.Crossover Operators
- **Order-One GeneticAlgorithm.Crossover (OX1)**: For binary chromosomes
- **Integer GeneticAlgorithm.Crossover**: Preserves permutation validity
- **Floating Point Uniform GeneticAlgorithm.Crossover**: Random gene swapping

### 4. Mutation Methods
Each chromosome type implements 1-2 mutation methods:
- **Binary**: Swap mutation (position-based)
- **Integer**: Swap mutation, "become last" mutation
- **Floating Point**: Uniform mutation, non-uniform mutation

### 5. GeneticAlgorithm.Replacement Strategies
- **Generational GeneticAlgorithm.Replacement**: Complete population replacement
- **Steady-State GeneticAlgorithm.Replacement**: K parents replaced by K offspring
- **Elitist GeneticAlgorithm.Replacement**: Preserve best individuals across generations

### 6. Infeasibility Handling
The fitness evaluation considers time constraints and only counts deliveries that can be completed within the limit.

## 🚀 Quick Start

### Using the Library (Simple Approach)

```java
import GeneticAlgorithm.GeneticAlgorithm;

import java.util.ArrayList;

public class MyApplication {
    public static void main(String[] args) {
        // 1. Setup your problem
        ArrayList<ArrayList<Integer>> distanceMatrix = /* your data */;
        int timeConstraint = 200;

        // 2. Initialize fitness evaluator
        IntegerFitnessEvaluator.getInstance(distanceMatrix, timeConstraint);
        FitnessEvaluator fitnessFunction = IntegerFitnessEvaluator.getInstance();

        // 3. Configure GA
        GeneticAlgorithm ga = new GeneticAlgorithm();
        ga.setPopulationSize(50);
        ga.setChromosomeLength(8);
        ga.setFitnessFunction(fitnessFunction);
        ga.setChromosomeType(GeneticAlgorithm.ChromosomeType.INTEGER);
        ga.setCrossoverRate(0.7);
        ga.setMutationRate(0.02);
        ga.setGenerations(100);

        // 4. Run
        ga.run();

        // 5. Get results
        Chromosome bestSolution = ga.getBestSolution();
        System.out.println("Best Solution: " + bestSolution.getDeliverySequence());
        System.out.println("GeneticAlgorithm.Fitness: " + bestSolution.getFitness());
    }
}
```

## 📊 Case Study: Order Delivery Optimization

### Problem Description
A delivery company needs to optimize the sequence of delivering orders to multiple locations within a time constraint. The objective is to maximize the number of orders delivered.

### Constraints
- Time limit for all deliveries
- Must consider travel time between locations
- Start from depot (location 0)

### Running the Case Study

#### Option 1: Simple Demo (Follows Template)
```bash
javac src/GeneticAlgorithm.CaseStudyDemo.java
java -cp src GeneticAlgorithm.CaseStudyDemo
```

This runs a preset demonstration showing how to use the library with fixed parameters.

#### Option 2: Interactive Mode (Full Control)
```bash
javac src/GeneticAlgorithm.Main.java
java -cp src GeneticAlgorithm.Main
```

This provides an interactive interface where you can:
- Choose chromosome type
- Configure all GA parameters
- Use default values (just press Enter)
- See detailed step-by-step execution

**Interactive Mode Features:**
- Shows default values for all parameters
- Press Enter to use defaults, or type custom values
- Demonstrates all 8 phases of GA:
  1. Problem Setup
  2. Initialization
  3. GeneticAlgorithm.Fitness Evaluation
  4. GeneticAlgorithm.Selection Method
  5. GeneticAlgorithm.Crossover Operators
  6. Mutation Operators
  7. Evolutionary Loop
  8. Results Analysis

## 🔧 Configuration Options

### Default Values
```java
Population Size: 50
Chromosome Length: 10
Generations: 100
GeneticAlgorithm.Crossover Rate: 0.7
Mutation Rate: 0.01
GeneticAlgorithm.Selection: Tournament (size=3)
GeneticAlgorithm.Replacement: Elitist (elite count=1)
```

### Customizable Parameters
All parameters can be configured through setter methods:
- `setPopulationSize(int)`
- `setChromosomeLength(int)`
- `setGenerations(int)`
- `setCrossoverRate(double)`
- `setMutationRate(double)`
- `setSelectionMethod(GeneticAlgorithm.Selection)`
- `setCrossoverOperator(GeneticAlgorithm.Crossover)`
- `setReplacementStrategy(ReplacementStrategy)`

## 📝 Testing

The project includes comprehensive test files:
- `TestSelection.java` - Tests selection methods
- `TestCrossover.java` - Tests crossover operators
- `TestMutation.java` - Tests mutation methods
- `TestReplacement.java` - Tests replacement strategies

## 🎯 Clean Code Principles

This implementation follows clean code principles:

1. **Interface-based Design**: All major components use interfaces for flexibility
2. **Separation of Concerns**: Each class has a single responsibility
3. **Problem-Dependent Interfaces**: GeneticAlgorithm.Fitness evaluation uses interfaces for different problems
4. **No Over-commenting**: Comments only where necessary for complex logic
5. **Meaningful Names**: Classes, methods, and variables have descriptive names
6. **Modular Architecture**: Components can be easily replaced or extended

## 📈 Results Analysis

The library provides comprehensive statistics:
- Best fitness achieved
- Best solution (delivery sequence)
- Total route time
- GeneticAlgorithm.Fitness evolution over generations
- Average, maximum, minimum fitness
- Convergence analysis

## 🎓 For TA Presentation

### Key Points to Cover:

1. **Library Structure**: Modular design with interfaces
2. **GA Implementation**: Complete GA lifecycle with all required components
3. **Challenging Parts**:
   - Maintaining chromosome validity during crossover/mutation
   - Infeasibility handling in fitness evaluation
   - Generic design supporting multiple chromosome types
4. **Case Study**: Delivery optimization with time constraints

### Demo Flow:
1. Show GeneticAlgorithm.CaseStudyDemo.java (simple library usage)
2. Run interactive GeneticAlgorithm.Main.java (comprehensive demonstration)
3. Explain key design decisions
4. Show test results

## 📄 Requirements Compliance

✅ 3 Chromosome types (Binary, Integer, Floating Point)  
✅ 2+ GeneticAlgorithm.Selection methods (Tournament, Roulette Wheel)  
✅ 3+ GeneticAlgorithm.Crossover methods (OrderOne, Integer, FP Uniform)  
✅ 1-2 Mutation methods per chromosome type  
✅ 3 GeneticAlgorithm.Replacement strategies (Generational, Steady-State, Elitist)  
✅ Infeasibility handling (time constraint checking)  
✅ Default values with override capability  
✅ Clean code architecture  
✅ Interface-based fitness evaluation  
✅ Real-world case study  

## 👥 Team Members
[Add your team member names here]

## 📅 Academic Information
Course: Soft Computing  
Institution: FCAI  
Phase: 1 - Genetic Algorithm Implementation

---

**Note**: This is an academic project demonstrating GA implementation principles. The library is designed for educational purposes and can be extended for production use.

