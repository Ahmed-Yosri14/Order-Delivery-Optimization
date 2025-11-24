package GeneticAlgorithm.Helpers;

public class Pair implements Comparable<Pair>{

    double number;
    int idx;

    public int getIdx() {
        return idx;
    }

    public Pair(double number, int idx) {
        this.number = number;
        this.idx = idx;
    }
    @Override
    public int compareTo(Pair other) {
        if (this.number != other.number) {
            return Double.compare(this.number, other.number); // Sort by number first
        }
        return Integer.compare(this.idx, other.idx); // Then by index
    }

    @Override
    public String toString() {
        return "(" + number + ", " + idx + ")";
    }
}
