package tokenizer.tokens;

public class StringLiteralToken implements Token{
    public final String string;
	
	public boolean equals(final Object other) {
        return other instanceof StringLiteralToken;
    }
	public int hashCode() {
        return 29;
    }

    public StringLiteralToken(final String string){
        this.string = string;
    }
    public String toString(){
        return "StringLiteralToken(" + string + ")";
    }
}