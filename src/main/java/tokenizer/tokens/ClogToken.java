package tokenizer.tokens;

public class ClogToken	implements Token {
	public boolean equals(final Object other) {
        return other instanceof ClogToken;
    }
	public int hashCode() {
        return 37;
    }
    public String toString(){
        return "ClogToken";
    }
}