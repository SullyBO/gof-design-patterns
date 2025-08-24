

/**
 * Abstract factory for creating cards with singleton concrete implementations.
 *
 * Defines the interface for creating different types of cards
 * while allowing concrete factories to implement specific creation logic.
 *
 * ## Trade-offs (vs composition approach):
 * Pros: Enforced contract through abstract methods, single interface to maintain.
 *
 * Cons: As stated in the book, adding new card types requires modifying this abstract class
 * and ALL concrete factory impl, even if they don't need the new card type.
 * Can get really painful real quick.
 *
 * ## Kotlin-specific nice things:
 * - Object keyword means no singleton boilerplate
 *
 * ## Note:
 * - Doesn't have to be singleton, but consistency guarantees are nice (more on those in singleton impl.)
 */
abstract class InheritanceCardFactory {
        abstract fun createMinion(
        id: Int,
        name: String,
        cost: Int,
        effect: () -> Unit,
        attack: Int,
        health: Int,
    ): Minion

    abstract fun createSpell(
        id: Int,
        name: String,
        cost: Int,
        effect: () -> Unit,
        damage: Int?,
    ): Spell
}

object InheritanceCardFactoryProvider {
    fun getFactory(cardType: CardType): InheritanceCardFactory =
        when (cardType) {
            CardType.MINION -> MinionCardFactoryImpl
            CardType.SPELL -> SpellCardFactoryImpl
        }
}

object MinionCardFactoryImpl : InheritanceCardFactory() {
    override fun createMinion(
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

    override fun createSpell(
        id: Int,
        name: String,
        cost: Int,
        effect: () -> Unit,
        damage: Int?,
    ): Spell {
        throw UnsupportedOperationException("This factory only creates minions")
    }
}

object SpellCardFactoryImpl : InheritanceCardFactory() {
    override fun createMinion(
        id: Int,
        name: String,
        cost: Int,
        effect: () -> Unit,
        attack: Int,
        health: Int,
    ): Minion {
        throw UnsupportedOperationException("This factory only creates spells")
    }

    override fun createSpell(
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
