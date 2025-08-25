/**
 * Represents a spell card in a card game.
 *
 * A spell is a magical effect that can be cast for immediate impact.
 * Different rarity implementations provide their own pack opening
 * sound effects and card information access.
 *
 * Functions:
 * - playPackSfx(): Plays rarity-specific sound when revealed from pack (for demo purposes, this is a println)
 * - getCardName(): Returns the spell's display name
 */
interface Spell {
    fun playPackSfx()

    fun getCardName(): String
}

class RareSpell internal constructor(
    private val cardInfo: CardInfo,
    private val damage: Int?,
) : Spell {
    override fun playPackSfx() {
        println("raaare spell!")
    }

    override fun getCardName(): String {
        return cardInfo.name
    }

    companion object {
        fun create(
            cardInfo: CardInfo,
            damage: Int?,
        ): RareSpell = RareSpell(cardInfo, damage)
    }
}

class LegendarySpell internal constructor(
    private val cardInfo: CardInfo,
    private val damage: Int?,
) : Spell {
    override fun playPackSfx() {
        println("LEGENDARY SPELL!!")
    }

    override fun getCardName(): String {
        return cardInfo.name
    }

    companion object {
        fun create(
            cardInfo: CardInfo,
            damage: Int?,
        ): LegendarySpell = LegendarySpell(cardInfo, damage)
    }
}
