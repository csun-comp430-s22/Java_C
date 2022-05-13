package tokenizer.tokens;

public class ConstructorKeywordToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof ConstructorKeywordToken;
    }
	public int hashCode() {
        return 0;
    }
    public String toString(){
        return "ConstructorKeywordToken";
    }
}