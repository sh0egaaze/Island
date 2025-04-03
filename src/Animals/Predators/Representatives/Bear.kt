package Animals.Predators.Representatives

import Animals.Predators.Predator
import Simulation.Config.SimulationConfig

class Bear : Predator(
    name = "Медведь",
    weight = SimulationConfig.animalCharacteristics["Медведь"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Медведь"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Медведь"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Медведь"]!!.foodNeeded,
    offspringCount = SimulationConfig.animalCharacteristics["Медведь"]!!.offspringCount
)