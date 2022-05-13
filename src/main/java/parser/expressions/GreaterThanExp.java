package parser.expressions;

public class GreaterThanExp implements Expression {
    public final Expression exp1;
    public final Expression exp2;

    public GreaterThanExp(final Expression exp1, final Expression exp2) {
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    public String toString() {
        return "GreaterThanExp(" + exp1 + ", " + exp2 + ")";
    }

    public boolean equals(final Expression exp) {
        return (this.toString()).equals(exp.toString());
    }
}