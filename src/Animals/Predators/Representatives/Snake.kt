package Animals.Predators.Representatives

import Animals.Predators.Predator
import Simulation.Config.SimulationConfig

class Snake : Predator(
    name = "Удав",
    weight = SimulationConfig.animalCharacteristics["Удав"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Удав"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Удав"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Удав"]!!.foodNeeded,
    offspringCount = SimulationConfig.animalCharacteristics["Удав"]!!.offspringCount
)