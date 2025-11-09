package FuzzyLogic.Variable;

import FuzzyLogic.Membership.MembershipFunction;
import java.util.*;

public abstract class FuzzyVariable {
    protected String name;
    protected double value;
    protected Map<String, MembershipFunction> sets = new HashMap<>();

    public FuzzyVariable(String name) {
        this.name = name;
        defineMembershipFunctions();
    }

    // Let subclasses define their own fuzzy sets
    protected abstract void defineMembershipFunctions();

    public void setValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public double getMembership(String label) {
        return sets.get(label).getMembership(value);
    }

    public Map<String, Double> fuzzify() {
        Map<String, Double> memberships = new HashMap<>();
        for (var e : sets.entrySet()) {
            memberships.put(e.getKey(), e.getValue().getMembership(value));
        }
        return memberships;
    }

    public String getName() {
        return name;
    }

    public Map<String, MembershipFunction> getSets() {
        return sets;
    }
}
