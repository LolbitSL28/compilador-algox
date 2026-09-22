package com.compi.lenyau2;

public class TokenList {
    private Token token;
    private TokenList next;

    public TokenList(Token token) {
        this.token = token;
        this.next = null;
    }

    public Token getToken() {
        return token;
    }

    public TokenList getNext() {
        return next;
    }

    public void setNext(TokenList next) {
        this.next = next;
    }
}
