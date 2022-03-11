package tokenizer.tokens;

public class StringLiteralToken implements Token{
    public final String string;

	public boolean equals(final Object other) {
        if (other instanceof StringLiteralToken) {
            final StringLiteralToken asStr = (StringLiteralToken)other;
            return string.equals(asStr.string);
        } else {
            return false;
        }
    }
	
	public int hashCode() {
        return string.hashCode();
    }

    public StringLiteralToken(final String string){
        this.string = string;
    }
    public String toString(){
        return "StringLiteralToken(" + string + ")";
    }
}