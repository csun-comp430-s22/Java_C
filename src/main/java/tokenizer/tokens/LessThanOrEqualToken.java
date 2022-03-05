package tokenizer.tokens;

public class LessThanOrEqualToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof LessThanOrEqualToken;
    }
	public int hashCode() {
        return 16;
    }
    public String toString(){
        return "LessThanOrEqualToken";
    }
}
