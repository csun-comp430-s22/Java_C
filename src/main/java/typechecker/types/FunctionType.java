package typechecker.types;

public class FunctionType implements Type{

    public final Type paramType;
    public final Type returnType;

    public FunctionType(final Type paramType,
                        final Type returnType) {
        this.paramType = paramType;
        this.returnType = returnType;
    }

    public boolean equals(final Object other) {
        if (other instanceof FunctionType) {
            final FunctionType asFunc = (FunctionType)other;
            return (paramType.equals(asFunc.paramType) &&
                    returnType.equals(asFunc.returnType));
        } else {
            return false;
        }
    }

    public String toString() {
        return "Function(" + paramType.toString() + " => " + returnType.toString() + ")";
    }
}