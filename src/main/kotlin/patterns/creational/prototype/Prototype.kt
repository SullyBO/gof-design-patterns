/**
 * Prototype pattern for cloning configured objects instead of reconstructing.
 *
 * Creates new objects by cloning existing prototype instances rather than
 * instantiating from scratch. Useful when object creation is expensive or
 * when you need copies of complex, pre-configured object compositions.
 *
 * ## Trade-offs:
 * Pros: Avoids expensive initialization of complex objects. Reduces coupling
 * between client and concrete classes. Handles composition elegantly without
 * needing to know internal representation/structure.
 *
 * Cons: Deep copying can be tricky to implement correctly. Every prototype
 * class needs clone methods. Need to make sure when to deep/shallow copy.
 *
 * ## Kotlin-specific nice things:
 * - Data classes provide .copy() function for shallow copying
 * - Sealed classes work well for prototype registries with type safety
 * - Extension functions can add cloning behavior to existing classes
 *
 * ## Note:
 * - Shallow vs deep copying matters a ton, choose the right one for the right
 *   case. (i.e. opt for deep copying if independent object state is desired
 *   and shallow copying if sharing immutable references is possibly safely)
 *
 * - please don't expose both (shallow/deep copying) to users and make them decide
 */
interface Prototype {
    fun shallowCopy(): Prototype

    fun deepCopy(): Prototype
    // These are both included for pedagogic reasons,
    // a good API only includes the correct one.
}

class PrototypeRegistry {
    val items: MutableMap<EnemyType, Prototype> = mutableMapOf()

    fun addItem(
        type: EnemyType,
        prototype: Prototype,
    ) {
        items[type] = prototype
    }

    fun findByType(type: EnemyType): Prototype? = items[type]
}

/**
 * Data classes already come with copy() builtin for shallow copies
 * but this helps showcase what's actually happening under the hood better,
 * and it's really not that much more code for simple examples like this one
 */
data class EnemyNPC(
    private var position: Position,
    private var aiState: EnemyAIState,
    private var stats: Stats,
) : Prototype {
    override fun shallowCopy(): Prototype {
        return EnemyNPC(position, aiState, stats)
    }

    // Note: aiState is an immutable enum, copying it is perfectly safe
    override fun deepCopy(): Prototype {
        return EnemyNPC(
            Position(position.x, position.y, position.z),
            aiState,
            Stats(
                stats.rank,
                stats.maxHP,
                stats.currentHP,
                stats.attackDamage,
                stats.attackRange,
                stats.armorValue,
            ),
        )
    }

    /**
     * Interestingly, .copy() does not copy more than one level deep
     * meaning that solutions like the deepCopy() function are necessary
     * for deep copying complex objects with mutable objects nested in objects
     */
    fun idiomaticDeepCopy(): Prototype {
        return EnemyNPC(
            position.copy(),
            aiState,
            stats.copy(),
        )
    }
}

data class Stats(
    val rank: EnemyRank,
    val maxHP: Int,
    var currentHP: Int,
    var attackDamage: Int,
    var attackRange: Int,
    var armorValue: Int,
)

data class Position(
    var x: Int,
    var y: Int,
    var z: Int,
)

enum class EnemyRank {
    BOSS,
    MINIBOSS,
    ELITE,
    COMMON,
}

enum class EnemyAIState {
    IDLE,
    ATTACKING,
    WALKING,
    RUNNING,
}

enum class EnemyType {
    MURLOC,
    GOBLIN,
    GNOME,
    UNDEAD,
}
