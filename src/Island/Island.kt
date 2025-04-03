package Island

import Animals.Animal.Animal
import Animals.Animal.Location
import Animals.Herbivores.Representatives.*
import Animals.Predators.Representatives.*
import Island.Entities.Plant
import Simulation.Config.SimulationConfig
import kotlin.random.Random

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
            val speciesCount = location.animals.count { it.name == animal.name }
            if (speciesCount < SimulationConfig.animalCharacteristics[animal.name]!!.maxCount) {
                location.animals.add(animal)
                animal.currentLocation = location
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
        val speciesCount = newLocation.animals.count { it.name == animal.name }
        if (oldLocation.animals.contains(animal) && speciesCount < SimulationConfig.animalCharacteristics[animal.name]!!.maxCount) {
            oldLocation.animals.remove(animal)
            newLocation.animals.add(animal)
            animal.currentLocation = newLocation
        }
    }

    fun getTotalAnimalCount(): Int {
        return grid.sumOf { row -> row.sumOf { it.animals.size } }
    }
}