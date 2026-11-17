package com.compi.lenyau2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.compi.lenyau2.ast.Programa;
import com.compi.lenyau2.semantico.Resolver;

import java.nio.file.Path;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        String file = "ejemplo.algox";
        Path path = Paths.get(file);

        try {
            // Lexer
            String contenido = Files.readString(path);

            Lexer lexer = new Lexer(contenido);
            TokenList listaTokens = lexer.analizeToken();
            System.out.println("--ANALSISIS LEXICO--");
            System.out.println("Tokens generados:");
            TokenList current = listaTokens;
            while (current != null) {
                Token token = current.getToken();
                System.out.println(
                        "Token: " + token.getTypeTokenNumber() + ", Tipo: " + token.getType() + ", Valor: "
                                + token.getValue() + ", Línea: " + token.getLine());
                current = current.getNext();
            }
            // Parser
            System.out.println("\n--ANALSISIS SINTACTICO--");
            Parser parser = new Parser(listaTokens);
            Programa prog = parser.parse();

            if (!parser.getErrores().isEmpty()) {
                System.out.println("Se encontraron " + parser.getErrores().size() + " error(es) sintáctico(s):");
                for (String e : parser.getErrores()) {
                    System.out.println("  " + e);
                }
                return;
            }

            System.out.println("\nAnálisis sintáctico exitoso. Sentencias principales: " + prog.sentencias.size());

            // Resolver (Analisis semantico)
            System.out.println("\n--ANALSISIS SEMANTICO--");
            Resolver resolver = new Resolver();
            prog.accept(resolver);
            List<String> erroresSemanticos = resolver.getErrores();

            if (!erroresSemanticos.isEmpty()) {
                System.out.println("Se encontraron " + erroresSemanticos.size() + " error(es) semántico(s):");
                for (String e : erroresSemanticos) {
                    System.out.println("  " + e);
                }

            } else {
                System.out.println("Análisis semántico exitoso. El programa es válido.");
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

    }
}