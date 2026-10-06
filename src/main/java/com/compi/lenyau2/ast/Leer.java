package com.compi.lenyau2.ast;

public class Leer extends NodoAST {
    public final String id;

    public Leer(String id) {
        this.id = id;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitLeer(this);
    }
}
