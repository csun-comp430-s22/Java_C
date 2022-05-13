package parser.expressions;

public class ParenthesizedExp implements Expression {
    public final Expression body;

    public ParenthesizedExp(final Expression body) {
        this.body = body;
    }

    public String toString() {
        return "ParenthesizedExp(" + body + ")";  
    }

    public boolean equals(final Expression exp) {
        return (this.toString()).equals(exp.toString());
    }
}