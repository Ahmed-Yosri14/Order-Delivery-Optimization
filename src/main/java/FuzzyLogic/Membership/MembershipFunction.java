package FuzzyLogic.Membership;

public interface MembershipFunction {
    double getMembership(double x);
    double getCentroid();
    double getMeanOfMaximum();
}
