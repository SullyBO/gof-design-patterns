fun main() {
    demonstrateAbstractFactory()
}

@Suppress("LongParameterList")
fun demonstrateAbstractFactory() {
    println("=== Abstract Factory Demo ===")

    // Card constants
    val goblinId = 1
    val goblinName = "Mad Bomber"
    val goblinCost = 2
    val goblinEffect = { println("Battlecry: Deal 3 damage randomly split between all other characters.") }
    val goblinAttack = 3
    val goblinHealth = 2

    val fireballId = 2
    val fireballName = "Fireball"
    val fireballCost = 4
    val fireballEffect = { println("Deal damage to target!") }
    val fireballDamage = 6

    val legendaryFactory = CardFactoryProvider.getFactory(CardType.LEGENDARY)
    val legendaryMinion =
        legendaryFactory.createMinion(
            goblinId,
            goblinName,
            goblinCost,
            goblinEffect,
            goblinAttack,
            goblinHealth,
        )
    val legendarySpell =
        legendaryFactory.createSpell(
            fireballId,
            fireballName,
            fireballCost,
            fireballEffect,
            fireballDamage,
        )

    println("Created legendary minion: ${legendaryMinion.getCardName()}")
    legendaryMinion.playPackSfx()
    println("Created legendary spell: ${legendarySpell.getCardName()}")
    legendarySpell.playPackSfx()

    val rareFactory = CardFactoryProvider.getFactory(CardType.RARE)
    val rareMinion =
        rareFactory.createMinion(
            goblinId,
            goblinName,
            goblinCost,
            goblinEffect,
            goblinAttack,
            goblinHealth,
        )
    val rareSpell =
        rareFactory.createSpell(
            fireballId,
            fireballName,
            fireballCost,
            fireballEffect,
            fireballDamage,
        )

    println("Created rare minion: ${rareMinion.getCardName()}")
    rareMinion.playPackSfx()
    println("Created rare spell: ${rareSpell.getCardName()}")
    rareSpell.playPackSfx()
}
