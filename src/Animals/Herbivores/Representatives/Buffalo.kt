package Animals.Herbivores.Representatives

import Animals.Herbivores.Herbivore
import Simulation.Config.SimulationConfig

class Buffalo : Herbivore(
    name = "Буйвол",
    weight = SimulationConfig.animalCharacteristics["Буйвол"]!!.weight,
    maxCount = SimulationConfig.animalCharacteristics["Буйвол"]!!.maxCount,
    speed = SimulationConfig.animalCharacteristics["Буйвол"]!!.speed,
    foodNeeded = SimulationConfig.animalCharacteristics["Буйвол"]!!.foodNeeded,
    offspringCount = SimulationConfig.animalCharacteristics["Буйвол"]!!.offspringCount
)