package tokenizer.tokens;

public class IdentifierToken implements Token {
    public final String name;

    public IdentifierToken(final String name) {
        this.name = name;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        return "Identifier(" + name + ")";
    }

    public boolean equals(final Object other) {
        if (other instanceof IdentifierToken) {
            final IdentifierToken asVar = (IdentifierToken)other;
            return name.equals(asVar.name);
        } else {
            return false;
        }
    }
}