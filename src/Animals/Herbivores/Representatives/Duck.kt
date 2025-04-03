package Animals.Herbivores.Representatives

import Animals.Herbivores.Herbivore
import Simulation.Config.SimulationConfig

class Duck : Herbivore(
    name = "Утка",
    weight = SimulationConfig.animalCharacteristics["Утка"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Утка"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Утка"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Утка"]!!.foodNeeded,
    offspringCount = SimulationConfig.animalCharacteristics["Утка"]!!.offspringCount
)