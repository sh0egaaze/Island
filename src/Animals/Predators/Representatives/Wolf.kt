package Animals.Predators.Representatives

import Animals.Predators.Predator
import Simulation.Config.SimulationConfig

class Wolf : Predator(
    name = "Волк",
    weight = SimulationConfig.animalCharacteristics["Волк"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Волк"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Волк"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Волк"]!!.foodNeeded,
    offspringCount = SimulationConfig.animalCharacteristics["Волк"]!!.offspringCount
)