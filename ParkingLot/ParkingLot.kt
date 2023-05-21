package parking

data class Spot(val number: Int, var car: String = "", var color: String = "")

class ParkingLot {
    private var lot: MutableList<Spot> = mutableListOf()

    fun create(spots: Int) {
        lot.clear()
        if (spots > 0) {
            for (number in 1..spots) this.lot.add(Spot(number))
            println("Created a parking lot with $spots spots.")
        }
    }

    private fun isCreated(): Boolean {
        var created = false
        if (this.lot.isEmpty()) {
            println("Sorry, a parking lot has not been created.")
        } else {
            created = true
        }
        return created
    }

    private fun getFreeSpot(): Int {
        for (spot in this.lot) {
            if (spot.car.isEmpty()) {
                return spot.number
            }
        }
        return -1
    }

    fun park(car: String, color: String) {
        if (this.isCreated()) {
            val freeSpot = this.getFreeSpot()
            if (freeSpot != -1) {
                for (spot in this.lot) {
                    if (spot.number == freeSpot) {
                        spot.car = car
                        spot.color = color
                        println("$color car parked in spot $freeSpot.")
                        break
                    }
                }
            } else {
                println("Sorry, the parking lot is full.")
            }
        }
    }

    fun leave(spot: Int) {
        if (this.isCreated()) {
            for (s in this.lot) {
                if (s.number == spot) {
                    if (s.car.isNotEmpty()) {
                        s.car = ""
                        s.color = ""
                        println("Spot $spot is free.")
                    } else {
                        println("There is no car in spot $spot.")
                    }
                    break
                }
            }
        }
    }

    fun status() {
        if (this.isCreated()) {
            var occupied = 0
            var carList = ""
            for (spot in this.lot) {
                if (spot.car.isNotEmpty()) {
                    carList += "${spot.number} ${spot.car} ${spot.color}\n"
                    occupied++
                }
            }
            when (occupied) {
                0 -> println("Parking lot is empty.")
                else -> println(carList.trimEnd())
            }
        }
    }

    fun regByColor(color: String) {
        if(this.isCreated()) {
            val filtered = filterByColor(color, "car")
            if (filtered != "") println(filtered)
        }
    }

    fun spotByColor(color: String) {
        if (this.isCreated()) {
            val filtered = filterByColor(color, "spot")
            if (filtered != "") println(filtered)
        }
    }

    fun spotByReg(car: String) {
        if (this.isCreated()) {
            var number = 0
            for (spot in this.lot) {
                if (spot.car == car) {
                    number = spot.number
                }
            }
            if (number != 0) println(number) else println("No cars with registration number $car were found.")
        }
    }

    private fun filterByColor(color: String, whatToFind: String): String {
        var listByFilter = ""
        if (this.isCreated()) {
            for (spot in this.lot) {
                if (spot.color.lowercase() == color.lowercase()) {
                    if (whatToFind=="spot") {
                        listByFilter += "${spot.number}, "
                    } else {
                        listByFilter += "${spot.car}, "
                    }
                }
            }
            if (listByFilter == "") {
                println("No cars with color $color were found.")
            } else {
                listByFilter = listByFilter.dropLast(2)
            }
        }
        return listByFilter
    }
}

fun main() {
    var parkingLot = ParkingLot()

    while (true) {
        var input = readlnOrNull()?.split(" ") ?: error("Enter the command!")
        var command = input.first()
        var arguments = input.subList(1, input.size)

        when (command) {
            "exit" -> break
            "create" -> parkingLot.create(arguments[0].toInt())
            "park" -> parkingLot.park(arguments[0], arguments[1])
            "leave" -> parkingLot.leave(arguments[0].toInt())
            "status" -> parkingLot.status()
            "reg_by_color" -> parkingLot.regByColor(arguments[0])
            "spot_by_color" -> parkingLot.spotByColor(arguments[0])
            "spot_by_reg" -> parkingLot.spotByReg(arguments[0])
            else -> continue
        }
    }
}