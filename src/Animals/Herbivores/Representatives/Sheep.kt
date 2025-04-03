package Animals.Herbivores.Representatives

import Animals.Herbivores.Herbivore
import Simulation.Config.SimulationConfig

class Sheep : Herbivore(
    name = "Овца",
    weight = SimulationConfig.animalCharacteristics["Овца"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Овца"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Овца"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Овца"]!!.foodNeeded,
    offspringCount = SimulationConfig.animalCharacteristics["Овца"]!!.offspringCount
)