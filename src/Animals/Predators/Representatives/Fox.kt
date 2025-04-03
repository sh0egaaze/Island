package Animals.Predators.Representatives

import Animals.Predators.Predator
import Simulation.Config.SimulationConfig

class Fox : Predator(
    name = "Лиса",
    weight = SimulationConfig.animalCharacteristics["Лиса"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Лиса"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Лиса"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Лиса"]!!.foodNeeded,
    offspringCount = SimulationConfig.animalCharacteristics["Лиса"]!!.offspringCount
)