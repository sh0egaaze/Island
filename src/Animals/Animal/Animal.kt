package Animals.Animal

import Island.Entities.Eatable
import Island.Island
import Island.Entities.IslandElement
import Island.Entities.Plant

abstract class Animal(
    open val name: String,
    weight: Double,
    open val maxCount: Int,
    open val speed: Double,
    open val foodNeeded: Double,
    open val offspringCount: Int,
    open var isAlive: Boolean = true
) : IslandElement(weight), Eatable {
    var foodLevel: Double = foodNeeded / 2
    var currentLocation: Location? = null

    override var weight: Double = weight
        get() = field
        set(value) { field = value }

    abstract fun eat(location: Location)
    abstract fun move(island: Island)
    abstract fun reproduce(location: Location)

    open fun die() {
        isAlive = false
        val location = currentLocation
        if (location != null) {
            println("$name умер на координатах (${location.x}, ${location.y}).")
        } else {
            println("$name умер.")
        }
    }

    override fun beEaten(): Boolean {
        isAlive = false
        return true
    }

    override fun toString(): String {
        return "$name (Вес: $weight, Еда: $foodLevel)"
    }
}

data class Location(val x: Int, val y: Int) {
    val plants: MutableList<Plant> = mutableListOf()
    val animals: MutableList<Animal> = mutableListOf()

    override fun toString(): String {
        return "Location(x=$x, y=$y, Plants=${plants.size}, Animals=${animals.size})"
    }
}