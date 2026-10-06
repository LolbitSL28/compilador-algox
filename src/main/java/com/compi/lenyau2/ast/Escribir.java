package com.compi.lenyau2.ast;

public class Escribir extends NodoAST {
    public final NodoAST expresion;

    public Escribir(NodoAST expresion) {
        this.expresion = expresion;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitEscribir(this);
    }
}
