/**
 * Command Pattern for custom key mapping in a video game
 *
 * Command encapsulates a request as an object, allowing you to
 * parameterize clients with different requests, queue, logging,
 * and support undo/redo functionality.
 *
 * ## Trade-offs:
 * Pros: Decouples invoker from receiver. Easy to add new commands
 * as there's no need to change existing code. Enables undo/redo,
 * queuing. Supports logging and remote execution.
 *
 * Cons: Can increase code complexity with many small command classes.
 *
 * ## Kotlin-specific nice things:
 * - Sealed classes work well for representing different command types
 * - Lambda expressions can replace simple command classes at the cost
 * of architectural consistency
 * - Data classes make command params easy to manage
 *
 * ## Note:
 * - Keep commands focused at the same level of abstraction
 * - Consider using function types for simple commands
 * - Perfect fit for customizable controls, undo systems, and action logging
 * - This pattern is identical to how usecases are implemented in MVVM + Clean arch
 * - Combined with composite pattern, macrocommands can get orchestrated
 */
interface Command {
    fun execute()
    // fun undo()/redo() would go here but they're not relevant for this example
}

// The Invoker class
class InputHandler constructor(val keyBindings: MutableMap<String, Command>) {
    fun bindKey(
        key: String,
        command: Command,
    ) {
        keyBindings[key] = command
    }

    fun handleInput(key: String) {
        val command = keyBindings[key]
        command?.execute()
    }
}

class PlayerReceiver {
    fun attack() = println("Attacked")

    fun moveForward() = println("Moved one step forward")

    fun moveBackward() = println("Moved one step backward")

    fun moveRightward() = println("Moved one step rightward")

    fun moveLeftward() = println("Moved one step leftward")

    fun useUltimate() {
        println("THIS IS TO GO FURTHER BEYOND AAAAAAAAAAA")
    }
}

class NullCommand : Command {
    override fun execute() { /* default for unused inputs */ }
}

class AttackCommand constructor(val player: PlayerReceiver) : Command {
    override fun execute() = player.attack()
}

class UltimateCommand constructor(val player: PlayerReceiver) : Command {
    override fun execute() = player.useUltimate()
}

class MoveForwardCommand constructor(val player: PlayerReceiver) : Command {
    override fun execute() = player.moveForward()
}

class MoveBackwardCommand constructor(val player: PlayerReceiver) : Command {
    override fun execute() = player.moveBackward()
}

class MoveRightwardCommand constructor(val player: PlayerReceiver) : Command {
    override fun execute() = player.moveRightward()
}

class MoveLeftwardCommand constructor(val player: PlayerReceiver) : Command {
    override fun execute() = player.moveLeftward()
}
