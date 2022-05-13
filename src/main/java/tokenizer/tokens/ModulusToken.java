package tokenizer.tokens;

public class ModulusToken implements Token {
	public boolean equals(final Object other) {
        return other instanceof ModulusToken;
    }
	public int hashCode() {
        return 19;
    }
    public String toString(){
        return "ModulusToken";
    }
}
