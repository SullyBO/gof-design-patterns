/**
 * Observer pattern for pub-sub notification system
 *
 * Defines a one-to-many dependency between objects so that when
 * one obj (subject/publisher) changes state, all its dependents
 * (observers/subscribers) are notified & updated automatically.
 * The publisher maintains a list of subs and broadcasts changes
 * to them without knowing their concrete types, promoting loose
 * coupling between them.
 *
 * ## Trade-offs:
 * Pros: Loose coupling between pub and subs. Supports broadcast
 * communication. Easy to add new subs without modifying pub.
 *
 * Cons: Subscribers are notified in an unpredictable order. Can
 * cause memory leaks if subscribers aren't properly unsubbed.
 * Unexpected cascading updates when subs trigger more notifs.
 * Can make sys behavior harder to track and debug.
 *
 * ## Kotlin-specific nice things:
 * - Property delegation syntax (by Delegates.observable) for auto-notifying
 * - Coroutines and Flow provide structured reactive alternatives
 *
 * ## Note:
 * - Consider weak references for subscribers to prevent memory leaks
 * - For complex event streams, prefer Kotlin Flow or reactive libs
 * - Be mindful of update storms in highly connected sub networks
 * - Thread safety is critical in concurrent contexts
 */
interface Subscriber {
    var state: SubscriberState

    fun update(message: String)

    fun printState()
}

class Publisher {
    private val subscribers: MutableList<Subscriber> = mutableListOf()

    fun subscribe(subscriber: Subscriber) {
        subscribers.add(subscriber)
        subscriber.state = SubscriberState.ACTIVE
        subscriber.printState()
    }

    fun unsubscribe(subscriber: Subscriber) {
        if (subscribers.contains(subscriber)) {
            subscribers.remove(subscriber)
            subscriber.state = SubscriberState.IDLE
            subscriber.printState()
        }
    }

    fun notifySubs(message: String) {
        subscribers.forEach { observer ->
            observer.state = SubscriberState.PROCESSING
            observer.printState()
            observer.update(message)
        }
    }
}

class PublicSubscriber(
    private val name: String,
    override var state: SubscriberState = SubscriberState.IDLE,
) : Subscriber {
    override fun update(message: String) {
        println("$name received: $message")
        state = SubscriberState.COMPLETE
        printState()
    }

    override fun printState() {
        println("Public Subscriber with name: $name changed state to $state")
    }
}

class AnonymousSubscriber(
    override var state: SubscriberState = SubscriberState.IDLE,
) : Subscriber {
    override fun update(message: String) {
        println("Anonymous subscriber received: $message")
        state = SubscriberState.COMPLETE
        printState()
    }

    override fun printState() {
        println("Anonymous subscriber changed state to $state")
    }
}

enum class SubscriberState { ACTIVE, PROCESSING, COMPLETE, IDLE }
