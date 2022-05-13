package typechecker.types;

public class VoidType implements Type {
    public int hashCode() { return 2; }
    public boolean equals(final Object other) {
        return other instanceof VoidType;
    }
    public String toString() { return "Void"; }
}