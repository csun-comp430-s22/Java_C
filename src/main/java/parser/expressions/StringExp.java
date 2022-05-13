package parser.expressions;
public class StringExp implements Expression{
    public final String value;

    public StringExp(final String value) {
        this.value = value;
    }

    public String toString() {
        return "StringExp(" + value + ")";
    }

    public boolean equals(final Expression exp) {
        return (this.toString()).equals(exp.toString());
    }
}