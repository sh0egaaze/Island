package Animals.Herbivores.Representatives

import Animals.Herbivores.Herbivore
import Simulation.Config.SimulationConfig

class Caterpillar : Herbivore(
    name = "Гусеница",
    weight = SimulationConfig.animalCharacteristics["Гусеница"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Гусеница"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Гусеница"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Гусеница"]!!.foodNeeded,
    offspringCount = SimulationConfig.animalCharacteristics["Гусеница"]!!.offspringCount
)