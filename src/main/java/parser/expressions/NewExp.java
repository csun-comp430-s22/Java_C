package parser.expressions;

import java.util.List;

public class NewExp implements Expression {

    public final String classname;
    public final List<Expression> parameters;

    public NewExp(final String classname, final List<Expression> parameters) {
        this.classname = classname;
        this.parameters = parameters;
    }
    
    public String toString() {
        return "NewExp(" + classname + ", " + parametersString() +  ")";
    }

    private String parametersString(){
        String ret = "";
        if(parameters.size() >= 1){
            ret += parameters.get(0).toString();
        }
        for(int i = 1; i < parameters.size(); i++){
            ret += ", " + parameters.get(i).toString();
        }
        return ret;
    }

    public boolean equals(Expression exp) {
        return (this.toString()).equals(exp.toString());
    }
}