package tokenizer.tokens;

public class GreaterThanToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof GreaterThanToken;
    }
	public int hashCode() {
        return 8;
    }
    public String toString(){
        return "GreaterThanToken";
    }
}
