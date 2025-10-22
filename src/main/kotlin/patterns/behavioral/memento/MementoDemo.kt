fun main() {
    val gameState = GameState()
    val saveManager = SaveManager()

    println("=== New Game ===")
    println(gameState)

    gameState.addItem("Sword")
    gameState.addItem("Health Potion")
    gameState.addQuest(1)
    gameState.movePlayer(10.0, 5.0, 0.0)
    gameState.levelUp()

    println("\n=== After Playing ===")
    println(gameState)

    saveManager.saveToSlot(1, gameState.createCheckpoint())

    gameState.takeDamage(50.0)
    gameState.completeQuest(1)
    gameState.movePlayer(20.0, 10.0, 5.0)

    println("\n=== After More Playing ===")
    println(gameState)

    saveManager.autoSave(gameState.createCheckpoint())

    gameState.takeDamage(100.0)

    println("\n=== After Heavy Damage ===")
    println(gameState)

    println("\n=== Loading Save ===")
    val save = saveManager.loadFromSlot(1)
    save?.let {
        gameState.loadFromSave(it)
        println(gameState)
    }
}
