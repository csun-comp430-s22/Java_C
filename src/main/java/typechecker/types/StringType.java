package typechecker.types;

public class StringType implements Type{

    public boolean equals(final Object other) {
        return other instanceof StringType;
    }


    @Override
    public String toString() {
        return "String";
    }

}