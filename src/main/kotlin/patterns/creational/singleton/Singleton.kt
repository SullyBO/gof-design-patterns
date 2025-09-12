

/**
 * Singleton pattern ensuring only one instance exists with global access.
 *
 * Guarantees a class only has one instance and provides a global point of
 * access to it. Useful for managing shared resources like database connections,
 * loggers, or configuration managers.
 *
 * With implementation of singleton being very straightforward without much wiring,
 * I decided to implement different levels of the classic Singleton (thread-safe and unsafe)
 * in addition to kotlin-idiomatic implementations (simple 'object' keyword and custom 'by lazy'.)
 *
 * ## Trade-offs:
 * Pros: Controls resource usage for expensive objects. Provides consistent
 * global state. Thread-safe initialization when implemented correctly.
 * More flexible than static methods (inheritance, polymorphism, interfaces.)
 *
 * Cons: Creates hidden dependencies making testing difficult (DI frameworks help.)
 * Introduces global state reducing modularity. Can become bottleneck in concurrent apps.
 * Memory lifecycle tied to app lifetime.
 *
 * ## Kotlin-specific nice things:
 * - "object" keyword provides a thread-safe singleton w.o boilerplate
 * - "by lazy" operator handles thread-safe lazy initilization automatically
 * - Companion objects enable static-like access while maintaining OOP benefits
 *   this point is in comparison to C++/Smalltalk constraint, as mentioned in the book
 * - No need for manual synchronization or double-checked locking patterns
 *
 * ## Note:
 * - Prefer dependency injection frameworks for better testability
 * - Use 'object' for simple cases, 'by lazy' class when configuration needed (See: SingletonDemo.kt)
 * - Consider if you actually need global state vs scoped instances
 *
 * ========================================================================
 *
 * Kotlin-idiomatic and simple method
 *
 * it really is just that, wasn't exaggerating when I said "w.o boilerplate"
 */
object Singleton {
    fun sayHello() {
        println("Singleton's that easy in Kotlin")
    }
}

/**
 * Classical singleton implementation
 *
 * Simple and w.o a locking mechanism to ensure thread-safety
 *
 * Illustrates under the hood implementation
 */
class ClassicSingleton private constructor() {
    companion object {
        private var instance: ClassicSingleton? = null

        fun getInstance(): ClassicSingleton {
            if (instance != null) {
                return instance!!
            }
            instance = ClassicSingleton()
            return instance!!
        }
    }
}

/**
 * Thread-safe version of the classic implementation
 *
 * utilizes @Synchronized annotation for locking and preventing race conditions
 * @Synchronized annotation is equivalent to Java's "synchronized" keyword
 */
class ThreadSafeSingleton private constructor() {
    companion object {
        private var instance: ThreadSafeSingleton? = null

        @Synchronized
        fun getInstance(): ThreadSafeSingleton {
            if (instance != null) {
                return instance!!
            }
            instance = ThreadSafeSingleton()
            return instance!!
        }
    }
}
