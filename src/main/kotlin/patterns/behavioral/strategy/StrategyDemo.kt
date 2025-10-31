fun main() {
    val guard = AiEntity(Pos(0.0, 0.0, 0.0), Patrol())

    println("=== AI Strategy Demo ===\n")

    println("=== Guard starts patrolling ===")
    guard.move(Pos(10.0, 5.0, 0.0))

    println("\n=== Player spotted! Guard switches to chase mode ===")
    guard.setStrategy(Chasing())
    guard.move(Pos(10.0, 5.0, 0.0))

    println("\n=== Guard moves closer to player ===")
    guard.pos = Pos(5.0, 3.0, 0.0)
    guard.move(Pos(10.0, 5.0, 0.0))

    println("\n=== Player escaped! Back to patrolling ===")
    guard.setStrategy(Patrol())
    guard.move(Pos(10.0, 5.0, 0.0))

    println("\n=== Demo Complete ===")
}
