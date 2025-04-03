package Simulation.Config

object SimulationConfig {
    const val ISLAND_WIDTH = 100
    const val ISLAND_HEIGHT = 20
    const val TICK_INTERVAL_MS = 1000L
    const val INITIAL_PLANT_DENSITY = 0.3

    const val INITIAL_WOLF_COUNT = 5
    const val INITIAL_SNAKE_COUNT = 5
    const val INITIAL_FOX_COUNT = 5
    const val INITIAL_BEAR_COUNT = 2
    const val INITIAL_EAGLE_COUNT = 5
    const val INITIAL_HORSE_COUNT = 10
    const val INITIAL_DEER_COUNT = 10
    const val INITIAL_RABBIT_COUNT = 50
    const val INITIAL_MOUSE_COUNT = 100
    const val INITIAL_GOAT_COUNT = 20
    const val INITIAL_SHEEP_COUNT = 20
    const val INITIAL_BOAR_COUNT = 15
    const val INITIAL_BUFFALO_COUNT = 5
    const val INITIAL_DUCK_COUNT = 30
    const val INITIAL_CATERPILLAR_COUNT = 200

    const val MAX_PLANTS_PER_CELL = 200

    var IS_SIMULATION_RUNNING = true

    val animalCharacteristics = mapOf(
        "Волк" to AnimalCharacteristics(50.0, 30, 3.0, 8.0, 1),
        "Удав" to AnimalCharacteristics(15.0, 30, 1.0, 3.0, 1),
        "Лиса" to AnimalCharacteristics(8.0, 30, 2.0, 2.0, 1),
        "Медведь" to AnimalCharacteristics(500.0, 5, 2.0, 80.0, 1),
        "Орел" to AnimalCharacteristics(6.0, 20, 3.0, 1.0, 1),
        "Лошадь" to AnimalCharacteristics(400.0, 20, 4.0, 60.0, 1),
        "Олень" to AnimalCharacteristics(300.0, 20, 4.0, 50.0, 1),
        "Кролик" to AnimalCharacteristics(2.0, 150, 2.0, 0.45, 2),
        "Мышь" to AnimalCharacteristics(0.05, 500, 1.0, 0.01, 3),
        "Коза" to AnimalCharacteristics(60.0, 140, 3.0, 10.0, 1),
        "Овца" to AnimalCharacteristics(70.0, 140, 3.0, 15.0, 1),
        "Кабан" to AnimalCharacteristics(400.0, 50, 2.0, 50.0, 1),
        "Буйвол" to AnimalCharacteristics(700.0, 10, 3.0, 100.0, 1),
        "Утка" to AnimalCharacteristics(1.0, 200, 4.0, 0.15, 2),
        "Гусеница" to AnimalCharacteristics(0.01, 1000, 0.0, 0.0, 10),
        "Растения" to AnimalCharacteristics(1.0, 200, 0.0, 0.0, 0)
    )

    val foodProbabilities = mapOf(
        "Волк" to mapOf("Кролик" to 60, "Мышь" to 80, "Коза" to 60, "Овца" to 70, "Кабан" to 15, "Буйвол" to 10),
        "Удав" to mapOf("Лиса" to 15, "Кролик" to 20, "Мышь" to 40, "Утка" to 10),
        "Лиса" to mapOf("Кролик" to 70, "Мышь" to 90, "Утка" to 60, "Гусеница" to 40),
        "Медведь" to mapOf("Удав" to 80, "Лошадь" to 40, "Олень" to 80, "Кролик" to 80, "Мышь" to 90, "Коза" to 70, "Овца" to 70, "Кабан" to 50, "Буйвол" to 20, "Утка" to 10),
        "Орел" to mapOf("Лиса" to 10, "Кролик" to 90, "Мышь" to 90, "Утка" to 80),
        "Лошадь" to mapOf("Растения" to 100),
        "Олень" to mapOf("Растения" to 100),
        "Кролик" to mapOf("Растения" to 100),
        "Мышь" to mapOf("Гусеница" to 90, "Растения" to 100),
        "Коза" to mapOf("Растения" to 100),
        "Овца" to mapOf("Растения" to 100),
        "Кабан" to mapOf("Мышь" to 50, "Гусеница" to 90, "Растения" to 100),
        "Буйвол" to mapOf("Растения" to 100),
        "Утка" to mapOf("Гусеница" to 90, "Растения" to 100),
        "Гусеница" to mapOf("Растения" to 100)
    )
}

data class AnimalCharacteristics(
    val weight: Double,
    val maxCount: Int,
    val speed: Double,
    val foodNeeded: Double,
    val offspringCount: Int
)