/**
 * Deviated from textbook implementation to expose a fluent interface.
 */
class Director<T : CardBuilder>(
    private var builder: T,
) {
    /**
     * Pedagogically included, I'd prefer to just create new directors than reuse.
     */
    fun changeBuilder(newBuilder: T) {
        builder = newBuilder
    }

    fun constructCommonCard(): T {
        builder.reset()
        builder.setRarity(Rarity.COMMON)
        return builder
    }

    fun constructRareCard(): T {
        builder.reset()
        builder.setRarity(Rarity.RARE)
        return builder
    }

    fun constructEpicCard(): T {
        builder.reset()
        builder.setRarity(Rarity.EPIC)
        return builder
    }

    fun constructLegendaryCard(): T {
        builder.reset()
        builder.setRarity(Rarity.LEGENDARY)
        return builder
    }
}
