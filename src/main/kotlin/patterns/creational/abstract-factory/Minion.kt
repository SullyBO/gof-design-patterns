/**
 * Represents a minion card in a card game.
 *
 * A minion is a creature that can be played onto the battlefield.
 * Different rarity implementations provide their own pack opening
 * sound effects and card information access.
 *
 * Functions:
 * - playPackSfx(): Plays rarity-specific sound when revealed from pack (for demo purposes, this is a println)
 * - getCardName(): Returns the minion's display name
 */
interface Minion {
    fun playPackSfx()

    fun getCardName(): String
}

class RareMinion internal constructor(
    private val cardInfo: CardInfo,
    private val attack: Int,
    private val health: Int,
) : Minion {
    override fun playPackSfx() {
        println("raaare minion!")
    }

    override fun getCardName(): String {
        return cardInfo.name
    }

    companion object {
        fun create(
            cardInfo: CardInfo,
            attack: Int,
            health: Int,
        ): RareMinion = RareMinion(cardInfo, attack, health)
    }
}

class LegendaryMinion internal constructor(
    private val cardInfo: CardInfo,
    private val attack: Int,
    private val health: Int,
) : Minion {
    override fun playPackSfx() {
        println("LEGENDARY MINION!!")
    }

    override fun getCardName(): String {
        return cardInfo.name
    }

    companion object {
        fun create(
            cardInfo: CardInfo,
            attack: Int,
            health: Int,
        ): LegendaryMinion = LegendaryMinion(cardInfo, attack, health)
    }
}
