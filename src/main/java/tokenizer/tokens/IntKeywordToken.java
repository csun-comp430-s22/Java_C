package tokenizer.tokens;

public class IntKeywordToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof IntKeywordToken;
    }
	public int hashCode() {
        return 11;
    }
    public String toString(){
        return "IntKeywordToken";
    }
}
