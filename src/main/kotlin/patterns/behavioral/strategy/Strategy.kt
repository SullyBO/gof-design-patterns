import kotlin.math.sqrt

/**
 * Strategy pattern for encapsulating interchangeable algorithms
 *
 * Defines a family of algorithms, encapsulate each one, and make
 * them interchangeable. Strategy lets the algorithms vary indie
 * from clients that use it. The context delegates algo execution
 * to a strategy object, which can be swapped at runtime.
 *
 * ## Trade-offs:
 * Pros: Eliminates conditional statements for algo selection.
 * Makes algos interchangeable and reusable. Easy to add new
 * strategies w.o modifying context. Separates algo impl from
 * usage. Enables runtime behavior changes.
 *
 * Cons: Clients must understand different strategies and choose
 * appropriately. Increases number of classes. Communication
 * overhead between context and strategy. Can be overkill.
 *
 * ## Kotlin-specific nice things:
 * - Extension functions can sometimes eliminate need for strategies
 * - Object keyword for stateless strategy impl
 *
 * ## Note:
 * - For trivial cases, a simple function param is good enough
 * - Common in games (AI behavior, pathfinding, combat sys),
 * sorting algos, validation rules, compression/encryption.
 * - State: WHEN behavior changes based on internal state
 * - Strategy: HOW to do something
 * - Can combine with Factory pattern for strategy selection
 */
interface AiPathingAlgorithm {
    fun execute(
        currentPos: Pos,
        target: Pos,
    )
}

data class Pos(val x: Double, val y: Double, val z: Double) {
    fun distanceTo(other: Pos): Double {
        val dx = other.x - x
        val dy = other.y - y
        val dz = other.z - z
        return sqrt(dx * dx + dy * dy + dz * dz)
    }
}

class AiEntity(
    var pos: Pos,
    private var strategy: AiPathingAlgorithm,
) {
    fun setStrategy(newStrategy: AiPathingAlgorithm) {
        strategy = newStrategy
    }

    fun move(target: Pos) {
        strategy.execute(pos, target)
    }
}

class Patrol : AiPathingAlgorithm {
    override fun execute(
        currentPos: Pos,
        target: Pos,
    ) {
        println("*whistle* *whistle* patrolling at (${currentPos.x}, ${currentPos.y}, ${currentPos.z})")
    }
}

class Chasing : AiPathingAlgorithm {
    override fun execute(
        currentPos: Pos,
        target: Pos,
    ) {
        val distance = currentPos.distanceTo(target)
        println("Chasing target! Distance to target: $distance")
    }
}
