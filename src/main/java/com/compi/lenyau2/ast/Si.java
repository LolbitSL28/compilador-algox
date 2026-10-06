package com.compi.lenyau2.ast;

public class Si extends NodoAST {
    public final NodoAST condicion;
    public final Bloque entonces;
    public final Bloque sino;

    public Si(NodoAST condicion, Bloque entonces, Bloque sino) {
        this.condicion = condicion;
        this.entonces = entonces;
        this.sino = sino;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitSi(this);
    }
}
