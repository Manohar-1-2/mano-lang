package com.manohar.mano;

public abstract class Stmt {
    interface Visitor<R> {
        R visitExpressionStmt(Expression stmt);

        R visitPrintStmt(Print stmt);

        R visitVarStmt(Var stmt);
    }

    public static class Expression extends Stmt {
        final Expr expression;

        Expression(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitExpressionStmt(this);
        }
    }

    public static class Print extends Stmt {
        final Expr expression;

        Print(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitPrintStmt(this);
        }
    }
    public static class Var extends Stmt {
        final Token tokenName;
        final Expr initializer;

        Var(Token tokenName,Expr initializer) {
            this.tokenName = tokenName;
            this.initializer = initializer;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVarStmt(this);
        }
    }

    abstract <R> R accept(Visitor<R> visitor);
}
