import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class astCodeGen {
    public static void main(String[] args){
        String outPath=args[0];
        try {
            defineAst(outPath,"Expr", Arrays.asList(
                    "Binary : Expr left,Token Operator,Expr right",
                    "Grouping : Expr Expression",
                    "Unary : Token operator,Expr right",
                    "Literal : Object value",
                     "Variable : Token name"
            ));
            defineAst(outPath, "Stmt", Arrays.asList(
                    "Expression : Expr expression",
                    "Print : Expr expression",
                    "Var : Token name, Expr initializer"
            ));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static void defineAst(String outPath, String baseClass, List<String> types) throws IOException {
        String path=outPath+"/"+baseClass+".java";
        PrintWriter writer=new PrintWriter(path, StandardCharsets.UTF_8);
        writer.println("package com.manohar.mano;");
        writer.println("abstract class "+baseClass+"{");
        defineVisitor(writer,baseClass,types);
        for(String type:types){
            String className=type.split(":")[0].trim();
            String fields=type.split(":")[1].trim();
            defineTypes(writer,baseClass,className,fields);
        }
        writer.println();
        writer.println(" abstract <R> R accept(Visitor<R> visitor);");
        writer.println("}");
        writer.close();
    }
    public static void defineTypes(PrintWriter writer,String baseClass,String classname,String fields){
        writer.println("   class "+classname+" extends "+baseClass+"{");
        String[] properties=fields.split(",");
        for(String p:properties){
            writer.println("final "+p+";");
        }
        writer.println("   "+classname+"("+fields+") {");
        for(String p:properties){
            String name=p.split(" ")[1];
            writer.println("this."+name+"="+name+";");
        }
        writer.println("}");
        writer.println();
        writer.println(" @Override");
        writer.println(" <R> R accept(Visitor<R> visitor) {");
        writer.println(" return visitor.visit" +
                        classname + baseClass + "(this);");
        writer.println("}");
        writer.println("}");
    }
    private static void defineVisitor(
            PrintWriter writer, String baseName, List<String> types) {
        writer.println(" interface Visitor<R> {");
        for (String type : types) {
            String typeName = type.split(":")[0].trim();
            writer.println(" R visit" + typeName + baseName + "(" +
                    typeName + " " + baseName.toLowerCase() + ");");
        }
        writer.println(" }");
    }
}
