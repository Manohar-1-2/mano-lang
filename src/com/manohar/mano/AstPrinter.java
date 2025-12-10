package com.manohar.mano;

public class AstPrinter implements Expr.Visitor<String>{
    public String print(Expr expr){
        return expr.accept(this);
    }
    @Override
    public String visitBinaryExpr(Expr.Binary expr) {
        return parenthesize(expr.Operator.lexme,expr.left,expr.right);
    }

    @Override
    public String visitGroupingExpr(Expr.Grouping expr) {
        return parenthesize("group",expr.expression);
    }

    @Override
    public String visitUnaryExpr(Expr.Unary expr) {
        return parenthesize(expr.operator.lexme,expr.right);
    }

    @Override
    public String visitLiteralExpr(Expr.Literal expr) {
        if(expr.value==null) return "nill";
        return expr.value.toString();
    }

    @Override
    public String visitVariable(Expr.Variable expr) {
        return "";
    }

    private String parenthesize(String name,Expr ...exprs){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("(").append(name);
        for(Expr e:exprs){
            stringBuilder.append(" ").append(e.accept(this));
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
