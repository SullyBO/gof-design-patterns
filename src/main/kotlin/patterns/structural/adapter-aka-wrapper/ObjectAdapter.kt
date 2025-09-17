/**
 * Object Adapter pattern enabling incompatible interfaces to work together.
 *
 * Wraps an existing class with a new interface, allowing classes with
 * incompatible interfaces to collaborate. Uses composition to delegate
 * calls to the wrapped object while translating the interface.
 *
 * ## Trade-offs:
 * Pros: Integrates legacy or 3rd party code without modification. 
 * Enables reuse of existing functionality.
 *
 * Cons: Adds indirection layer. Can hide the complexity of interface mismatches.
 * May require extensive translation for complex interfaces.
 *
 * ## Kotlin-specific nice things:
 * - Constructor parameters make wrapping explicit and clean
 * - Extension functions can sometimes eliminate need for full adapter
 * - Data classes reduce boilerplate for simple data transformations
 *
 * ## Note:
 * - Consider extension functions for simple cases before full adapter pattern
 * - Object adapter (composition) over class adapter (inheritance)
 * - Not like Kotlin or Java support multiple inheritance for class adapters to work
 */
interface FileManager {
    fun readFile(filename: String): String
    fun writeFile(filename: String, content: String)
}

class LegacyFileHandler {
    fun loadFromDisk(path: String): ByteArray { 
        println("Loaded file $path")
        return byteArrayOf(72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33)
    }
    fun saveToDisk(path: String, bytes: ByteArray) {
        println("Saved file of size: ${bytes.size} bytes to $path")
    }
}

class FileAdapter(
    private val legacyHandler: LegacyFileHandler
    ) : FileManager {
        override fun readFile(filename: String): String { 
            val bytes = legacyHandler.loadFromDisk(filename)
            return String(bytes)
         }
        
        override fun writeFile(filename: String, content: String) { 
            legacyHandler.saveToDisk(filename, content.toByteArray())
         }
}
