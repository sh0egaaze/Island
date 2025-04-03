package Animals.Herbivores.Representatives

import Animals.Herbivores.Herbivore
import Simulation.Config.SimulationConfig

class Deer : Herbivore(
    name = "Олень",
    weight = SimulationConfig.animalCharacteristics["Олень"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Олень"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Олень"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Олень"]!!.foodNeeded,
    offspringCount = SimulationConfig.animalCharacteristics["Олень"]!!.offspringCount
)