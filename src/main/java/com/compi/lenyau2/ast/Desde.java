package com.compi.lenyau2.ast;

public class Desde extends NodoAST {
    public final String id;
    public final NodoAST inicio;
    public final NodoAST fin;
    public final NodoAST paso;
    public final Bloque cuerpo;

    public Desde(String id, NodoAST inicio, NodoAST fin, NodoAST paso, Bloque cuerpo) {
        this.id = id;
        this.inicio = inicio;
        this.fin = fin;
        this.paso = paso;
        this.cuerpo = cuerpo;
    }

    @Override
    public <Resultado> Resultado accept(Visitor<Resultado> visitor) {
        return visitor.visitDesde(this);
    }
}
