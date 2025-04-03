package Animals.Herbivores.Representatives

import Animals.Herbivores.Herbivore
import Simulation.Config.SimulationConfig

class Goat : Herbivore(
    name = "Коза",
    weight = SimulationConfig.animalCharacteristics["Коза"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Коза"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Коза"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Коза"]!!.foodNeeded,
    offspringCount = SimulationConfig.animalCharacteristics["Коза"]!!.offspringCount
)