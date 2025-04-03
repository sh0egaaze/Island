package Animals.Herbivores.Representatives

import Animals.Herbivores.Herbivore
import Simulation.Config.SimulationConfig

class Horse : Herbivore(
    name = "Лошадь",
    weight = SimulationConfig.animalCharacteristics["Лошадь"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Лошадь"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Лошадь"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Лошадь"]!!.foodNeeded,
    offspringCount = SimulationConfig.animalCharacteristics["Лошадь"]!!.offspringCount
)