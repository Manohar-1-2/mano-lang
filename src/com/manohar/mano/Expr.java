package com.manohar.mano;

public abstract class Expr {
    interface Visitor<R> {
        R visitBinaryExpr(Binary expr);

        R visitGroupingExpr(Grouping expr);

        R visitUnaryExpr(Unary expr);

        R visitLiteralExpr(Literal expr);

        R visitVariable(Variable expr);
    }

    public static class Binary extends Expr {
        final Expr left;
        final Token Operator;
        final Expr right;

        Binary(Expr left, Token Operator, Expr right) {
            this.left = left;
            this.Operator = Operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    public static class Grouping extends Expr {
        final Expr expression;

        Grouping(Expr expression) {
            this.expression = expression;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitGroupingExpr(this);
        }
    }

    public static class Unary extends Expr {
        final Token operator;
        final Expr right;

        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnaryExpr(this);
        }
    }

    public static class Literal extends Expr {
        final Object value;

        Literal(Object value) {
            this.value = value;
        }

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteralExpr(this);
        }
    }
    public static class Variable extends Expr{
        final Token tokenName;
        Variable(Token tokenName){
            this.tokenName=tokenName;
        }
        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitVariable(this);
        }
    }

    abstract <R> R accept(Visitor<R> visitor);
}
