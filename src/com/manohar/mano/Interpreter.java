package com.manohar.mano;

import java.util.List;

public class Interpreter implements Expr.Visitor<Object>,Stmt.Visitor<Object>{
    private Environment env=new Environment();
    public void interpret(List<Stmt> statements){
        try {
            for(Stmt statement:statements){
                evaluate(statement);
            }

        } catch (RuntimeError error) {
            Mano.runtimeError(error);
        }

    }
    @Override
    public Object visitVarStmt(Stmt.Var stmt) {
        Object value=null;
        if(stmt.initializer!=null){
            value=evaluate(stmt.initializer);
            env.define(stmt.tokenName.lexme,value);
            return evaluate(stmt.initializer);
        }
        env.define(stmt.tokenName.lexme,value);
        return null;
    }
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object left=evaluate(expr.left);
        Object right=evaluate(expr.right);
        switch (expr.Operator.type){
            case GREATER:
                checkNumberOperands(expr.Operator,left,right);
                return (double)left > (double)right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.Operator,left,right);
                return (double)left >= (double)right;
            case LESS:
                checkNumberOperands(expr.Operator,left,right);
                return (double)left < (double)right;
            case LESS_EQUAL:
                checkNumberOperands(expr.Operator,left,right);
                return (double)left <= (double)right;
            case MINUS:
                checkNumberOperands(expr.Operator,left,right);
                return (double) left - (double) right;
            case STAR:
                checkNumberOperands(expr.Operator,left,right);
                return (double) left * (double) right;
            case SLASH:
                checkNumberOperands(expr.Operator,left,right);
                return (double) left / (double) right;
            case PLUS:

                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }
                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }
                throw new RuntimeError(expr.Operator,"both operands must be either string or number type");

            case BANG_EQUAL: return !isEqual(left, right);
            case EQUAL_EQUAL: return isEqual(left, right);
        }
        return null;
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);
        switch (expr.operator.type) {
            case BANG:
                return !isTruthy(right);
            case MINUS:
                checkNumberOperand(expr.operator,right);
                return -(double)right;
        }
        return null;
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitVariable(Expr.Variable variable) {
        return env.get(variable.tokenName);
    }


    private boolean isTruthy(Object obj){
        if(obj==null) return false;
        if(obj instanceof Boolean){
            return (boolean) obj;
        }
        return true;
    }
    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null) return true;
        if (a == null) return false;
        return a.equals(b);
    }
    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }
    private Object evaluate(Stmt stmt) {
        return stmt.accept(this);
    }
    private void checkNumberOperand(Token operator,Object obj){
        if(obj instanceof Double) return;
        throw new RuntimeError(operator,"Operand must me number");
    }
    private void checkNumberOperands(Token operator,
                                     Object left, Object right) {
        if (left instanceof Double && right instanceof Double) return;

        throw new RuntimeError(operator, "Operands must be numbers.");
    }

    @Override
    public Object visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }

    @Override
    public Object visitPrintStmt(Stmt.Print stmt) {
        Object value=evaluate(stmt.expression);
        if(value==null){
            System.out.println("NIL");
            return  null;
        }
        System.out.println(value.toString());
        return null;
    }


}
