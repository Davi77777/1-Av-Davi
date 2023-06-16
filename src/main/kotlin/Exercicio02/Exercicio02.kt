package Exercicio02

//Maria Luíza do Carmo de Santana- 01357229
//Davi de Mendonça Vasconcelos Alves Coutinho - 01383910
//Arllom de Fraga Dutra - 01358091
class Exercicio02 {
    fun main(args: Array<String>) {

        val glc = Grammar(
            nonTerminals = setOf("S", "A"),
            terminals = setOf("a", "b"),
            startSymbol = "S",
            productions = mutableListOf(
                Production("S", listOf("a", "A")),
                Production("A", listOf("S")),
                Production("A", listOf("b"))
            )
        )
        val fng = transformToFNG(glc)

        // Imprime a Gramática na Forma Normal de Greibach (FNG)
        println("Gramática na Forma Normal de Greibach (FNG):")
        fng.productions.forEach { production ->
            println("${production.left} -> ${production.right.joinToString(" ")}")
        }
    }

    fun transformToFNG(glc: Grammar): Grammar {
        val fng = Grammar(glc.nonTerminals, glc.terminals, glc.startSymbol)

        eliminateEpsilonProductions(glc, fng)
        eliminateUnitProductions(fng)
        eliminateNonTerminalTransitions(fng)
        convertToGreibachNormalForm(fng)

        return fng
    }

    // Etapa 1: Eliminação das produções vazias
    private fun eliminateEpsilonProductions(glc: Grammar, fng: Grammar) {
        val nullableNonTerminals = mutableSetOf<String>()
        var oldNullableCount: Int
        var newNullableCount: Int

        // Passo 1: Encontra os não-terminais que são anuláveis
        do {
            oldNullableCount = nullableNonTerminals.size
            glc.productions.filter { it.right.contains(EPSILON) }
                .forEach { nullableNonTerminals.add(it.left) }

            glc.productions.filter { production ->
                production.right.all { nullableNonTerminals.contains(it) }
            }.forEach { nullableNonTerminals.add(it.left) }

            newNullableCount = nullableNonTerminals.size
        } while (oldNullableCount != newNullableCount)

        // Passo 2: Elimina as produções vazias
        glc.productions.filter { it.right.contains(EPSILON) }
            .forEach { production ->
                val nonNullableProductions = glc.productions.filterNot { it == production }
                val newProductions = mutableSetOf(listOf(production.left))
                nullableNonTerminals.forEach { nullableNonTerminal ->
                    nonNullableProductions.forEach { nonNullableProduction ->
                        val right = nonNullableProduction.right.map {
                            if (it == nullableNonTerminal) EPSILON else it
                        }
                        newProductions.add(listOf(production.left) + right)
                    }
                }
                newProductions.forEach { fng.productions.add(Production(it[0], it.subList(1, it.size))) }
            }
    }

    // Etapa 2: Eliminação das produções unitárias
    fun eliminateUnitProductions(g: Grammar) {
        val unitProductions = g.productions.filter { it.right.size == 1 && it.right[0] in g.nonTerminals }

        // Passo 3: Elimina as produções unitárias
        unitProductions.forEach { unitProduction ->
            val nonUnitProductions = g.productions.filter { it != unitProduction }
            val visited = mutableSetOf<String>()
            val queue = mutableListOf(unitProduction.right[0])

            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                if (current !in visited) {
                    visited.add(current)
                    nonUnitProductions.filter { it.left == current }
                        .forEach { nonUnitProduction ->
                            val right = nonUnitProduction.right
                            if (right.size == 1 && right[0] in g.nonTerminals) {
                                queue.add(right[0])
                            } else {
                                g.productions.add(Production(unitProduction.left, right))
                            }
                        }
                }
            }
        }
        g.productions.removeAll(unitProductions)
    }

    // Etapa 3: Eliminação das transições entre não-terminais
    fun eliminateNonTerminalTransitions(g: Grammar) {
        val nonTerminalTransitions = g.productions.filter { it.right.all { it in g.nonTerminals } }

        // Passo 4: Elimina as transições entre não-terminais
        nonTerminalTransitions.forEach { nonTerminalTransition ->
            val visited = mutableSetOf<String>()
            val queue = nonTerminalTransition.right.toMutableList()

            while (queue.isNotEmpty()) {
                val current = queue.removeFirst()
                if (current !in visited) {
                    visited.add(current)
                    g.productions.filter { it.left == current }
                        .forEach { nonTerminalProduction ->
                            queue.addAll(nonTerminalProduction.right)
                            g.productions.add(Production(nonTerminalTransition.left, nonTerminalProduction.right))
                        }
                }
            }
        }
        g.productions.removeAll(nonTerminalTransitions)
    }

    // Etapa 4: Conversão para a forma normal de Greibach
    fun convertToGreibachNormalForm(g: Grammar) {
        val newNonTerminals = mutableSetOf<String>()
        val newTerminals = mutableSetOf<String>()
        var productionCount = 1

        // Passo 5: Converte para a forma normal de Greibach
        g.productions.forEach { production ->
            val right = mutableListOf<String>()

            for (symbol in production.right) {
                if (symbol in g.terminals) {
                    newTerminals.add(symbol)
                    right.add(symbol)
                } else if (symbol in g.nonTerminals) {
                    newNonTerminals.add(symbol)
                    right.add(symbol)
                } else {
                    val newNonTerminal = "N${productionCount++}"
                    newNonTerminals.add(newNonTerminal)
                    right.add(newNonTerminal)
                    g.productions.add(Production(newNonTerminal, listOf(symbol)))
                }
            }

            if (right.isNotEmpty()) {
                g.productions.add(Production(production.left, right))
            }
        }

        g.nonTerminals = newNonTerminals
        g.terminals = newTerminals
    }

    // Classe que representa uma gramática
    data class Grammar(
        var nonTerminals: Set<String>,
        var terminals: Set<String>,
        val startSymbol: String,
        val productions: MutableList<Production> = mutableListOf()
    )

    // Classe que representa uma regra de produção
    data class Production(
        val left: String,
        val right: List<String>
    )

    companion object {
        // Constantes
        const val EPSILON = "ε"
    }

}