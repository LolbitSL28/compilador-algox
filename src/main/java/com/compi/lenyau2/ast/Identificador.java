package com.compi.lenyau2.ast;

public class Identificador extends NodoAST {
    public final String nombre;

    public Identificador(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitIdentificador(this);
    }
}
