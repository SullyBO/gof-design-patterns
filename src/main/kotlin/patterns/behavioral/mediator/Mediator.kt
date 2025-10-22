/**
 * Mediator pattern for managing complex object interactrions through a hub
 *
 * Defines an object that encapsulates how a set of objects communicate.
 * Promotes looser coupling by keeping objects from referring to each other
 * explicitly. All comms flow through the mediator, which coordinates behavior
 * and enforces interaction rules.
 *
 * ## Trade-offs:
 * Pros: Reduces coupling between components (they only know the mediator.)
 * Decouples colleagues. Simplifies object protocols. Abstracts how objects
 * cooperate. Centralizing interaction simplifies many-to-many relationships
 * to one-to-many relationships.
 *
 * Cons: Centralizing control increases complexity. Risk of creating a "God
 * Object" with mediators. Can be harder to trace.
 *
 * ## Kotlin-specific nice things:
 * - Sealed classes work great for defining message/event types
 * - Extension functions can add mediator behavior without coupling
 * - Coroutines and Flow provide natural async mediation patterns
 * - Lambda callbacks make event registration cleaner
 *
 * ## Note:
 * - Watch for mediator getting bloated, splitting it in domain terms is ideal
 * - Modern reactive patterns (Flow, StateFlow) often replace mediators
 * - Similar to Observer but mediator coordinates bidirectional comms
 * - Common in UI frameworks (view controllers) and chat systems
 */
interface ChatMediator {
    fun sendMessage(
        message: String,
        sender: Chatter,
    )

    fun addUser(user: Chatter)
}

interface Chatter {
    val name: String

    fun receive(
        message: String,
        fromUser: String,
    )
}

class ChatUser(
    override val name: String,
    private val mediator: ChatMediator,
    private val onReceive: (String, String) -> Unit = { msg, from ->
        println("[$name receives from $from]: $msg")
    },
) : Chatter {
    fun send(message: String) {
        println("$name sends: $message")
        mediator.sendMessage(message, this)
    }

    override fun receive(
        message: String,
        fromUser: String,
    ) {
        onReceive(message, fromUser)
    }
}

class ChatRoom : ChatMediator {
    private val users = mutableListOf<Chatter>()

    override fun addUser(user: Chatter) {
        users.add(user)
        println("${user.name} joined the chat")
    }

    override fun sendMessage(
        message: String,
        sender: Chatter,
    ) {
        users
            .filter { it != sender }
            .forEach { it.receive(message, sender.name) }
    }
}
