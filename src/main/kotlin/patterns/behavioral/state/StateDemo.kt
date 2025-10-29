fun main() {
    val hero = CharacterEntity(health = 100, state = StandingState())

    println("=== Combat State Demo ===\n")

    hero.printState()
    println()

    hero.attack()
    hero.printState()
    println()

    hero.attack()
    hero.printState()
    println()

    hero.takeHit()
    hero.printState()
    println()

    hero.attack()
    hero.printState()
    hero.takeHit()
    hero.printState()
    println()

    hero.takeHit()
    hero.takeHit()
    hero.printState()
    println()

    hero.attack()
    hero.takeHit()

    println("\n=== Demo Complete ===")
}
