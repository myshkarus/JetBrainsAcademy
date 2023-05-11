package chucknorris

fun getAction(): String {
    var notSelected = true
    var actionSelected = ""

    while (notSelected) {
        println("Please input operation (encode/decode/exit):")
        actionSelected = readln()
        if (actionSelected !in listOf("exit", "encode", "decode")) {
            println("There is no '$actionSelected' operation")
            continue
        } else {
            notSelected = false
        }
    }
    return actionSelected
}

fun main() {
    when (getAction()) {
        "encode" -> {
            val message = readInput()
            println("Encoded string:\n${encode(message)}")
            main()
        }
        "decode" -> {
            val cipher = readEncodedString()
            if (cipher.isNotEmpty()) println("Decoded string:\n${decode(cipher)}")
            main()
        }
        "exit" -> println("Bye!")
    }
}

fun encode(message: CharSequence): String {
    var encoded = ""
    var chunk = ""
    var binary = ""
    for (char in message) {
        binary += Integer.toBinaryString(char.code).padStart(7, '0')
    }
    for (i in binary.indices) {
        chunk += binary[i]
        if (i < binary.length - 1 && binary[i] == binary[i + 1]) {
            continue
        } else {
            var bit: String
            if (chunk.first() == '0') bit = "00" else bit = "0"
            encoded += ("$bit ${"0".repeat(chunk.length)} ")
            chunk = ""
        }
    }
    return encoded
}

fun decode(cipher: List<List<String>>): String {
    var str = ""
    var decoded = ""

    for (chunk in cipher) {
        if (chunk[0] == "00") {
            str += "0".repeat(chunk[1].length)
        } else {
            str += "1".repeat(chunk[1].length)
        }
    }

    for (s in str.chunked(7)) {
        decoded += Integer.parseInt(s, 2).toChar()
    }

    return decoded
}

fun readEncodedString(): List<List<String>> {
    var encoded: List<List<String>> = listOf()
    var multiple = 0

    println("Input encoded string:")

    val input = readln().split(' ')

    if (input.size % 2 != 0) {
        println("Encoded string is not valid.")
        return listOf()
    } else {
        encoded = input.chunked(2)
        for (e in encoded) {
            multiple += e[1].length
            if (e[0] != "0" && e[0] != "00" || e[1].toInt() != 0) {
                println("Encoded string is not valid.")
                return listOf()
            }
        }
    }

    if (multiple % 7 != 0) {
        println("Encoded string is not valid.")
        return listOf()
    }
    return encoded
}

fun readInput(): CharSequence {
    println("Input string:")
    return readln()
}
