package parser.statements;

import parser.expressions.Expression;

public class VariableAssignmentStmt implements Statement {
    public final String name;
    public final Expression value;

    public VariableAssignmentStmt(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public String toString() {
        return "VariableAssignmentStmt(" + name + ", " + value + ")";
    }

    public boolean equals(Statement stmt) {
        return (this.toString()).equals(stmt.toString());
    }
}