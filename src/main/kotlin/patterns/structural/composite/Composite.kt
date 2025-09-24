/**
 * Composite pattern for treating individual objects and compositions uniformly.
 *
 * Composes objects into tree structures to represent part-whole hierarchies.
 * Lets clients treat individual objects and compositions of objects uniformly.
 * Useful when you need to work with tree-like structures or when operations
 * should work the same on leaves and branches.
 *
 * ## Trade-offs:
 * Pros: Uniform interface for leaves and composites. Easy to add new component
 * types. Recursive operations come naturally. Clients don't need to distinguish
 * between simple and complex elements.
 *
 * Cons: Can make design too generalized. Interface might need to support
 * operations that don't make sense for all components. Can be overkill.
 *
 * ## Kotlin-specific nice things:
 * - Extension functions like sumOf() make aggregate operations clean
 * - Sealed classes could enforce type safety if you know all component types
 * - Data classes work well for simple leaf implementations
 *
 * ## Note:
 * - Consider if you need full composite pattern vs simple obj collection
 * - Reconsider if not all inherited operations make sense
 * - For simple cases, just use lists
 */
interface ComponentPurchasable {
    fun getPrice(): Double

    fun displayItemDetails()
}

class LeafItem constructor(
    private val name: String,
    private val price: Double,
) : ComponentPurchasable {
    override fun getPrice(): Double {
        return price
    }

    override fun displayItemDetails() {
        println("Name: $name Price: $price")
    }
}

class CompositeBundle constructor(
    private val children: MutableList<ComponentPurchasable>,
) : ComponentPurchasable {
    fun add(newPurchasable: ComponentPurchasable) {
        children.add(newPurchasable)
    }

    fun remove(purchasable: ComponentPurchasable) {
        children.remove(purchasable)
    }

    fun getChildren(): List<ComponentPurchasable> {
        return children.toList()
    }

    override fun getPrice(): Double {
        return children.sumOf { it.getPrice() }
    }

    override fun displayItemDetails() {
        children.forEach { it.displayItemDetails() }
    }
}
