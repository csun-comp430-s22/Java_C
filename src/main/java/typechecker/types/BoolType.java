package typechecker.types;

public class BoolType implements Type {
    public int hashCode() { return 1; }
    public boolean equals(final Object other) {
        return other instanceof BoolType;
    }
    public String toString() { return "BoolType"; }
}