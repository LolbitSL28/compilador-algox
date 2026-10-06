package com.compi.lenyau2.ast;

public class Salir extends NodoAST {
    public Salir() {
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitSalir(this);
    }
}
