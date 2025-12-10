package com.manohar.mano;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.manohar.mano.TokenType.*;

public class Scanner {
    private String source;
    private List<Token> tokens=new ArrayList<>();
    private int start=0;
    private int current=0;
    private int line=1;
    private static final Map<String,TokenType>keywords=new HashMap<>();
    static {
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }
    public Scanner(String source){
        this.source=source;
    }
    List<Token> scanTokens(){
        while (!isEnd()){
            start=current;
            scanToken();
        }
        tokens.add(new Token(EOF,"","",line));

        return tokens;
    }
    boolean isEnd(){
        return current>=source.length();
    }
    private void scanToken(){
        char c=advance();
        switch (c){
            case '{':addToken(LEFT_PAREN);break;
            case '}':addToken(RIGHT_PAREN);break;
            case '(':addToken(LEFT_BRACE);break;
            case ')':addToken(RIGHT_BRACE);break;
            case ',':addToken(COMMA);break;
            case '.':addToken(DOT);break;
            case '-':addToken(MINUS);break;
            case '+':addToken(PLUS);break;
            case ';':addToken(SEMICOLON);break;
            case '/':
                if(match('/')){
                    while (peek()!='\n' && !isEnd()) advance();
                }else {
                    addToken(SLASH);
                }
                break;
            case '*':addToken(STAR);break;
            case '!':
                addToken(match('=')?BANG_EQUAL:BANG);
                break;
            case '=':
                addToken(match('=')?EQUAL_EQUAL:EQUAL);
                break;
            case '>':
                addToken(match('=')?GREATER_EQUAL:EQUAL);
                break;
            case '<':
                addToken(match('=')?LESS_EQUAL:EQUAL);
                break;
            case '"':string();break;
            case '\n':
                line++;
                break;
            case ' ','\r','\t':
                break;
            default:
                if(isDigit(c)){
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    System.out.println("Unkown token");
                }
        }
    }
    private char peek(){
        if (isEnd()) return '\0';
        return source.charAt(current);
    }
    private char nextPeek(){
        if(current+1>source.length()) return '\0';
        return source.charAt(current+1);
    }
    private char advance(){
        current++;
        return source.charAt(current-1);
    }
    private void addToken(TokenType type){
        addToken(type,null);
    }
    private void addToken(TokenType type,Object literal){
        String text=source.substring(start,current);
        tokens.add(new Token(type,text,literal,line));
    }
    private boolean match(char expeceted){
        if(isEnd()) return false;
        if(source.charAt(current)!=expeceted) return false;
        current++;
        return true;
    }
    private void string(){
        while(peek()!='"' && !isEnd()){
            if(match('\n'))line++;
            advance();
        }
        if(isEnd()){
            Scanner.error(line,"unclosed string quotes");
            return;
        }
        advance();
        String s=source.substring(start+1,current-1);
        addToken(STRING,s);
    }
    private void number(){
        while(isDigit(peek()) && !isEnd()){
            advance();
        }
        if(peek()=='.' && isDigit(nextPeek())){
            advance();
            while (isDigit(peek()) && !isEnd()) advance();
        }
        addToken(NUMBER,Double.parseDouble(source.substring(start,current)));
    }
    private void identifier(){
        while (isAlphaNumeric(peek())) advance();
        String identifer=source.substring(start,current);
        TokenType type=keywords.get(identifer);
        if(type==null) type=IDENTIFIER;
        addToken(type,identifer);
    }
    private boolean isDigit(char digit){
        return '0'<=digit && digit <= '9';
    }
    private boolean isAlpha(char alpha){
        return ('a'<=alpha && alpha<='z')||('A'<=alpha && alpha <='Z') || '_'==alpha;
    }
    private boolean isAlphaNumeric(char c){
        return isAlpha(c)||isDigit(c);
    }
    public void printTokens(){
        for(Token t:tokens){
            System.out.println(t.toString());
        }
    }
    static void error(int line, String message) {
        report(line, "", message);
    }
    private static void report(int line, String where,
                               String message) {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
    }
}
