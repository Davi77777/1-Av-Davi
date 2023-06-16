package Exercicio01

import java.util.*

//Maria Luíza do Carmo de Santana- 01357229
//Davi de Mendonça Vasconcelos Alves Coutinho - 01383910
//Arllom de Fraga Dutra - 01358091
fun main(args: Array<String>) {
    val input = Scanner(System.`in`)

    println("############## Primeira parte ##############")
    println("-------------- Digite um texto -------------")
    val text = input.nextLine()

    val token = checkCharacters(text)
    verificarConsoanteEVogal(token)

}

fun checkCharacters(text: String): List<Char> {
    val charactersToCheck = listOf(
        'j',
        'w',
        'k',
        'y',
        'ç',
        'h',
        'q',
        '/',
        '(',
        ')',
        '&',
        '%',
        '$',
        '#',
        '@',
        '!'
    )
    val missingCharacters = mutableListOf<Char>()

    for (char in text) {
        if (!charactersToCheck.contains(char)) {
            missingCharacters.add(char)
        }
    }

    return missingCharacters
}

fun verificarConsoanteEVogal(lista: List<Char>) {
    val vogais = listOf("a", "e", "i", "o", "u")
    val consoantes = ('a'..'z').filterNot { it.toString() in vogais }
    val digrafosConsoantes = listOf("ch", "nh", "lh", "rr", "ss", "sc", "sç")

    var temVogal = false
    var temConsoante = false
    var temDigrafoConsoante = false

    for (palavra in lista) {
        for (letra in palavra.lowercase()) {
            if (letra.toString() in vogais) {
                temVogal = true
            } else if (letra in consoantes) {
                temConsoante = true
            }
        }

        for (digrafo in digrafosConsoantes) {
            if (digrafo in palavra.lowercase()) {
                temDigrafoConsoante = true
            }
        }
    }
    if (temDigrafoConsoante || !temVogal || !temConsoante) {
        println("Palavra inválida")
    } else {
        println("Palavra válida")
    }
}