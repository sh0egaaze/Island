package Island.Entities

import Simulation.Config.SimulationConfig

interface Eatable {
    fun beEaten(): Boolean
    val weight: Double
}

abstract class IslandElement(open var weight: Double)

class Plant(weight: Double = SimulationConfig.animalCharacteristics["Растения"]!!.weight) : IslandElement(weight),
    Eatable {
    override fun beEaten(): Boolean {
        return true
    }
}
