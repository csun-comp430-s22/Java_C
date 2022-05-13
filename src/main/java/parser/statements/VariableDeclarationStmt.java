package parser.statements;
import parser.expressions.Expression;

public class VariableDeclarationStmt implements Statement {
    public final String type;
    public final String name;
    public final Expression value;

    public VariableDeclarationStmt(final String type, final String name) {
        this.type = type;
        this.name = name;
        this.value = null;
    }

    public VariableDeclarationStmt(final String type, final String name, final Expression value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public String toString() {
        if(value != null)
            return "VariableDeclarationStmt(" + type + ", " + name + ", " + value + ")";
        return "VariableDeclarationStmt(" + type + ", " + name + ")";
    }
    
    public boolean equals(Statement stmt) {
        return (this.toString()).equals(stmt.toString());
    }
}