package parser.statements;

import parser.expressions.Expression;

public class CloglnStmt implements Statement {
    public final Expression output;

    public CloglnStmt(Expression output) {
        this.output = output;
    }

    public String toString() {
        return "CloglnStmt(" + output + ")";
    }

    public boolean equals(Statement stmt) {
        return toString().equals(stmt.toString());
    }
}