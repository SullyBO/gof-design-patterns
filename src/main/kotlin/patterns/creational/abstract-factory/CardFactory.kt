/**
 * Abstract factory (Singleton) for creating card families based on rarity.
 *
 * Defines the interface for creating different types of cards
 * while allowing concrete factories to implement specific creation logic.
 *
 * ## Trade-offs:
 * Pros: Guarantees coordinated families - legendary minions and spells both
 * get legendary treatment. Easy to add new rarity tiers.
 *
 * Cons: Adding new card types (e.g., weapons) requires modifying the
 * interface and ALL concrete factory implementations, even if some rarities
 * don't support the new type yet.
 *
 * ## Kotlin-specific nice things:
 * - Object keyword means no singleton boilerplate
 *
 * ## Note:
 * - Doesn't have to be singleton, but consistency guarantees are nice (more on those in singleton impl.)
 */
interface CardFactory {
    fun createMinion(
        id: Int,
        name: String,
        cost: Int,
        effect: () -> Unit,
        attack: Int,
        health: Int,
    ): Minion

    fun createSpell(
        id: Int,
        name: String,
        cost: Int,
        effect: () -> Unit,
        damage: Int?,
    ): Spell
}

object CardFactoryProvider {
    fun getFactory(cardType: CardType): CardFactory =
        when (cardType) {
            CardType.LEGENDARY -> LegendaryCardFactoryImpl
            CardType.RARE -> RareCardFactoryImpl
        }
}

object LegendaryCardFactoryImpl : CardFactory {
    override fun createMinion(
        id: Int,
        name: String,
        cost: Int,
        effect: () -> Unit,
        attack: Int,
        health: Int,
    ): Minion {
        val cardInfo = CardInfo.create(id, name, cost, effect)
        return LegendaryMinion.create(cardInfo, attack, health)
    }

    override fun createSpell(
        id: Int,
        name: String,
        cost: Int,
        effect: () -> Unit,
        damage: Int?,
    ): Spell {
        val cardInfo = CardInfo.create(id, name, cost, effect)
        return LegendarySpell.create(cardInfo, damage)
    }
}

object RareCardFactoryImpl : CardFactory {
    override fun createMinion(
        id: Int,
        name: String,
        cost: Int,
        effect: () -> Unit,
        attack: Int,
        health: Int,
    ): Minion {
        val cardInfo = CardInfo.create(id, name, cost, effect)
        return RareMinion.create(cardInfo, attack, health)
    }

    override fun createSpell(
        id: Int,
        name: String,
        cost: Int,
        effect: () -> Unit,
        damage: Int?,
    ): Spell {
        val cardInfo = CardInfo.create(id, name, cost, effect)
        return RareSpell.create(cardInfo, damage)
    }
}
