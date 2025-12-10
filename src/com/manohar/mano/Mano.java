package com.manohar.mano;

import java.io.IOException;
import java.util.List;
import  java.nio.file.Files;
import  java.nio.charset.Charset;
import java.nio.file.Paths;

public class Mano {
    static boolean hadError;
    static boolean hadRuntimeError;
    public static Interpreter interpreter=new Interpreter();
    public static void main(String[] args) {
        if(args.length<1){
            System.out.println("Usage :mano [filename]");
            System.exit(0);
        } else if (args.length==1) {
            try {
                runFile(args[0]);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    public static void runFile(String filePath) throws IOException{
        byte[] bytes=Files.readAllBytes(Paths.get(filePath));
        run(new String(bytes,Charset.defaultCharset()));
    }
    public static void run(String source){

        Scanner scanner=new Scanner(source);
        List<Token> tokens = scanner.scanTokens();
        Parser parser = new Parser(tokens);
        List<Stmt> expressions= parser.parse();
        // Stop if there was a syntax error.
        if (hadError)  System.exit(65);
        if (hadRuntimeError)  System.exit(75);
//        System.out.println(new AstPrinter().print(expression));
        interpreter.interpret(expressions);
    }
    static void error(Token token, String message) {
        hadError=true;
        if (token.type == TokenType.EOF) {
            report(token.lineNo, " at end", message);
        } else {
            report(token.lineNo, " at '" + token.lexme + "'", message);
        }
    }
    static void runtimeError(RuntimeError error) {
        System.err.println(error.getMessage() +
                "\n[line " + error.token.lineNo + "]");
        hadRuntimeError = true;
    }
    private static void report(int line, String where,
                               String message) {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
    }
}
