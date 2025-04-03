package Animals.Predators

import Animals.Animal.Animal
import Animals.Animal.Location
import Animals.Predators.Representatives.*
import Island.Island
import Simulation.Config.SimulationConfig
import kotlin.random.Random

abstract class Predator(
    name: String,
    weight: Double,
    maxCount: Int,
    speed: Double,
    foodNeeded: Double,
    offspringCount: Int
) : Animal(name, weight, maxCount, speed, foodNeeded, offspringCount) {

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
                        println("$name съел ${target.name} на координатах (${location.x}, ${location.y}).")
                    }
                } else {
                    foodLevel -= foodNeeded * 0.25
                    println("$name не смог найти еду на координатах (${location.x}, ${location.y}).")
                }
            } else {
                foodLevel -= foodNeeded * 0.25
                println("$name не смог найти еду на координатах (${location.x}, ${location.y}).")
            }
        }
    }

    override fun reproduce(location: Location) {
        val potentialMate = location.animals.firstOrNull { it::class == this::class && it != this } as? Predator
        if (potentialMate != null) {
            val speciesCount = location.animals.count { it.name == name }
            if (speciesCount < SimulationConfig.animalCharacteristics[name]!!.maxCount) {
                val offspringCount = SimulationConfig.animalCharacteristics[name]!!.offspringCount
                repeat(offspringCount) {
                    val newPredator = when (this) {
                        is Wolf -> Wolf()
                        is Snake -> Snake()
                        is Fox -> Fox()
                        is Bear -> Bear()
                        is Eagle -> Eagle()
                        else -> return
                    }
                    location.animals.add(newPredator)
                    newPredator.currentLocation = location
                    println("$name размножился на координатах (${location.x}, ${location.y}). Новый ${newPredator.name} появился.")
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