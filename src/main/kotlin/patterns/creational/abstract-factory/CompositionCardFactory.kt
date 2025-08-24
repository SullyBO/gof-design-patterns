

/**
 * Abstract factory for creating cards using composition-based approach.
 *
 * Defines the contract for card creation without imposing inheritance
 * constraints on implementing classes. Allows for flexible composition
 * of different creation strategies.
 *
 * ## Trade-offs (vs inheritance approach):
 * Pros: Adding new card types only requires extending the interface and
 * implementing new creators - existing implementations are unaffected.
 * You can have specialized creators for different contexts without touching old code.
 *
 * Cons: This is tough, it genuinely is just better and delegation is more readable and idiomatic than polymorphic code.
 *
 * ## Kotlin-specific nice things:
 * - Object keyword means no singleton boilerplate
 *
 * ## Note:
 * - Doesn't have to be singleton, but consistency guarantees are nice (more on those in singleton impl.)
 */
object CompositionCardFactoryProvider {
    fun getFactory(cardType: CardType): Any = // Can use enums for strong typing
        when (cardType) {
            CardType.MINION -> MinionCardFactory
            CardType.SPELL -> SpellCardFactory
        }
}

object MinionCardFactory {
    fun createMinion(
        id: Int,
        name: String,
        cost: Int,
        effect: () -> Unit,
        attack: Int,
        health: Int,
    ): Minion {
        val cardInfo = CardInfo.create(id, name, cost, effect)
        return Minion.create(cardInfo, attack, health)
    }
}

object SpellCardFactory {
    fun createSpell(
        id: Int,
        name: String,
        cost: Int,
        effect: () -> Unit,
        damage: Int?,
    ): Spell {
        val cardInfo = CardInfo.create(id, name, cost, effect)
        return Spell.create(cardInfo, damage)
    }
}
