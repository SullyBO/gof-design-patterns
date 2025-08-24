@file:Suppress("MagicNumber")

import CardType
import InheritanceCardFactory
import CompositionCardFactoryProvider
import MinionCardFactory
import SpellCardFactory
import InheritanceCardFactoryProvider
fun main() {
    // Shared card constants
    val goblinId = 1
    val goblinName = "Mad Bomber"
    val goblinCost = 2
    val goblinEffect = { println("Battlecry: Deal 3 damage randomly split between all other characters.") }
    val goblinAttack = 3
    val goblinHealth = 2
    
    val fireballId = 2
    val fireballName = "Fireball"
    val fireballCost = 4
    val fireballEffect = { println("Deal damage to target!") }
    val fireballDamage = 6
    
    demonstrateInheritanceApproach(
        goblinId, goblinName, goblinCost, goblinEffect, goblinAttack, goblinHealth,
        fireballId, fireballName, fireballCost, fireballEffect, fireballDamage
    )
    
    println()
    
    demonstrateCompositionApproach(
        goblinId, goblinName, goblinCost, goblinEffect, goblinAttack, goblinHealth,
        fireballId, fireballName, fireballCost, fireballEffect, fireballDamage
    )
}

@Suppress("LongParameterList")
fun demonstrateInheritanceApproach(
    goblinId: Int, goblinName: String, goblinCost: Int, goblinEffect: () -> Unit, goblinAttack: Int, goblinHealth: Int,
    fireballId: Int, fireballName: String, fireballCost: Int, fireballEffect: () -> Unit, fireballDamage: Int
) {
    println("=== Inheritance-based Factory Demo ===")
    
    val minionFactory = InheritanceCardFactoryProvider.getFactory(CardType.MINION)
    val minion = minionFactory.createMinion(goblinId, goblinName, goblinCost, goblinEffect, goblinAttack, goblinHealth)
    
    val spellFactory = InheritanceCardFactoryProvider.getFactory(CardType.SPELL)
    val spell = spellFactory.createSpell(fireballId, fireballName, fireballCost, fireballEffect, fireballDamage)
    
    println("Created minion: ${minion.cardInfo.name} (${minion.attack}/${minion.health})")
    println("Created spell: ${spell.cardInfo.name} (${spell.damage} damage)")
}

fun demonstrateCompositionApproach(
    goblinId: Int, goblinName: String, goblinCost: Int, goblinEffect: () -> Unit, goblinAttack: Int, goblinHealth: Int,
    fireballId: Int, fireballName: String, fireballCost: Int, fireballEffect: () -> Unit, fireballDamage: Int
) {
    println("=== Composition-based Factory Demo ===")
    
    val minionFactory = CompositionCardFactoryProvider.getFactory(CardType.MINION) as MinionCardFactory
    val minion = minionFactory.createMinion(goblinId, goblinName, goblinCost, goblinEffect, goblinAttack, goblinHealth)
    
    val spellFactory = CompositionCardFactoryProvider.getFactory(CardType.SPELL) as SpellCardFactory
    val spell = spellFactory.createSpell(fireballId, fireballName, fireballCost, fireballEffect, fireballDamage)
    
    println("Created minion: ${minion.cardInfo.name} (${minion.attack}/${minion.health})")
    println("Created spell: ${spell.cardInfo.name} (${spell.damage} damage)")

    minion.cardInfo.ability()
    spell.cardInfo.ability()
}