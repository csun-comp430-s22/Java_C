package tokenizer.tokens;

public class StringKeywordToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof StringKeywordToken;
    }
	public int hashCode() {
        return 28;
    }
    public String toString(){
        return "StringKeywordToken";
    }
}