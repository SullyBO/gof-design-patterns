

/**
* Represents a minion card in a card game.
*
* A minion is a creature that can be played onto the battlefield with
* specific combat stats and a mana cost to summon.
*
* @property cardInfo: basic card information (id, name, cost, ability)
* @property attack: damage dealt in combat
* @property health: points before the minion is destroyed
*/
class Minion internal constructor(
    val cardInfo: CardInfo,
    val attack: Int,
    val health: Int,
) {
    companion object {
        internal fun create(
            cardInfo: CardInfo,
            attack: Int,
            health: Int,
        ): Minion {
            return Minion(cardInfo, attack, health)
        }
    }
}
