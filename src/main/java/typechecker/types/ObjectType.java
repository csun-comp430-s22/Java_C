package typechecker.types;

public class ObjectType implements Type {
    public final String name;

    public ObjectType(final String name) {
        this.name = name;
    }
	
	public int hashCode() { return name.hashCode(); }

    public boolean equals(Object other) {
        if (other instanceof ObjectType) {
            ObjectType asType = (ObjectType) other;
            return name.equals(asType.name); 
        } else
            return false;
    }

    public String toString() {
        return "ObjectType(" + name + ")";
    }

}