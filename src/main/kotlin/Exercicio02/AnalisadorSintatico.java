package Exercicio02;

import java.util.Stack;

//Maria Luíza do Carmo de Santana- 01357229
public class AnalisadorSintatico {
    public static boolean analisar(String expressao) {
        Stack<String> pilha = new Stack<>();
        pilha.push("EOF");
        pilha.push("Expressão");

        int pos = 0;

        while (!pilha.isEmpty()) {
            String topo = pilha.pop();

            if (topo.equals("EOF")) {
                if (pos == expressao.length()) {
                    return true;
                } else {
                    return false;
                }
            } else if (pos >= expressao.length()) {
                return false;
            } else if (isTerminal(topo)) {
                if (topo.equals(expressao.substring(pos, pos + topo.length()))) {
                    pos += topo.length();
                } else {
                    return false;
                }
            } else {
                if (topo.equals("Expressão")) {
                    pilha.push("Termo");
                    pilha.push("'+'");
                    pilha.push("Expressão");
                } else if (topo.equals("Termo")) {
                    pilha.push("Número");
                } else if (topo.equals("Número")) {
                    pilha.push("0");
                } else {
                    return false;
                }
            }
        }

        return false;
    }

    public static boolean isTerminal(String simbolo) {
        return simbolo.matches("[a-z]");
    }

    public static void main(String[] args) {
        String expressao = "0+0";

        if (analisar(expressao)) {
            System.out.println("Análise sintática concluída com sucesso!");
        } else {
            System.out.println("Erro na análise sintática!");
        }
    }
}
