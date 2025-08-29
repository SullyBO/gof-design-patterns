/**
 * Deviated from textbook implementation to expose a fluent interface.
 */
class Director(
    private var builder: CardBuilder
    ) {
    
    /**
     * Pedagogically included, I'd prefer to just create new directors than reuse.
     */
    fun changeBuilder(newBuilder: CardBuilder) {
        builder = newBuilder
    }
    
    @Suppress("UNCHECKED_CAST")
    fun <T : CardBuilder> constructCommonCard(): T {
        builder.reset()
        builder.setRarity(Rarity.COMMON)
        return builder as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : CardBuilder> constructRareCard(): T {
        builder.reset()
        builder.setRarity(Rarity.RARE)
        return builder as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : CardBuilder> constructEpicCard(): T {
        builder.reset()
        builder.setRarity(Rarity.EPIC)
        return builder as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : CardBuilder> constructLegendaryCard(): T {
        builder.reset()
        builder.setRarity(Rarity.LEGENDARY)
        return builder as T
    }
}
