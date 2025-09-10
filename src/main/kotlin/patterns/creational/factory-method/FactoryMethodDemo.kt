class FactoryMethodDemo {
    fun runDemo() {
        println("=== Factory Method Pattern Demo ===\n")

        val swordFactory = SwordFactory()
        val shieldFactory = ShieldFactory()

        val sword = swordFactory.createWeapon()
        val shield = shieldFactory.createWeapon()
        println("1. Weapons created using concrete factories")

        println("\n2. Using weapon inspection function:")
        println(sword.inspect(sword))
        println(shield.inspect(shield))

        println("\n3. Polymorphic usage - factories treated uniformly:")
        val factories = listOf(swordFactory, shieldFactory)

        factories.forEach { factory ->
            val weapon = factory.createWeapon()
            println("Factory created weapon: ${weapon.inspect(weapon)}")
        }

        println("\n4. Simulating a game scenario:")
        val playerChoice = "sword"
        val chosenFactory = getWeaponFactory(playerChoice)
        val playerWeapon = chosenFactory.createWeapon()

        println("Player chose: $playerChoice")
        println("Equipped weapon: ${playerWeapon.inspect(playerWeapon)}")
    }

    // Helper functions could be used for factory selection that enforces consistent domain rules
    private fun getWeaponFactory(weaponType: String): WeaponFactory {
        return when (weaponType.lowercase()) {
            "sword" -> SwordFactory()
            "shield" -> ShieldFactory()
            else -> SwordFactory()
        }
    }
}

fun main() {
    FactoryMethodDemo().runDemo()
}
