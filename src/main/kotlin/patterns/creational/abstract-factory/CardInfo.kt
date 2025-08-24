/**
* Represents basic card information shared by all card types.
*
* Contains the fundamental properties that every card in the game
* possesses regardless of its specific type or mechanics.
*
* @property id: unique identifier for this card type
* @property name: display name of the card
* @property cost: mana required to play this card
* @property ability: basic card effect executed when played (empty by default)
*/
class CardInfo internal constructor(
    val id: Int,
    val name: String,
    val cost: Int,
    val ability: () -> Unit = {},
) {
    companion object {
        internal fun create(
            id: Int,
            name: String,
            cost: Int,
            ability: () -> Unit = {},
        ): CardInfo {
            return CardInfo(id, name, cost, ability)
        }
    }
}
