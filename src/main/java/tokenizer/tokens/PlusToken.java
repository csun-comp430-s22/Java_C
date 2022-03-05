package tokenizer.tokens;

public class PlusToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof PlusToken;
    }
	public int hashCode() {
        return 22;
    }
    public String toString(){
        return "PlusToken";
    }
}
