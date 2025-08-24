

/**
* Represents a spell card in a card game.
*
* A spell is a magical effect that can be cast for a mana cost,
* providing immediate or ongoing effects when played.
*
* @property cardInfo: basic card information (id, name, cost, ability)
* @property damage: direct damage dealt to target (null if not a damage spell)
*/
class Spell internal constructor(
    val cardInfo: CardInfo,
    val damage: Int? = null,
) {
    companion object {
        internal fun create(
            cardInfo: CardInfo,
            damage: Int?,
        ): Spell {
            return Spell(cardInfo, damage)
        }
    }
}
