/**
 * Decorator (aka Wrapper) for naively profiling performance.
 *
 * Decorators are used to attach additional responsibilities to
 * objects dynamically.
 *
 * ## Trade-offs:
 * Pros: More flexible than static inheritance. Pay-as-you-go
 * approach to adding responsibilities.
 *
 * Cons: Decorator and its component aren't identical (Can't use instanceOf.)
 * Lots of shallow objects can get out of hand.
 *
 * Kotlin-specific nice things:
 * - Extension functions are so elegant here that I had to showcase them
 *
 * ## Note:
 * - This profiler is very naive, garbage collection and JVM optimization
 * complexities are too much.
 * - With something this naive the best you can get is running it multiple
 * times and measuring worst/avg/best cases.
 *
 * =====================================
 *
 * Classical implementation of a Decorator
 */
interface Executable {
    fun execute(): Any
}

// Base Decorator
abstract class ExecutableDecorator(
    protected val executable: Executable,
) : Executable {
    override fun execute(): Any {
        return executable.execute()
    }
}

// Concrete Decorator
class ProfilerDecorator(executable: Executable) :
    ExecutableDecorator(executable) {
    override fun execute(): Any {
        val runtime = Runtime.getRuntime()

        val startMemory = runtime.totalMemory() - runtime.freeMemory()
        val startTime = System.nanoTime()

        val result = super.execute()

        val endTime = System.nanoTime()
        val endMemory = runtime.totalMemory() - runtime.freeMemory()

        val executionTime = (endTime - startTime) / 1_000_000.0 // ms
        val memoryDiff = endMemory - startMemory

        val functionName = if (executable is Function) executable.getName() else "Unknown"

        println("\n--- Profile: $functionName ---")
        println("Execution time: %.4f ms".format(executionTime))
        println("Memory delta: ${formatMemory(memoryDiff)}")
        println("Total memory: ${formatMemory(endMemory)}")

        return result
    }

    fun formatMemory(bytes: Long): String {
        return when {
            bytes >= 1_073_741_824 -> "%.3f GB".format(bytes / 1_073_741_824.0)
            bytes >= 1_048_576 -> "%.2f MB".format(bytes / 1_048_576.0)
            bytes >= 1_024 -> "%.1f KB".format(bytes / 1_024.0)
            else -> "$bytes B"
        }
    }
}

// Concrete Component
class Function(
    private val functionName: String,
    private val block: () -> Any,
) : Executable {
    override fun execute(): Any {
        return block()
    }

    fun getName(): String = functionName
}

/** Kotlin-idiomatic extension function approach
 *
 * Much simpler implementation and ever simpler usage
 */
fun <T> (() -> T).profile(name: String = "function"): T {
    val runtime = Runtime.getRuntime()

    val startMemory = runtime.totalMemory() - runtime.freeMemory()
    val startTime = System.nanoTime()

    val result = this()

    val endTime = System.nanoTime()
    val endMemory = runtime.totalMemory() - runtime.freeMemory()

    val executionTime = (endTime - startTime) / 1_000_000.0
    val memoryDiff = endMemory - startMemory

    println("\n--- Profile: $name ---")
    println("Execution time: %.4f ms".format(executionTime))
    println("Memory delta: ${memoryDiff.formatMemory()}")
    println("Total memory: ${endMemory.formatMemory()}")

    return result
}

fun Long.formatMemory(): String =
    when {
        this >= 1_073_741_824 -> "%.3f GB".format(this / 1_073_741_824.0)
        this >= 1_048_576 -> "%.2f MB".format(this / 1_048_576.0)
        this >= 1_024 -> "%.1f KB".format(this / 1_024.0)
        else -> "$this B"
    }
