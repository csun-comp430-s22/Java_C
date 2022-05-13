
package parser.expressions;

import java.util.List;

public class MethodCallExp implements Expression{
    public final String objectName;
    public final String name;
    public final List<Expression> parameters;

    public MethodCallExp(final String objectName, final String name, final List<Expression> parameters) {
        this.objectName = objectName;
        this.name = name;
        this.parameters = parameters;
    }

    public String toString() {
        return "MethodCallExp(" + objectName + ", " + name + ", " + parametersString() + ")";     
    }

    public String parametersString(){
        String ret = "";
        if(parameters.size() == 1){
            ret += parameters.get(0).toString();
        }
        for(int i = 1; i < parameters.size(); i++){
            ret += ", " + parameters.get(i).toString();
        }
        return ret;
    }

    public boolean equals(final Expression exp) {
        return (this.toString()).equals(exp.toString());
    }
}