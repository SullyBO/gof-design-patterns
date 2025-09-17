/**
 * Bridge pattern separating abstraction from implementation.
 *
 * Decouples an abstraction from its implementation so both can vary
 * independently. Useful when you have multiple ways to implement something
 * and multiple things that need implementing, prevents subclass proliferation.
 *
 * ## Trade-offs:
 * Pros: Enables runtime configuration of implementations. Separates interface
 * from implementation details. Easy to extend either hierarchy independently.
 * Reduces coupling between abstraction and concrete implementations.
 *
 * Cons: Increases complexity with additional abstraction layer. Can be overkill
 * for simple scenarios. Requires more upfront design thinking about what varies.
 *
 * ## Kotlin-specific nice things:
 * - Constructor parameters make dependency injection clean and explicit
 * - Interface implementations are concise
 * - Primary constructors eliminate boilerplate for simple bridge setup
 * - Extension functions could add behavior without modifying hierarchies
 *
 * ## Note:
 * - Consider if you actually need two varying dimensions vs simple strategy pattern
 * - Runtime configuration is very nice but can be achieved by simple Composition.
 * - With that said, maybe start with composition and evolve into this if necessary.
 *
 * ========================================================================
 *
 * Game object rendering with swappable visual styles
 */
abstract class GameObject(protected val renderer: Renderer) {
    abstract fun draw()
}

class Player(renderer: Renderer) : GameObject(renderer) {
    override fun draw() {
        renderer.drawSprite("player.png")
    }
}

class Enemy(renderer: Renderer) : GameObject(renderer) {
    override fun draw() {
        renderer.drawSprite("enemy.png")
    }
}

interface Renderer {
    fun drawSprite(sprite: String)
}

class PixelRenderer : Renderer {
    override fun drawSprite(sprite: String) {
        println("Drawing $sprite in pixel art")
    }
}

class VoxelRenderer : Renderer {
    override fun drawSprite(sprite: String) {
        println("Drawing $sprite with voxel graphics")
    }
}
