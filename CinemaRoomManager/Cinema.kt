package cinema

const val EMPTY = "S"
const val TAKEN = "B"
const val STANDARD_PRICE = 10
const val BACK_SEATS_PRICE = 8
const val MAX_SEATS = 60
const val MENU = """
1. Show the seats
2. Buy a ticket
3. Statistics
0. Exit"""

var rows: Int = 0
var seats: Int = 0
var ticketsSold: Int = 0
var currentIncome: Int = 0
var cinema: MutableList<MutableList<String>> = mutableListOf()

fun totalIncome(): Int {
    var income = 0
    for(row in 1..cinema.lastIndex) {
        val price = getPrice(row)
        income += (seats * price)
    }
    return income
}

fun showSeats() {
    println("\nCinema:")
    for (row in cinema) {
        println(row.joinToString(" "))
    }
}

fun showStatistics() {
    val percentage = "%.2f".format(ticketsSold.toDouble() * 100 / (rows * seats))
    println("\nNumber of purchased tickets: $ticketsSold")
    println("Percentage: $percentage%")
    println("Current income: $$currentIncome")
    println("Total income: $${totalIncome()}")
}

fun selectFromMenu(): Int {
    print(MENU + "\n")
    val menu = readln().toInt()

    when (menu) {
        1 -> showSeats()
        2 -> buyTicket()
        3 -> showStatistics()
        0 -> {}
        else -> readln().toInt()
    }
    return menu
}

fun createCinema(rows: Int, seats: Int) {
    cinema = MutableList(rows) { MutableList(seats) { EMPTY } }

    for (r in 0..cinema.lastIndex + 1) {
        if (r == 0) {
            cinema.add(0, (1..seats).map { it.toString() }.toMutableList())
            cinema[r].add(0, " ")
        } else {
            cinema[r].add(0, (r).toString())
        }
    }
}

fun getPrice(row: Int): Int {
    val price = if (rows * seats < MAX_SEATS || row <= rows / 2) {
        STANDARD_PRICE
    } else {
        BACK_SEATS_PRICE
    }
    return price
}

fun isTicketAvailable(row: Int, seat: Int): Boolean {
    return cinema[row][seat] == "S"
}

fun buyTicket() {
    println("\nEnter a row number:")
    val rowNum = readln().toInt()
    println("Enter a seat number in that row:")
    val seatNum = readln().toInt()

    try {
        if (isTicketAvailable(rowNum, seatNum)) {
            cinema[rowNum][seatNum] = TAKEN
            ticketsSold++
            val price = getPrice(rowNum)
            currentIncome += price
            println("Ticket price: $$price")
        } else {
            println("\nThat ticket has already been purchased!")
            buyTicket()
        }
    } catch (e: Exception) {
        println("\nWrong input!")
        buyTicket()
    }
}

fun main() {
    var option: Int

    println("Enter the number of rows: ")
    rows = readln().toInt()
    println("Enter the number of seats in each row: ")
    seats = readln().toInt()

    createCinema(rows, seats)

    do {
        option = selectFromMenu()
    } while (option != 0)
}