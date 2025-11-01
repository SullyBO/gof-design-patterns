/**
 * Template Method pattern for defining algorithm skeletons
 * 
 * Defines the skeleton of an algo in a base class, deferring some steps to
 * subclasses. The template method calls a series of ops in a fixed order, 
 * where some ops are abstract (must be impl by subclasses) and others are 
 * concrete (have default impl that can be overridden.) This lets subclasses
 * redefine certain steps without changing the algo's overall structure, 
 * promoting code reuse and enforcing a consistent process across variants.
 * 
 * ## Trade-offs:
 * Pros: Eliminates code duplication by extracting common algo structure. 
 * Enforces consistent behavior across subclasses. Easy to extend with new
 * variants without modifying the base algorithm.
 * 
 * Cons: Can lead to rigid class hierarchies. Each variation requires a new
 * subclass (may lead to class proliferation.) Poor testability. Increased 
 * indirection adds to complexity.
 * 
 * ## Kotlin-specific nice things:
 * - Extension functions can replace template methods
 * - Sealed classes for variations if not too many
 * 
 * ## Note:
 * - Hooks (empty open methods) provide optional extension points
 * - Consider functional composition over inheritance for simple cases
 * - When reading the theory this pattern seemed fantastic and essential even.
 * However, after implementing it and working out the practical problems with it, 
 * especially with the poor testability, it feels like I won't be reaching for 
 * all that often.
 */
abstract class CustomCardCreator {
    fun templateMethod() {
        cardIntroSFX()
        cardIntroAnimFX()
        val customCard = createCustomCard()
        println(customCard)
        cardEffectHook()
    }

    // Concrete abstract class operation that can (optionally) be overriden
    protected open fun cardIntroAnimFX() {
        println("Playing default animation...")
    }

    // Primitive operations that must be overridden
    protected abstract fun cardIntroSFX()

    protected abstract fun createCustomCard(): CustomCard

    // User hook for (optional) custom behavior
    protected open fun cardEffectHook() {}
}

class UserCreatedCard : CustomCardCreator() {
    override fun cardIntroAnimFX() {
        println("Playing custom murloc animation...")
    }

    override fun cardIntroSFX() {
        println("Intro: Mrghlrghgl")
    }

    override fun createCustomCard(): CustomCard { 
        return CustomCard(
            id = 1,
            name = "Murgle",
            attack = 100000,
            hp = 1
            )
        }

    override fun cardEffectHook() {
        println("This fishge has the CHARGE keyword")
    }
}

data class CustomCard(
    val id: Int,
    val name: String,
    val attack: Int,
    val hp: Int,
    )
