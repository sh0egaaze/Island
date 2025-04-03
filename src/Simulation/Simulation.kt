package Simulation

import Island.Island
import Simulation.Config.SimulationConfig
import java.util.concurrent.Executors
import kotlin.random.Random

object Simulation {
    private val island = Island(SimulationConfig.ISLAND_WIDTH, SimulationConfig.ISLAND_HEIGHT)
    private val scheduledExecutor = Executors.newScheduledThreadPool(2)
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
    }

    private fun growPlants() {
        island.grid.forEach { row ->
            row.forEach { location ->
                if (location.plants.size < SimulationConfig.MAX_PLANTS_PER_CELL) {
                    if (Random.nextDouble() < 0.1) {
                        val newPlants = Random.nextInt(1, 6)
                        repeat(newPlants) {
                            island.addPlant(location)
                        }
                        println("Растения выросли на координатах (${location.x}, ${location.y}). Новое количество: ${location.plants.size}")
                    }
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
                                println("${animal.name} умер от голода на координатах (${location.x}, ${location.y}).")
                            }
                        }
                    }
                }
            }
        }

        printStatistics()

        if (island.getTotalAnimalCount() == 0) {
            stop()
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

        println("-------------------- СТАТИСТИКА --------------------")
        println("Всего животных: $totalAnimals")
        animalCounts.forEach { (name, count) ->
            println("$name: $count")
        }

        var totalPlants = 0
        island.grid.forEach { row ->
            row.forEach { location ->
                totalPlants += location.plants.size
            }
        }
        println("Всего растений: $totalPlants")
        println("----------------------------------------------------")
    }

    fun stop() {
        SimulationConfig.IS_SIMULATION_RUNNING = false
        scheduledExecutor.shutdown()
        animalExecutor.shutdown()
        println("Симуляция остановлена.")
    }
}
