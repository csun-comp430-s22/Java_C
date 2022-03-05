package tokenizer.tokens;

public class CloglnToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof CloglnToken;
    }
	public int hashCode() {
        return 36;
    }
    public String toString(){
        return "CloglnToken";
    }
}