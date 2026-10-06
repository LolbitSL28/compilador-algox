package com.compi.lenyau2.ast;

public class Mientras extends NodoAST {
    public final NodoAST condicion;
    public final Bloque cuerpo;

    public Mientras(NodoAST condicion, Bloque cuerpo) {
        this.condicion = condicion;
        this.cuerpo = cuerpo;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitMientras(this);
    }
}
