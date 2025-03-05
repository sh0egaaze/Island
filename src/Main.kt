import kotlin.coroutines.*
import java.util.*
import java.util.concurrent.Executors
import kotlin.math.floor
import kotlin.random.Random

object SimulationConfig {
    const val ISLAND_WIDTH = 10
    const val ISLAND_HEIGHT = 10
    const val SIMULATION_DURATION_MS = 10000L
    const val TICK_INTERVAL_MS = 1000L
    const val INITIAL_PLANT_DENSITY = 0.3
    const val STATISTICS_INTERVAL_MS = 5000L

    const val INITIAL_WOLF_COUNT = 5
    const val INITIAL_SNAKE_COUNT = 5
    const val INITIAL_FOX_COUNT = 5
    const val INITIAL_BEAR_COUNT = 2
    const val INITIAL_EAGLE_COUNT = 5
    const val INITIAL_HORSE_COUNT = 10
    const val INITIAL_DEER_COUNT = 10
    const val INITIAL_RABBIT_COUNT = 50
    const val INITIAL_MOUSE_COUNT = 100
    const val INITIAL_GOAT_COUNT = 20
    const val INITIAL_SHEEP_COUNT = 20
    const val INITIAL_BOAR_COUNT = 15
    const val INITIAL_BUFFALO_COUNT = 5
    const val INITIAL_DUCK_COUNT = 30
    const val INITIAL_CATERPILLAR_COUNT = 200

    const val MAX_ANIMALS_PER_CELL = 100
    const val MAX_PLANTS_PER_CELL = 200

    var IS_SIMULATION_RUNNING = true

    val animalCharacteristics = mapOf(
        "Волк" to AnimalCharacteristics(50.0, 30, 3.0, 8.0),
        "Удав" to AnimalCharacteristics(15.0, 30, 1.0, 3.0),
        "Лиса" to AnimalCharacteristics(8.0, 30, 2.0, 2.0),
        "Медведь" to AnimalCharacteristics(500.0, 5, 2.0, 80.0),
        "Орел" to AnimalCharacteristics(6.0, 20, 3.0, 1.0),
        "Лошадь" to AnimalCharacteristics(400.0, 20, 4.0, 60.0),
        "Олень" to AnimalCharacteristics(300.0, 20, 4.0, 50.0),
        "Кролик" to AnimalCharacteristics(2.0, 150, 2.0, 0.45),
        "Мышь" to AnimalCharacteristics(0.05, 500, 1.0, 0.01),
        "Коза" to AnimalCharacteristics(60.0, 140, 3.0, 10.0),
        "Овца" to AnimalCharacteristics(70.0, 140, 3.0, 15.0),
        "Кабан" to AnimalCharacteristics(400.0, 50, 2.0, 50.0),
        "Буйвол" to AnimalCharacteristics(700.0, 10, 3.0, 100.0),
        "Утка" to AnimalCharacteristics(1.0, 200, 4.0, 0.15),
        "Гусеница" to AnimalCharacteristics(0.01, 1000, 0.0, 0.0),
        "Растения" to AnimalCharacteristics(1.0, 200, 0.0, 0.0)
    )

    val foodProbabilities = mapOf(
        "Волк" to mapOf("Кролик" to 60, "Мышь" to 80, "Коза" to 60, "Овца" to 70, "Кабан" to 15, "Буйвол" to 10),
        "Удав" to mapOf("Лиса" to 15, "Кролик" to 20, "Мышь" to 40, "Утка" to 10),
        "Лиса" to mapOf("Кролик" to 70, "Мышь" to 90, "Утка" to 60, "Гусеница" to 40),
        "Медведь" to mapOf("Удав" to 80, "Лошадь" to 40, "Олень" to 80, "Кролик" to 80, "Мышь" to 90, "Коза" to 70, "Овца" to 70, "Кабан" to 50, "Буйвол" to 20, "Утка" to 10),
        "Орел" to mapOf("Лиса" to 10, "Кролик" to 90, "Мышь" to 90, "Утка" to 80),
        "Лошадь" to mapOf("Растения" to 100),
        "Олень" to mapOf("Растения" to 100),
        "Кролик" to mapOf("Растения" to 100),
        "Мышь" to mapOf("Гусеница" to 90, "Растения" to 100),
        "Коза" to mapOf("Растения" to 100),
        "Овца" to mapOf("Растения" to 100),
        "Кабан" to mapOf("Мышь" to 50, "Гусеница" to 90, "Растения" to 100),
        "Буйвол" to mapOf("Растения" to 100),
        "Утка" to mapOf("Гусеница" to 90, "Растения" to 100),
        "Гусеница" to mapOf("Растения" to 100)
    )
}

data class AnimalCharacteristics(
    val weight: Double,
    val maxCount: Int,
    val speed: Double,
    val foodNeeded: Double
)

interface Eatable {
    fun beEaten(): Boolean
    val weight: Double
}

abstract class IslandElement(open var weight: Double)

class Plant(weight: Double = SimulationConfig.animalCharacteristics["Растения"]!!.weight) : IslandElement(weight), Eatable {
    override fun beEaten(): Boolean {
        return true
    }
}

abstract class Animal(
    open val name: String,
    weight: Double,
    open val maxCount: Int,
    open val speed: Double,
    open val foodNeeded: Double,
    open var isAlive: Boolean = true
) : IslandElement(weight), Eatable {
    var foodLevel: Double = foodNeeded / 2

    override var weight: Double = weight
        get() = field
        set(value) { field = value }

    abstract fun eat(location: Location)
    abstract fun move(island: Island)
    abstract fun reproduce(location: Location)

    open fun die() {
        isAlive = false
        println("$name died.")
    }

    override fun beEaten(): Boolean {
        isAlive = false
        return true
    }

    override fun toString(): String {
        return "$name (Weight: $weight, Food: $foodLevel)"
    }

}

abstract class Predator(
    name: String,
    weight: Double,
    maxCount: Int,
    speed: Double,
    foodNeeded: Double
) : Animal(name, weight, maxCount, speed, foodNeeded) {

    override fun eat(location: Location) {
        if (foodLevel < foodNeeded) {
            val availableFood = location.animals.filter { it != this && it.isAlive }
            if (availableFood.isNotEmpty()) {
                val target = availableFood.firstOrNull {
                    val probability = SimulationConfig.foodProbabilities[name]?.get(it.name) ?: 0
                    Random.nextInt(0, 100) < probability
                }

                if (target != null) {
                    if (target.beEaten()) {
                        location.animals.remove(target)
                        foodLevel = foodNeeded
                        weight += target.weight * 0.1
                        println("$name ate a ${target.name}.")
                    }
                } else {
                    foodLevel -= foodNeeded * 0.25
                    println("$name couldn't find food.")
                }
            } else {
                foodLevel -= foodNeeded * 0.25
                println("$name couldn't find food.")
            }
        }
    }

    override fun reproduce(location: Location) {
        val potentialMate = location.animals.firstOrNull { it::class == this::class && it != this } as? Predator
        if (potentialMate != null && location.animals.size < SimulationConfig.MAX_ANIMALS_PER_CELL) {
            val newPredator = when (this) {
                is Wolf -> Wolf()
                is Snake -> Snake()
                is Fox -> Fox()
                is Bear -> Bear()
                is Eagle -> Eagle()
                else -> return
            }

            location.animals.add(newPredator)
            println("$name reproduced. New ${newPredator.name} appeared.")
        }
    }

    override fun move(island: Island) {
        val possibleMoves = island.getAdjacentLocations(island.getLocationOf(this)!!)
        if (possibleMoves.isNotEmpty()) {
            val newLocation = possibleMoves.random()
            island.moveAnimal(this, island.getLocationOf(this)!!, newLocation)
        }
    }
}

abstract class Herbivore(
    name: String,
    weight: Double,
    maxCount: Int,
    speed: Double,
    foodNeeded: Double
) : Animal(name, weight, maxCount, speed, foodNeeded) {

    override fun eat(location: Location) {
        if (foodLevel < foodNeeded) {
            if (SimulationConfig.foodProbabilities[name]?.containsKey("Растения") == true) {
                if (location.plants.isNotEmpty()) {
                    val plant = location.plants.removeAt(0)
                    foodLevel = foodNeeded
                    weight += plant.weight * 0.05
                    println("$name ate a plant.")
                    return
                }
            }

            SimulationConfig.foodProbabilities[name]?.forEach { (foodName, probability) ->
                if (foodName != "Растения") {
                    val target = location.animals.firstOrNull { it.name == foodName && it.isAlive }
                    if (target != null && Random.nextInt(0, 100) < probability) {
                        if (target.beEaten()) {
                            location.animals.remove(target)
                            foodLevel = foodNeeded
                            weight += target.weight * 0.1
                            println("$name ate a $foodName.")
                            return
                        }
                    }
                }
            }

            foodLevel -= foodNeeded * 0.25
            println("$name couldn't find food.")
        }
    }

    override fun reproduce(location: Location) {
        val potentialMate = location.animals.firstOrNull { it::class == this::class && it != this } as? Herbivore
        if (potentialMate != null && location.animals.size < SimulationConfig.MAX_ANIMALS_PER_CELL) {
            val newHerbivore = when (this) {
                is Horse -> Horse()
                is Deer -> Deer()
                is Rabbit -> Rabbit()
                is Mouse -> Mouse()
                is Goat -> Goat()
                is Sheep -> Sheep()
                is Boar -> Boar()
                is Buffalo -> Buffalo()
                is Duck -> Duck()
                is Caterpillar -> Caterpillar()
                else -> return
            }

            location.animals.add(newHerbivore)
            println("$name reproduced. New ${newHerbivore.name} appeared.")
        }
    }

    override fun move(island: Island) {
        val possibleMoves = island.getAdjacentLocations(island.getLocationOf(this)!!)
        if (possibleMoves.isNotEmpty()) {
            val newLocation = possibleMoves.random()
            island.moveAnimal(this, island.getLocationOf(this)!!, newLocation)
        }
    }
}

class Wolf : Predator(
    name = "Волк",
    weight = SimulationConfig.animalCharacteristics["Волк"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Волк"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Волк"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Волк"]!!.foodNeeded
)

class Snake : Predator(
    name = "Удав",
    weight = SimulationConfig.animalCharacteristics["Удав"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Удав"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Удав"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Удав"]!!.foodNeeded
)

class Fox : Predator(
    name = "Лиса",
    weight = SimulationConfig.animalCharacteristics["Лиса"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Лиса"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Лиса"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Лиса"]!!.foodNeeded
)

class Bear : Predator(
    name = "Медведь",
    weight = SimulationConfig.animalCharacteristics["Медведь"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Медведь"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Медведь"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Медведь"]!!.foodNeeded
)

class Eagle : Predator(
    name = "Орел",
    weight = SimulationConfig.animalCharacteristics["Орел"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Орел"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Орел"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Орел"]!!.foodNeeded
)

class Horse : Herbivore(
    name = "Лошадь",
    weight = SimulationConfig.animalCharacteristics["Лошадь"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Лошадь"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Лошадь"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Лошадь"]!!.foodNeeded
)

class Deer : Herbivore(
    name = "Олень",
    weight = SimulationConfig.animalCharacteristics["Олень"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Олень"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Олень"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Олень"]!!.foodNeeded
)

class Rabbit : Herbivore(
    name = "Кролик",
    weight = SimulationConfig.animalCharacteristics["Кролик"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Кролик"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Кролик"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Кролик"]!!.foodNeeded
)

class Mouse : Herbivore(
    name = "Мышь",
    weight = SimulationConfig.animalCharacteristics["Мышь"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Мышь"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Мышь"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Мышь"]!!.foodNeeded
)

class Goat : Herbivore(
    name = "Коза",
    weight = SimulationConfig.animalCharacteristics["Коза"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Коза"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Коза"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Коза"]!!.foodNeeded
)

class Sheep : Herbivore(
    name = "Овца",
    weight = SimulationConfig.animalCharacteristics["Овца"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Овца"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Овца"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Овца"]!!.foodNeeded
)

class Boar : Herbivore(
    name = "Кабан",
    weight = SimulationConfig.animalCharacteristics["Кабан"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Кабан"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Кабан"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Кабан"]!!.foodNeeded
)

class Buffalo : Herbivore(
    name = "Буйвол",
    weight = SimulationConfig.animalCharacteristics["Буйвол"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Буйвол"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Буйвол"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Буйвол"]!!.foodNeeded
)

class Duck : Herbivore(
    name = "Утка",
    weight = SimulationConfig.animalCharacteristics["Утка"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Утка"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Утка"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Утка"]!!.foodNeeded
)

class Caterpillar : Herbivore(
    name = "Гусеница",
    weight = SimulationConfig.animalCharacteristics["Гусеница"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Гусеница"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Гусеница"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Гусеница"]!!.foodNeeded
)

data class Location(val x: Int, val y: Int) {
    val plants: MutableList<Plant> = mutableListOf()
    val animals: MutableList<Animal> = mutableListOf()

    override fun toString(): String {
        return "Location(x=$x, y=$y, Plants=${plants.size}, Animals=${animals.size})"
    }
}

class Island(val width: Int, val height: Int) {
    val grid: Array<Array<Location>> = Array(width) { x ->
        Array(height) { y ->
            Location(x, y)
        }
    }

    fun populateInitialAnimals() {
        repeat(SimulationConfig.INITIAL_WOLF_COUNT) { addAnimal(Wolf()) }
        repeat(SimulationConfig.INITIAL_SNAKE_COUNT) { addAnimal(Snake()) }
        repeat(SimulationConfig.INITIAL_FOX_COUNT) { addAnimal(Fox()) }
        repeat(SimulationConfig.INITIAL_BEAR_COUNT) { addAnimal(Bear()) }
        repeat(SimulationConfig.INITIAL_EAGLE_COUNT) { addAnimal(Eagle()) }
        repeat(SimulationConfig.INITIAL_HORSE_COUNT) { addAnimal(Horse()) }
        repeat(SimulationConfig.INITIAL_DEER_COUNT) { addAnimal(Deer()) }
        repeat(SimulationConfig.INITIAL_RABBIT_COUNT) { addAnimal(Rabbit()) }
        repeat(SimulationConfig.INITIAL_MOUSE_COUNT) { addAnimal(Mouse()) }
        repeat(SimulationConfig.INITIAL_GOAT_COUNT) { addAnimal(Goat()) }
        repeat(SimulationConfig.INITIAL_SHEEP_COUNT) { addAnimal(Sheep()) }
        repeat(SimulationConfig.INITIAL_BOAR_COUNT) { addAnimal(Boar()) }
        repeat(SimulationConfig.INITIAL_BUFFALO_COUNT) { addAnimal(Buffalo()) }
        repeat(SimulationConfig.INITIAL_DUCK_COUNT) { addAnimal(Duck()) }
        repeat(SimulationConfig.INITIAL_CATERPILLAR_COUNT) { addAnimal(Caterpillar()) }
    }

    fun initializePlants() {
        grid.forEach { row ->
            row.forEach { location ->
                val plantCount = (Random.nextDouble() * SimulationConfig.MAX_PLANTS_PER_CELL * SimulationConfig.INITIAL_PLANT_DENSITY).toInt()
                repeat(plantCount) {
                    location.plants.add(Plant())
                }
            }
        }
    }

    fun addAnimal(animal: Animal) {
        var placed = false
        while (!placed) {
            val x = Random.nextInt(0, width)
            val y = Random.nextInt(0, height)
            val location = grid[x][y]
            if (location.animals.size < SimulationConfig.MAX_ANIMALS_PER_CELL) {
                location.animals.add(animal)
                placed = true
            }
        }
    }

    fun addPlant(location: Location, plant: Plant = Plant()) {
        if (location.plants.size < SimulationConfig.MAX_PLANTS_PER_CELL) {
            location.plants.add(plant)
        }
    }

    fun removeAnimal(animal: Animal, location: Location) {
        location.animals.remove(animal)
    }

    fun getLocationOf(animal: Animal): Location? {
        grid.forEach { row ->
            row.forEach { location ->
                if (location.animals.contains(animal)) {
                    return location
                }
            }
        }
        return null
    }

    fun getAdjacentLocations(location: Location): List<Location> {
        val adjacentLocations = mutableListOf<Location>()
        val x = location.x
        val y = location.y

        if (x > 0) adjacentLocations.add(grid[x - 1][y])
        if (x < width - 1) adjacentLocations.add(grid[x + 1][y])
        if (y > 0) adjacentLocations.add(grid[x][y - 1])
        if (y < height - 1) adjacentLocations.add(grid[x][y + 1])

        return adjacentLocations
    }

    fun moveAnimal(animal: Animal, oldLocation: Location, newLocation: Location) {
        if (oldLocation.animals.contains(animal) && newLocation.animals.size < SimulationConfig.MAX_ANIMALS_PER_CELL) {
            oldLocation.animals.remove(animal)
            newLocation.animals.add(animal)
        }
    }
}

object Simulation {
    private val island = Island(SimulationConfig.ISLAND_WIDTH, SimulationConfig.ISLAND_HEIGHT)
    private val scheduledExecutor = Executors.newScheduledThreadPool(3)
    private val animalExecutor = Executors.newFixedThreadPool(10)

    fun start() {
        island.initializePlants()
        island.populateInitialAnimals()

        scheduledExecutor.scheduleAtFixedRate({
            growPlants()
        }, 0, SimulationConfig.TICK_INTERVAL_MS, java.util.concurrent.TimeUnit.MILLISECONDS)

        scheduledExecutor.scheduleAtFixedRate({
            animalLifecycle()
        }, 0, SimulationConfig.TICK_INTERVAL_MS, java.util.concurrent.TimeUnit.MILLISECONDS)

        scheduledExecutor.scheduleAtFixedRate({
            printStatistics()
        }, 0, SimulationConfig.STATISTICS_INTERVAL_MS, java.util.concurrent.TimeUnit.MILLISECONDS)

        scheduledExecutor.schedule({
            stop()
        }, SimulationConfig.SIMULATION_DURATION_MS, java.util.concurrent.TimeUnit.MILLISECONDS)
    }

    private fun growPlants() {
        island.grid.forEach { row ->
            row.forEach { location ->
                if (location.plants.size < SimulationConfig.MAX_PLANTS_PER_CELL) {
                    val growthFactor = 0.1 + Random.nextDouble() * 0.1
                    val newPlants = (SimulationConfig.MAX_PLANTS_PER_CELL * growthFactor).toInt()
                    repeat(newPlants) {
                        island.addPlant(location)
                    }
                    println("Plants grew at location ${location.x}, ${location.y}. New count: ${location.plants.size}")
                }
            }
        }
    }

    private fun animalLifecycle() {
        island.grid.forEach { row ->
            row.forEach { location ->
                val animals = location.animals.toList()

                animals.forEach { animal ->
                    animalExecutor.submit {
                        if (animal.isAlive) {
                            animal.eat(location)
                            animal.move(island)
                            if (location.animals.count { it::class == animal::class } > 1 && Random.nextDouble() < 0.1) {
                                animal.reproduce(location)
                            }

                            animal.foodLevel -= animal.foodNeeded * 0.1
                            if (animal.foodLevel <= 0) {
                                animal.die()
                                location.animals.remove(animal)
                                println("${animal.name} starved to death.")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun printStatistics() {
        var totalAnimals = 0
        val animalCounts = mutableMapOf<String, Int>()

        island.grid.forEach { row ->
            row.forEach { location ->
                totalAnimals += location.animals.size
                location.animals.forEach { animal ->
                    animalCounts[animal.name] = (animalCounts[animal.name] ?: 0) + 1
                }
            }
        }

        println("-------------------- STATISTICS --------------------")
        println("Total Animals: $totalAnimals")
        animalCounts.forEach { (name, count) ->
            println("$name: $count")
        }

        var totalPlants = 0
        island.grid.forEach { row ->
            row.forEach { location ->
                totalPlants += location.plants.size
            }
        }
        println("Total Plants: $totalPlants")
        println("----------------------------------------------------")
    }

    fun stop() {
        SimulationConfig.IS_SIMULATION_RUNNING = false
        scheduledExecutor.shutdown()
        animalExecutor.shutdown()
        println("Simulation stopped.")
    }
}

fun main() {
    Simulation.start()
}