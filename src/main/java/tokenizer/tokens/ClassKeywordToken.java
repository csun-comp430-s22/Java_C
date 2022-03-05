package tokenizer.tokens;

public class ClassKeywordToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof ClassKeywordToken;
    }
	public int hashCode() {
        return 35;
    }
    public String toString(){
        return "ClassKeywordToken";
    }
}