package com.compi.lenyau2.ast;

public class Repetir extends NodoAST {
    public final Bloque cuerpo;
    public final NodoAST condicion;

    public Repetir(Bloque cuerpo, NodoAST condicion) {
        this.cuerpo = cuerpo;
        this.condicion = condicion;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitRepetir(this);
    }
}
