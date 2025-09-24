import java.io.FileNotFoundException

/**
 * Flyweight pattern for sharing common textures efficiently
 *
 * Uses sharing to support large numbers of fine-grained objects efficiently.
 * Separates intrinsic state (shared, immutable) from extrinsic state (unique
 * per instance.) Factory manages flyweight creation and ensures sharing.
 * Useful when you have many objects that share common expensive data.
 *
 * ## Trade-offs:
 * Pros: Huge memory savings when you have many similar objects. Centralizes
 * resource management. Perfect fit for cached rscs like textures and fonts.
 * Factory can improve performance by reducing object creation overhead.
 *
 * Cons: Complexity of separating intrinsic/extrinsic state. Clients must
 * manage extrinsic state carefully. Can be overkill for small obj counts.
 * Lack of multithreading is a nonstarter when performance matters, and its
 * complexity is very real.
 *
 * ## Kotlin-specific nice things:
 * - Object keyword is nice for simple singleton implementation
 * - Data classes work well for immutable flyweight implementations
 * - Extension functions can add behavior without polluting flyweight interface
 * - Lazy initialization fits naturally with flyweight caching
 *
 * ## Note:
 * - Consider using simple caching if you don't need full intrinsic/extrinsic split
 * - Watch for memory leaks if flyweights hold references to extrinsic state
 * - For small datasets, runtime overhead might outweigh benefits
 * - This is a smart reference (subtype of proxy pattern)
 *
 * ===========================
 * The Context class for Game Objects
 */
class GameSprite constructor(
    private val texture: GameTexture,
) {
    // Extrinsic (unique) state to each instance
    var x: Float = 0f
    var y: Float = 0f
    var rotation: Float = 0f
    var scale: Float = 1f

    fun render() {
        if (!texture.isDisposed()) {
            println("Rendering ${texture.rawFile} at position ($x, $y), rotation: $rotation deg, scale: $scale")
        }
    }

    fun moveTo(
        newX: Float,
        newY: Float,
    ) {
        x = newX
        y = newY
        println("Moved sprite to ($x, $y)")
    }

    fun rotate(degrees: Float) {
        rotation = (rotation + degrees) % 360
        println("Rotated sprite to $rotation deg")
    }
}

// Flyweight class
data class GameTexture(val rawFile: Any) {
    private var disposed = false

    fun dispose() {
        if (!disposed) {
            println("Disposing texture: $rawFile")
            disposed = true
        }
    }

    fun isDisposed() = disposed
}

class TextureLoadException(message: String, cause: Throwable? = null) : Exception(message, cause)

// (Singleton) Flyweight Factory
object AssetManager {
    private val textureCache = mutableMapOf<String, GameTexture>()
    private val referenceCounts = mutableMapOf<String, Int>()

    fun getTexture(path: String): GameTexture {
        return textureCache[path] ?: run {
            val texture = loadTextureFromFile(path)
            textureCache[path] = texture
            referenceCounts[path] = 0
            println("Loaded texture: $path")
            texture
        }.also {
            referenceCounts[path] = referenceCounts.getValue(path) + 1
        }
    }

    fun releaseTexture(path: String) {
        val currentCount = referenceCounts[path] ?: return
        val newCount = currentCount - 1

        if (newCount <= 0) {
            freeTexture(path)
            referenceCounts.remove(path)
            println("Freed texture: $path")
        } else {
            referenceCounts[path] = newCount
        }
    }

    fun disposeAll() {
        textureCache.keys.toList().forEach(::freeTexture)
        referenceCounts.clear()
        println("All assets disposed.")
    }

    private fun loadTextureFromFile(path: String): GameTexture {
        return try {
            GameTexture(rawFile = "Loaded file $path")
        } catch (e: FileNotFoundException) {
            throw TextureLoadException("Failed to load texture from: $path", e)
        }
    }

    private fun freeTexture(path: String) {
        textureCache[path]?.dispose()
        textureCache.remove(path)
    }
}
