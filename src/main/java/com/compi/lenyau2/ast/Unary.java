package com.compi.lenyau2.ast;

public class Unary extends NodoAST {
    public final String op;
    public final NodoAST expr;

    public Unary(String op, NodoAST e) {
        this.op = op;
        this.expr = e;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitUnary(this);
    }
}
