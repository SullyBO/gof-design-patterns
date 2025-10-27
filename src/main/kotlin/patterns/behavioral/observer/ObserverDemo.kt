fun main() {
    println("=== Observer Pattern Demo ===\n")

    val publisher = Publisher()

    val publicSub1 = PublicSubscriber("Lily")
    val publicSub2 = PublicSubscriber("Slowbro")
    val anonSub1 = AnonymousSubscriber()
    val anonSub2 = AnonymousSubscriber()

    println("--- Subscribing users ---")
    publisher.subscribe(publicSub1)
    publisher.subscribe(publicSub2)
    publisher.subscribe(anonSub1)
    publisher.subscribe(anonSub2)

    println("\n--- Publishing first message ---")
    publisher.notifySubs("Welcome to my page")

    println("\n--- Unsubscribing Slowbro ---")
    publisher.unsubscribe(publicSub2)

    println("\n--- Publishing second message ---")
    publisher.notifySubs("New banger just dropped")

    println("\n--- Unsubscribing anonymous user ---")
    publisher.unsubscribe(anonSub1)

    println("\n--- Publishing final message ---")
    publisher.notifySubs("Thanks for not unsubbing :(")

    println("\n=== Demo Complete ===")
}
