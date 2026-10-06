package com.compi.lenyau2.ast;

public class Binary extends NodoAST {
    public final String op;
    public final NodoAST izq;
    public final NodoAST der;

    public Binary(String op, NodoAST i, NodoAST d) {
        this.op = op;
        this.izq = i;
        this.der = d;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitBinary(this);
    }
}
