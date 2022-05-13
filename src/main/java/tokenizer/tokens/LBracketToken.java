package tokenizer.tokens;

public class LBracketToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof LBracketToken;
    }
	public int hashCode() {
        return 12;
    }
    public String toString(){
        return "LBracketToken";
    }
}
