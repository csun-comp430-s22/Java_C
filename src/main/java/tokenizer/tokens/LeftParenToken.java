package tokenizer.tokens;

public class LeftParenToken implements Token {
		public boolean equals(final Object other) {
        return other instanceof LeftParenToken;
    }
	public int hashCode() {
        return 14;
    }
    public String toString(){
        return "LeftParenToken";
    }
}