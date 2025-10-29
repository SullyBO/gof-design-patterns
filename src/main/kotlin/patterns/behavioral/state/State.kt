/**
 * State pattern for managing context-dependent behavior
 *
 * Allows an object to alter its behavior when its internal state changes.
 * The object will appear to change its class. State-specific behavior is
 * encapsulated in separate state classes, and the context delegates to
 * the current state object. States can trigger transitions by changing
 * the context's state.
 *
 * ## Trade-offs:
 * Pros: Eliminates complex conditional logic. Makes state transitions
 * explicit and visible. Each state is isolated and focused. Easy to add
 * new states without modifying existing ones. State-specific data can be
 * encapsulated within state classes. Simplifies host/context class.
 *
 * Cons: Class explosion risk. Increased indirection. May be overkill.
 *
 * ## Kotlin-specific nice things:
 * - Data classes can hold state-specific data cleanly
 * - Object keyword works well for stateless state impl
 * - When expression with sealed states provides exhaustive
 * compiler-enforced checking
 *
 * ## Note:
 * - For simple cases (2-3 states, minimal logic), enums with when expr
 * may be sufficient and more readable
 * - Common in games (AI, combat, animation controllers), UI (screens,
 * dialogs), network (connection states), document editing
 * - Works well with strategy pattern (states as strategies)
 * - Consider state machine libraries for complex state graphs
 */
interface CombatState {
    fun attack(character: CharacterEntity)

    fun takeHit(character: CharacterEntity)

    fun printState()
}

class CharacterEntity(
    var health: Int = 100,
    private var state: CombatState,
) {
    fun attack() = state.attack(this)

    fun takeHit() = state.takeHit(this)

    fun printState() = state.printState()

    fun setState(newState: CombatState) {
        state = newState
    }
}

class StandingState : CombatState {
    override fun attack(character: CharacterEntity) {
        println("Attack!")
        character.setState(AttackingState())
    }

    override fun takeHit(character: CharacterEntity) {
        character.health -= 20
        println("Took some damage, health is: ${character.health}")

        if (character.health <= 0) {
            character.setState(DefeatedState())
        }
    }

    override fun printState() = println("Currently standing")
}

class AttackingState : CombatState {
    override fun attack(character: CharacterEntity) {
        println("Still attacking!")
        character.setState(StandingState())
    }

    override fun takeHit(character: CharacterEntity) {
        character.health -= 30
        println("GUH Got hit mid-attack, health is: ${character.health}")

        if (character.health <= 0) {
            character.setState(DefeatedState())
        }
    }

    override fun printState() = println("Currently attacking")
}

class DefeatedState : CombatState {
    override fun attack(character: CharacterEntity) {
        println("Can't attack when deadge")
    }

    override fun takeHit(character: CharacterEntity) {
        println("already deadge")
    }

    override fun printState() = println("deadge")
}
