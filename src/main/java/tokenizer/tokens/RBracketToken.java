package tokenizer.tokens;

public class RBracketToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof RBracketToken;
    }
	public int hashCode() {
        return 23;
    }
    public String toString(){
        return "LBracketToken";
    }
}