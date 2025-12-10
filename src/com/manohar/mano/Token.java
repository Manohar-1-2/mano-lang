package com.manohar.mano;

public class Token {
    final TokenType type;
    final String lexme;
    final Object literal;
    final int lineNo;
    public Token(TokenType type,String lex,Object literal,int lineNo){
        this.type=type;
        this.lexme=lex;
        this.literal=literal;
        this.lineNo=lineNo;
    }

    @Override
    public String toString() {
        return type +" "+lexme;
    }
}
