package parser.statements;

import parser.expressions.Expression;

public class WhileStmt implements Statement {

    public final Expression condition;
    public final BlockStmt body;

    public WhileStmt(Expression condition, BlockStmt body) {
        this.condition = condition;
        this.body = body;
    }

    public String toString() {
        return "WhileStmt(" + condition + ", " + body + ")";
    }

    public boolean equals(Statement stmt) {
        return (this.toString()).equals(stmt.toString());
    }
}