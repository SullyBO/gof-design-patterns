/**
 * Iterator pattern for accessing elements of a collection sequentially
 * without exposing underlying representation.
 *
 * ## Trade-offs:
 * Pros: Decouples iteration from collection structure. Supports multi
 * simultaneous traversals with indie states. Can implement different
 * iteration strats (forward, backward, and bunnyhopping-filtered.)
 *
 * Cons: More verbose than modern language features (for-each loops
 * sequences.) Manual state management is error-prone (e.g. forgetting
 * isDone() checks is easy.) Creating additional objects for each
 * traversal can be a headache. Classic implementation needs bounds
 * checking and null-safety to be handled by user.
 *
 * ## Kotlin-specific nice things:
 * - Built-in Iterable/Iterator interfaces integrate with for loops
 * - Sequences provide lazy evaluation with similar traversal benefits
 * - Extension funcs (forEach, map, filter) eliminate manual iteration
 * - Operator overloading could make custom iterators feel more native
 *
 * ## Note
 * - Modern languages rarely need custom iterators
 * - Use builtin ones or sequences or extension funcs
 * - Iterators remind me of linked lists but much more abstracted
 */
interface CustomIterator<T> {
    fun reset()

    fun next()

    fun isDone(): Boolean

    fun getCurrentItem(): T
}

interface IterableCollection<T> {
    fun createIterator(): CustomIterator<T>
}

class ConcreteCollection<T>(
    private val items: List<T>,
) : IterableCollection<T> {
    override fun createIterator(): CustomIterator<T> {
        return ConcreteIterator(this)
    }

    fun getItem(index: Int): T = items[index]

    fun size(): Int = items.size
}

class ConcreteIterator<T>(
    private val collection: ConcreteCollection<T>,
) : CustomIterator<T> {
    private var currentIndex: Int = 0

    override fun reset() {
        currentIndex = 0
    }

    override fun next() {
        currentIndex++
    }

    override fun getCurrentItem(): T = collection.getItem(currentIndex)

    override fun isDone(): Boolean = currentIndex >= collection.size()
}
