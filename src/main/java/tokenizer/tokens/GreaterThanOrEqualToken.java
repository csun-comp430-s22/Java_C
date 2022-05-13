package tokenizer.tokens;

public class GreaterThanOrEqualToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof GreaterThanOrEqualToken;
    }
	public int hashCode() {
        return 7;
    }
    public String toString(){
        return "GreaterThanOrEqualToken";
    }
}
