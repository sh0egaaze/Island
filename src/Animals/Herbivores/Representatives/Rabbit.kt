package Animals.Herbivores.Representatives

import Animals.Herbivores.Herbivore
import Simulation.Config.SimulationConfig

class Rabbit : Herbivore(
    name = "Кролик",
    weight = SimulationConfig.animalCharacteristics["Кролик"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Кролик"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Кролик"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Кролик"]!!.foodNeeded,
    offspringCount = SimulationConfig.animalCharacteristics["Кролик"]!!.offspringCount
)