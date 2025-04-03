package Animals.Herbivores

import Animals.Animal.Animal
import Animals.Animal.Location
import Animals.Herbivores.Representatives.*
import Island.Island
import Simulation.Config.SimulationConfig
import kotlin.random.Random

abstract class Herbivore(
    name: String,
    weight: Double,
    maxCount: Int,
    speed: Double,
    foodNeeded: Double,
    offspringCount: Int
) : Animal(name, weight, maxCount, speed, foodNeeded, offspringCount) {

    override fun eat(location: Location) {
        if (foodLevel < foodNeeded) {
            if (SimulationConfig.foodProbabilities[name]?.containsKey("Растения") == true) {
                if (location.plants.isNotEmpty()) {
                    val plant = location.plants.removeAt(0)
                    foodLevel = foodNeeded
                    weight += plant.weight * 0.05
                    println("$name съел растение на координатах (${location.x}, ${location.y}).")
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
                            println("$name съел $foodName на координатах (${location.x}, ${location.y}).")
                            return
                        }
                    }
                }
            }

            foodLevel -= foodNeeded * 0.25
            println("$name не смог найти еду на координатах (${location.x}, ${location.y}).")
        }
    }

    override fun reproduce(location: Location) {
        val potentialMate = location.animals.firstOrNull { it::class == this::class && it != this } as? Herbivore
        if (potentialMate != null) {
            val speciesCount = location.animals.count { it.name == name }
            if (speciesCount < SimulationConfig.animalCharacteristics[name]!!.maxCount) {
                val offspringCount = SimulationConfig.animalCharacteristics[name]!!.offspringCount
                repeat(offspringCount) {
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
                    newHerbivore.currentLocation = location
                    println("$name размножился на координатах (${location.x}, ${location.y}). Новый ${newHerbivore.name} появился.")
                }
            }
        }
    }

    override fun move(island: Island) {
        val speed = speed.toInt()
        var steps = Random.nextInt(1, speed + 1)
        var currentLoc = currentLocation!!
        while (steps > 0) {
            val possibleMoves = island.getAdjacentLocations(currentLoc)
            if (possibleMoves.isNotEmpty()) {
                val newLocation = possibleMoves.random()
                island.moveAnimal(this, currentLoc, newLocation)
                currentLoc = newLocation
            }
            steps--
        }
    }
}