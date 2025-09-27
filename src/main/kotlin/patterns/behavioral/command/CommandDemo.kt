class Demo {
    private val player = PlayerReceiver()
    private val inputHandler = InputHandler(mutableMapOf())
    
    // Pre-create commands
    private val moveForward = MoveForwardCommand(player)
    private val moveBackward = MoveBackwardCommand(player)
    private val moveLeft = MoveLeftwardCommand(player)
    private val moveRight = MoveRightwardCommand(player)
    private val attack = AttackCommand(player)
    private val ultimate = UltimateCommand(player)
    
    init {
        setupControls()
    }
    
    private fun setupControls() {
        inputHandler.bindKey("w", moveForward)
        inputHandler.bindKey("s", moveBackward)
        inputHandler.bindKey("a", moveLeft)
        inputHandler.bindKey("d", moveRight)
        inputHandler.bindKey("space", attack)
        inputHandler.bindKey("q", ultimate)
    }
    
    fun run() {
        println("Controls: w/s (move), a/d (strafe), space (attack), q (ultimate)")
        println("Type 'quit' to exit\n")
        
        while (true) {
            print("Enter command: ")
            val input = readLine()?.lowercase() ?: ""
            
            if (input == "quit") {
                println("Thanks for playing!")
                break
            }
            
            inputHandler.handleInput(input)
        }
    }
}

fun main() {
    val demo = Demo()
    demo.run()
}