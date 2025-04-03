package Animals.Herbivores.Representatives

import Animals.Herbivores.Herbivore
import Simulation.Config.SimulationConfig

class Mouse : Herbivore(
    name = "Мышь",
    weight = SimulationConfig.animalCharacteristics["Мышь"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Мышь"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Мышь"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Мышь"]!!.foodNeeded,
    offspringCount = SimulationConfig.animalCharacteristics["Мышь"]!!.offspringCount
)