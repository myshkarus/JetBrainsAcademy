const val EMPTY = " "
const val FIRST = "X"
const val SECOND = "O"
var player: String = FIRST

fun main() {
    var rerun: Boolean = true
    val emptyGrid = " ".repeat(9)
    val grid = vectorToMatrix(emptyGrid, 3)

    printGrid(grid)
    while (rerun) {
        play(grid)
        printGrid(grid)
        analyze(grid)
        when(state(grid)) {
            "$FIRST wins", "$SECOND wins", "Draw" -> rerun = false
        }
    }
}

/**
 * Display the current state of the game.
 *
 * This function takes grid as matrix (mutable list of mutable list of string) and print the state to console.
 *
 * @param grid matrix as mutable list of mutable list of strings
 */
fun analyze(grid: MutableList<MutableList<String>>) {
    when (state(grid)) {
        "Impossible" -> println("Impossible")
        "Draw" -> println("Draw")
        "$FIRST wins" -> println("$FIRST wins")
        "$SECOND wins" -> println("$SECOND wins")
    }
}

/**
 * Play the game.
 *
 * The function expects the input of "X" or "O" string in console from players in turn, and change the grid elements.
 * In case of non-valid input, the error message is printed to console.
 *
 * @param grid matrix as mutable list of mutable list of strings
 */
fun play(grid: MutableList<MutableList<String>>) {
    var coordinates: List<Int> = listOf()
    var wrongMove: Boolean = false

    do {
        val play: String = readln()
        try {
            wrongMove = false
            if (play.contains(Regex("\\s"))) {
                coordinates = play.split(" ").map { it.toInt() }
            } else {
                coordinates = play.toCharArray().map { it.digitToInt() }.toList()
            }

            if (coordinates.size > 2) throw IndexOutOfBoundsException()

            if (grid[coordinates[0] - 1][coordinates[1] - 1] in "$FIRST$SECOND") {
                println("This cell is occupied! Choose another one!")
                wrongMove = true
            } else {
                grid[coordinates[0] - 1][coordinates[1] - 1] = player
                if (player == FIRST) player = SECOND else player = FIRST
            }
        } catch (e: IndexOutOfBoundsException) {
            println("Coordinates should be from 1 to 3!")
            wrongMove = true
        } catch (e: IllegalArgumentException) {
            println("You should enter numbers!")
            wrongMove = true
        }
    } while (wrongMove)
}

/**
 * Return the current state of the game.
 *
 * This function takes grid as matrix as mutable list of mutable list of string and return the string with
 * description of current game state.
 * Possible states:
 *  - "Game not finished"
 *  - "Impossible"
 *  - "X wins"
 *  - "O wins"
 *  - "Draw"
 *
 * @param grid matrix as mutable list of mutable list of strings
 * @return state of the game as string
 */
fun state(grid: MutableList<MutableList<String>>): String {
    val curState = gameCombinations(grid)
    var i: Int = 0
    val size = curState[0].lastIndex + 1
    val result: String = "Game not finished"

    for (st in grid.joinToString("")) {
        if (st.toString() == FIRST) {
            i++
        } else if (st.toString() == SECOND) {
            i--
        }
    }
    if (i > 1 || i < -1) return "Impossible"

    if (FIRST.repeat(size) in curState && SECOND.repeat(size) in curState) return "Impossible"

    for (combination in curState) {
        if (combination == FIRST.repeat(combination.length)) {
            return "$FIRST wins"
        } else if (combination == SECOND.repeat(combination.length)) {
            return "$SECOND wins"
        }
    }

    if (EMPTY !in curState.joinToString("")) return "Draw"
    return result
}

/**
 * Return the mutable list of all valid game combinations.
 *
 * This function takes grid as matrix as mutable list of mutable list of string and return the list of all
 * valid game combinations - rows, columns, and diagonals
 *
 * Example usage:
 * ```
 * val rows = listOf(
 *                   listOf("X", "O", "X"),
 *                   listOf("O", "X", "O"),
 *                   listOf("X", "O", "X")
 *                   )
 * val mutableList = rows.map { it.toMutableList() }.toMutableList()
 * print(gameCombinations(mutableList)) // output [XOX, OXO, XOX, XXX, XOX, OXO, XOX, XXX]
 *  ```
 *
 * @param grid matrix as mutable list of mutable list of strings
 * @return list of valid combinations
 */
fun gameCombinations(grid: MutableList<MutableList<String>>): MutableList<String> {
    val allCombo: MutableList<String> = mutableListOf()
    var combo: String = ""

    // combinations by rows and left diagonal
    for (row in 0..grid.lastIndex) {
        allCombo.add(grid[row].joinToString(""))
        combo += grid[row][row]
    }
    allCombo.add(combo)

    // combinations by columns
    for (col in 0..grid[0].lastIndex) {
        combo = ""
        for (row in 0..grid.lastIndex) {
            combo += grid[row][col]
        }
        allCombo.add(combo)
    }

    // combination by right diagonal
    combo = ""
    for (col in grid[0].lastIndex downTo 0) {
        combo += grid[grid[0].lastIndex - col][col]
    }
    allCombo.add(combo)
    return allCombo
}

/**
 * Checks if the matrix is square matrix of 'm x m'.
 *
 * This function takes matrix as mutable list of mutable list of string and check if the number of rows is
 * equal the number of column of matrix.
 *
 * @param grid matrix as mutable list of mutable list of strings
 * @return Boolean
 */
fun isSquareMatrix(grid: MutableList<MutableList<String>>): Boolean {
    if (grid != null && grid.lastIndex > 0) {
        return grid.lastIndex == grid[0].lastIndex
    } else {
        return false
    }
}

/**
 * Creates matrix based on the vector.
 *
 * This function takes vector as string and split it into matrix rows of equal length calculated by
 * break point value. In case if the matrix is ragged, the function throws an error.
 *
 * Example usage:
 * ```
 * val grid = vectorToMatrix("XXXOOOXXX", 3)
 * println(grid) // output: [[X, X, X], [O, O, O], [X, X, X]]
 * ```
 *
 * @param v vector
 * @param breakPoint point by which to split vector into rows
 * @return matrix as mutable list of mutable list of strings
 */
fun vectorToMatrix(v: String, breakPoint: Int): MutableList<MutableList<String>> {
    var rows: Int = 0
    var matrix: MutableList<MutableList<String>> = mutableListOf()

    try {
        if (v.length % breakPoint == 0) {
            rows = v.length / breakPoint
            matrix = MutableList(rows) { MutableList(breakPoint) { " " } }
            for (row in 0 until rows) {
                for (col in 0 until breakPoint) {
                    matrix[row][col] = v[row * breakPoint + col].toString()
                }
            }
        } else {
            throw IndexOutOfBoundsException()
        }
    } catch (e: IndexOutOfBoundsException) {
        println("Can't create matrix from vector!")
    }
    return matrix
}

/**
 * Print matrix (grid).
 *
 * This function takes a matrix as a mutable list of mutable list of string and if it is square matrix
 * print it to console.
 *
 * @param grid matrix as mutable list of mutable list of strings
 */
fun printGrid(grid: MutableList<MutableList<String>>) {
    if (isSquareMatrix(grid)) {
        println("-".repeat(9))
        for (row in grid) {
            print(row.joinToString(" ", "| ", " |\n"))
        }
        println("-".repeat(9))
    }
}
