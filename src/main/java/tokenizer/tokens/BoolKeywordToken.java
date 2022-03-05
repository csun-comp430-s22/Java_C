package tokenizer.tokens;

public class BoolKeywordToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof BoolKeywordToken;
    }
	public int hashCode() {
        return 33;
    }
    public String toString(){
        return "BoolKeywordToken";
    }
}