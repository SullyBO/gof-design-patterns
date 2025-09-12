/**
 * Prototype demonstration
 *
 * Given that prototype is easy-to-use with object creation via
 * composition being a big part of the appeal of this pattern,
 * I decided to spice things up and profile performance of
 * object creation vs shallow copying vs deep copying
 *
 * For most scenarios, these performance overheads are completely unnoticeable.
 * So decisions should be based on domain rules and managing complexity.
 *
 * This was pretty fun to profile tho
 *
 */
fun main() {
    println("=== Prototype Pattern Demo ===\n")

    val registry = PrototypeRegistry()

    val complexGoblin =
        EnemyNPC(
            Position(0, 0, 0),
            EnemyAIState.IDLE,
            Stats(EnemyRank.COMMON, 50, 50, 15, 3, 2),
        )

    registry.addItem(EnemyType.GOBLIN, complexGoblin)

    println("--- Performance Comparison ---")
    println("Testing with 100,000 iterations...\n")

    val startTime1 = System.nanoTime()
    repeat(100_000) {
        EnemyNPC(
            Position(0, 0, 0),
            EnemyAIState.IDLE,
            Stats(EnemyRank.COMMON, 50, 50, 15, 3, 2),
        )
    }
    val createTime = (System.nanoTime() - startTime1) / 1_000_000

    val startTime2 = System.nanoTime()
    repeat(100_000) {
        registry.findByType(EnemyType.GOBLIN)?.deepCopy()
    }
    val deepCloneTime = (System.nanoTime() - startTime2) / 1_000_000

    val startTime3 = System.nanoTime()
    repeat(100_000) {
        registry.findByType(EnemyType.GOBLIN)?.shallowCopy()
    }
    val shallowCloneTime = (System.nanoTime() - startTime3) / 1_000_000

    println("Ranking (fastest to slowest):")
    val results =
        listOf(
            "Shallow Clone" to shallowCloneTime,
            "Deep Clone" to deepCloneTime,
            "Fresh Creation" to createTime,
        ).sortedBy { it.second }

    results.forEachIndexed { index, (name, time) ->
        println("${index + 1}. $name: ${time}ms")
    }
}
