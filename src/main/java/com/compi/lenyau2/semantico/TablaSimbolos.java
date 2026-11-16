package com.compi.lenyau2.semantico;

import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    private final Map<String, TipoDato> tabla = new HashMap<>();

    public boolean existe(String nombre) {
        return tabla.containsKey(nombre);
    }

    public void declarar(String nombre, TipoDato tipo) {
        tabla.put(nombre, tipo);
    }

    public TipoDato obtener(String nombre) {
        return tabla.getOrDefault(nombre, TipoDato.ERROR);
    }
}