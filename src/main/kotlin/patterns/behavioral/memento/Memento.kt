/**
 * Memento pattern for capturing and restoring game saves
 *
 * Captures and externalizes an object's internal state w.o violating
 * encapsulation, so the object can be restored to this state later.
 * The Originator creates snapshots of its state, the caretaker stores
 * them without inspecting them, and the originator can restore from
 * any saved snapshots.
 *
 * ## Trade-offs:
 * Pros: Preserves encapsulation boundaries (internals stay private.)
 * Provides rollback/undo/redo. Simplifies Originator by delegating
 * state history management to caretaker. State snapshots are safe and
 * immutable.
 *
 * Cons: Copying deep graphs may be expensive. Memory Overhead from
 * storing multiple snapshots. Caretaker lifecycle management can be
 * tricky because of transparent data it's tied to. Expensive when
 * state is large or frequent.
 *
 * ## Kotlin-specific nice things:
 * - Data classes make immutable snapshots trivial
 * - Sealed classes naturally restrict memento types
 * - 'internal' visibility hides snapshot details from external code
 * - Builtin copy functions (.toList(), .toMap()) simplify copying
 * --They create shallow copies, watch for nested state objects
 * - Smart casts work well with sealed memento hierarchies
 *
 * ## Note:
 * - Consider incremental snapshots for large state (Command pattern)
 * - Works great with Command pattern for undo/redo stacks
 * - Common in games (saves), editors (undo), transactions (rollback)
 * - For simple cases, kotlinx.serialization might be sufficient
 */
interface Memento {
    val timestamp: Long
}

// Sealed class for GameMemento: Stores the state snapshot
sealed class GameMemento(override val timestamp: Long) : Memento {
    internal data class Snapshot(
        val playerHealth: Double,
        val playerPos: String,
        val playerLevel: Int,
        val inventory: List<String>,
        val questProgress: Map<Int, Boolean>,
        val time: Long = System.currentTimeMillis(),
    ) : GameMemento(time)
}

// Originator class: Creates and restores from mementos
class GameState(
    private var playerHealth: Double = 100.0,
    private var playerPos: String = "0.0, 0.0, 0.0",
    private var playerLevel: Int = 1,
    private var inventory: MutableList<String> = mutableListOf(),
    private var questProgress: MutableMap<Int, Boolean> = mutableMapOf(),
) {
    fun takeDamage(damageAmount: Double) {
        playerHealth -= damageAmount
        if (playerHealth < 0) playerHealth = 0.0
    }

    fun heal(healAmount: Double) {
        playerHealth += healAmount
    }

    fun movePlayer(
        x: Double,
        y: Double,
        z: Double,
    ) {
        playerPos = "$x, $y, $z"
    }

    fun addItem(item: String) {
        inventory.add(item)
    }

    fun levelUp() {
        playerLevel++
    }

    fun completeQuest(questId: Int) {
        if (questProgress.containsKey(questId)) {
            questProgress[questId] = true
            println("Quest $questId completed!")
        } else {
            println("Incorrect quest ID: $questId")
        }
    }

    fun addQuest(questId: Int) {
        questProgress[questId] = false
    }

    fun createCheckpoint(): GameMemento {
        return GameMemento.Snapshot(
            playerHealth = playerHealth,
            playerPos = playerPos,
            playerLevel = playerLevel,
            inventory = inventory.toList(),
            questProgress = questProgress.toMap(),
        )
    }

    fun loadFromSave(memento: GameMemento) {
        when (memento) {
            is GameMemento.Snapshot -> {
                playerHealth = memento.playerHealth
                playerPos = memento.playerPos
                playerLevel = memento.playerLevel
                inventory = memento.inventory.toMutableList()
                questProgress = memento.questProgress.toMutableMap()
                println("Game loaded successfully!")
            }
        }
    }

    override fun toString(): String {
        return "GameState(health=$playerHealth, level=$playerLevel, position=$playerPos, items=${inventory.size})"
    }
}

// Caretaker class: Manages memento storage w.o knowing memento contents
class SaveManager {
    private val saveSlots: MutableMap<Int, GameMemento> = mutableMapOf()
    private val autoSaves: MutableList<GameMemento> = mutableListOf()
    private val maxAutoSaves: Int = 5

    fun saveToSlot(
        slotNumber: Int,
        memento: GameMemento,
    ) {
        saveSlots[slotNumber] = memento
        println("Game saved to slot $slotNumber at ${memento.timestamp}")
    }

    fun loadFromSlot(slotNumber: Int): GameMemento? {
        val memento = saveSlots[slotNumber]
        if (memento == null) {
            println("No save found in slot $slotNumber")
        }
        return memento
    }

    fun autoSave(memento: GameMemento) {
        autoSaves.add(memento)
        if (autoSaves.size > maxAutoSaves) {
            autoSaves.removeAt(0)
        }
        println("Auto-save created (${autoSaves.size}/$maxAutoSaves)")
    }

    fun getLatestAutoSave(): GameMemento? {
        return autoSaves.lastOrNull()
    }

    fun listSaves(): List<SaveInfo> {
        return saveSlots.map { (slotNumber, memento) ->
            SaveInfo(slotNumber, memento.timestamp)
        }
    }

    fun deleteSave(slotNumber: Int): Boolean {
        val deleted = saveSlots.remove(slotNumber) != null
        if (deleted) {
            println("Save slot $slotNumber deleted")
        } else {
            println("No save found in slot $slotNumber")
        }
        return deleted
    }

    fun hasSlot(slotNumber: Int): Boolean {
        return saveSlots.containsKey(slotNumber)
    }
}

data class SaveInfo(val slotNumber: Int, val timestamp: Long)
