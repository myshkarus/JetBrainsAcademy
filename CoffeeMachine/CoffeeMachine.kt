package machine

enum class Coffee(val coffeeDrink: String, val water: Int, val milk: Int, val coffeeBeans: Int, val price: Int) {
    ESPRESSO("Espresso", 250, 0, 16, 4),
    LATTE("Latte", 350, 75, 20, 7),
    CAPPUCCINO("Cappuccino", 200, 100, 12, 6);
}

class CoffeeMachine(
    private var waterAvailable: Int,
    private var milkAvailable: Int,
    private var coffeeBeansAvailable: Int,
    private var disposableCups: Int,
    private var moneyBox: Int
) {
    private var _state: String = "select_action"
    private var ordered: Coffee = Coffee.ESPRESSO

    private var coffee: String = ""
        set(value: String) {
            when (value) {
                "1" -> this.ordered = Coffee.ESPRESSO
                "2" -> this.ordered = Coffee.LATTE
                "3" -> this.ordered = Coffee.CAPPUCCINO
            }
            field = this.ordered.coffeeDrink
        }

    var state: String = ""
        get() {
            if (this._state == "select_action") println("\nWrite action (buy, fill, take, remaining, exit):")
            return this._state
        }

    fun readInput(value: String) {
        when (value) {
            "buy" -> {
                println("\nWhat do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino:")
                this._state = "buy_action"
            }

            "back" -> this._state = "select_action"

            "fill" -> {
                println("\nWrite how many ml of water you want to add:")
                val water = readln().toInt()
                println("Write how many ml of milk you want to add:")
                val milk = readln().toInt()
                println("Write how many grams of coffee beans you want to add:")
                val coffeeBeans = readln().toInt()
                println("Write how many disposable cups you want to add:")
                val disposalCups = readln().toInt()
                this.fill(water, milk, coffeeBeans, disposalCups)
                this._state = "select_action"
            }

            "take" -> {
                this.take()
                this._state = "select_action"
            }

            "remaining" -> {
                this.printState()
                this._state = "select_action"
            }

            "exit" -> this._state = "exit_action"
            else -> {
                if (this._state == "buy_action") {
                    this.coffee = value
                    if (this.isEnoughResource(this.ordered)) {
                        this.prepareCoffee(this.ordered)
                        this.receiveMoney(this.ordered)
                    }
                    this._state = "select_action"
                }
            }
        }
    }

    private fun fill(water: Int, milk: Int, coffeeBeans: Int, disposalCups: Int) {
        waterAvailable += water
        milkAvailable += milk
        coffeeBeansAvailable += coffeeBeans
        disposableCups += disposalCups
    }

    private fun take() {
        println("I gave you $${this.moneyBox}")
        this.moneyBox = 0
    }

    private fun printState() {
        println(
            "\nThe coffee machine has:" +
                    "\n${this.waterAvailable} ml of water" +
                    "\n${this.milkAvailable} ml of milk" +
                    "\n${this.coffeeBeansAvailable} g of coffee beans" +
                    "\n${this.disposableCups} disposable cups" +
                    "\n$${this.moneyBox} of money"
        )
    }

    private fun receiveMoney(coffee: Coffee) {
        moneyBox += coffee.price
    }

    private fun prepareCoffee(coffee: Coffee) {
        waterAvailable -= coffee.water
        milkAvailable -= coffee.milk
        coffeeBeansAvailable -= coffee.coffeeBeans
        disposableCups--
    }

    private fun ingredientRequired(value: Int): Boolean {
        return value > 0
    }

    private fun isEnoughResource(coffee: Coffee): Boolean {
        var enough = true
        var lackOf = ""
        if (ingredientRequired(coffee.water) && waterAvailable / coffee.water < 1) {
            lackOf = "water"
            enough = false
        } else if (ingredientRequired(coffee.milk) && milkAvailable / coffee.milk < 1) {
            lackOf = "milk"
            enough = false
        } else if (ingredientRequired(coffee.coffeeBeans) && coffeeBeansAvailable / coffee.coffeeBeans < 1) {
            lackOf = "coffee beans"
            enough = false
        } else if (disposableCups < 1) {
            lackOf = "disposable cups"
            enough = false
        }

        if (enough) {
            println("I have enough resources, making you a coffee!")
        } else {
            println("Sorry, not enough $lackOf!")
        }
        return enough
    }
}

fun main() {
    val coffeeMachine = CoffeeMachine(
        waterAvailable = 400,
        milkAvailable = 540,
        coffeeBeansAvailable = 120,
        disposableCups = 9,
        moneyBox = 550
    )

    while (coffeeMachine.state != "exit_action") {
        coffeeMachine.readInput(readln())
    }
}