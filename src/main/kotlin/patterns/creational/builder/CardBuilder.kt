/**
 * Builder for separating construction of complex obj from
 * its representation so that the same construction process
 * can create different representations.
 *
 * Defines the interface for interacting with shared functions/properties.
 *
 * ## Trade-offs:
 * Pros: Enables varying internal representation. Isolates code for construction
 * and representation. Provides finer control over construction process.
 * Can enable elegant and beautiful API for object creation (more on that in the demo.)
 *
 * Cons: Memory overhead for holding temporary state. No thread-safety due to mutability.
 * Need to validate object getters (getResult(), printResult()) against incomplete objects.
 *
 * Kotlin-specific nice things:
 * - The syntactic sugar for implementing extension functions is neat.
 *
 * ## Note:
 * - Builders are stateful, problematic to implement as singleton.
 */
interface CardBuilder {
    fun reset()

    fun setName(newName: String): CardBuilder

    fun setCost(newCost: Int): CardBuilder

    fun setRarity(newRarity: Rarity): CardBuilder
}

enum class Rarity {
    UNSET,
    COMMON,
    RARE,
    EPIC,
    LEGENDARY,
}

class ConcreteMinionBuilder : CardBuilder {
    private var name: String = ""
    private var cost: Int = 0
    private var attack: Int = 0
    private var health: Int = 0
    private var rarity: Rarity = Rarity.UNSET

    override fun reset() {
        name = ""
        cost = 0
        attack = 0
        health = 0
        println("Minion card reset!")
    }

    override fun setName(newName: String): ConcreteMinionBuilder {
        name = newName
        println("Minion card name changed to $newName")
        return this
    }

    override fun setCost(newCost: Int): ConcreteMinionBuilder {
        cost = newCost
        println("Minion card cost changed to $newCost")
        return this
    }

    fun getResult(): MinionCard {
        require(name.isNotEmpty()) { "Name must not be empty" }
        require(cost >= 0) { "Cost must be >= 0" }
        require(attack >= 0) { "Attack must be >= 0" }
        require(health > 0) { "Health must be > 0" }

        return MinionCard(
            name = name,
            cost = cost,
            attack = attack,
            health = health,
            rarity = rarity,
        )
    }

    fun printResult() {
        require(name.isNotEmpty()) { "Name must not be empty" }
        require(cost >= 0) { "Cost must be >= 0" }
        require(attack >= 0) { "Attack must be >= 0" }
        require(health > 0) { "Health must be > 0" }

        println("Minion name: $name -- Cost: $cost -- Attack: $attack -- Health: $health")
    }

    fun setAttack(newAttack: Int): ConcreteMinionBuilder {
        attack = newAttack
        println("Minion card attack changed to $newAttack")
        return this
    }

    fun setHealth(newHealth: Int): ConcreteMinionBuilder {
        health = newHealth
        println("Minion card health changed to $newHealth")
        return this
    }

    override fun setRarity(newRarity: Rarity): ConcreteMinionBuilder {
        rarity = newRarity
        println("Minion rarity changed to $newRarity")
        return this
    }
}

class ConcreteSpellBuilder : CardBuilder {
    private var name: String = ""
    private var cost: Int = 0
    private var damage: Int? = null
    private var rarity: Rarity = Rarity.UNSET

    override fun reset() {
        name = ""
        cost = 0
        damage = null
        println("Spell card reset!")
    }

    override fun setName(newName: String): ConcreteSpellBuilder {
        name = newName
        println("Spell card name changed to $newName")
        return this
    }

    override fun setCost(newCost: Int): ConcreteSpellBuilder {
        cost = newCost
        println("Spell card cost changed to $newCost")
        return this
    }

    fun getResult(): SpellCard {
        require(name.isNotEmpty()) { "Name must not be empty" }
        require(cost >= 0) { "Cost must be >= 0" }

        return SpellCard(
            name = name,
            cost = cost,
            damage = damage,
            rarity = rarity,
        )
    }

    fun setDamage(newDamage: Int): ConcreteSpellBuilder {
        damage = newDamage
        println("Spell card damage changed to $newDamage")
        return this
    }

    override fun setRarity(newRarity: Rarity): ConcreteSpellBuilder {
        rarity = newRarity
        println("Spell rarity changed to $newRarity")
        return this
    }
}

class MinionCard(
    val name: String,
    val cost: Int,
    val attack: Int,
    val health: Int,
    val rarity: Rarity,
)

class SpellCard(
    val name: String,
    val cost: Int,
    val damage: Int?,
    val rarity: Rarity,
)
