package parser.statements;

import parser.expressions.Expression;

public class ReturnStmt implements Statement {
    public final Expression value;

    public ReturnStmt(final Expression value) {
        this.value = value;
    }

    public String toString() {
        return "ReturnStmt(" + value + ")";
    }

    public boolean equals(Statement stmt) {
        return (this.toString()).equals(stmt.toString());
    }
}