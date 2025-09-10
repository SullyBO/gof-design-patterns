/**
 * Factory Method for creating weapons with delegated creation logic.
 *
 * Defines an interface for creating objects, but lets subclasses decide
 * which class to instantiate. Moves instantiation logic from client code
 * to creator class(es.)
 *
 * ## Trade-offs:
 * Pros: Easy to add new types without modifying existing code.
 * Each creator handles one responsibility maintaining good separation.
 * Simpler to implement and use than Abstract Factory.
 *
 * Cons: Can lead to class proliferation. Can still be overkill if all
 * you need is abstracting object creation.
 *
 * ## Kotlin-specific nice things:
 * - Sealed classes work great for restricting creator hierarchies
 * - Data classes reduce boilerplate for simple product types
 * - Extension functions can add factory methods to existing types
 *
 * ## Note:
 * - Consider using simple factory functions instead if creation logic
 *   doesn't need to vary by subclass or runtime conditions (not GoF.)
 *
 * =======================================================================
 *
 * Product (Weapon) interface for user code to interact with and concrete products to inherit
 */
interface Weapon {
    fun getType(): String

    fun getAttackDamage(): Int

    fun getHealth(): Int

    fun inspect(weapon: Weapon): String
}

/**
 * Concrete Product A
 */
class Sword : Weapon {
    var durability: Int = 500
    var attack: Int = 500

    override fun getType(): String {
        return "Sword"
    }

    override fun getAttackDamage(): Int {
        return attack
    }

    override fun getHealth(): Int {
        return durability
    }

    override fun inspect(weapon: Weapon): String {
        return "Type: Sword - Stab Damage: ${weapon.getAttackDamage()}, Durability: ${weapon.getHealth()}"
    }
}

/**
 * Concrete Product B
 */
class Shield : Weapon {
    var durability: Int = 1000
    var attack: Int = 100

    override fun getType(): String {
        return "Shield"
    }

    override fun getAttackDamage(): Int {
        return attack
    }

    override fun getHealth(): Int {
        return durability
    }

    override fun inspect(weapon: Weapon): String {
        return "Type: Shield - Shield Bash Damage: ${weapon.getAttackDamage()}, Durability: ${weapon.getHealth()}"
    }
}

/**
 * Abstract Creator/Factory class for concrete creators/factories to inherit
 */
abstract class WeaponFactory {
    abstract fun createWeapon(): Weapon
}

/**
 * Concrete Factory A
 */
class SwordFactory : WeaponFactory() {
    override fun createWeapon(): Weapon {
        return Sword()
    }
}

/** Concrete Factory B */
class ShieldFactory : WeaponFactory() {
    override fun createWeapon(): Weapon {
        return Shield()
    }
}
