fun main() {
    println("=== Composite Pattern Demo ===\n")
    
    val cheeseBurger = LeafItem("Cheese Borgir", 8.99)
    val frenchFries = LeafItem("French Fry", 3.99)
    val softDrink = LeafItem("Soder", 2.99)
    val garlicSauce = LeafItem("Giga Aromatic Sauce", 0.99)

    val menu = listOf(cheeseBurger, frenchFries, softDrink, garlicSauce)

    println("--- Individual Item Menu: ---")
    menu.forEach { item ->
        item.displayItemDetails()
    }

    val cheeseBurgerMeal = CompositeBundle(mutableListOf(
        cheeseBurger, 
        frenchFries, 
        softDrink
        )
    )

    println("\n--- Cheese Burger Meal Contents: ---")
    cheeseBurgerMeal.displayItemDetails()
    println("Cheese Burger Meal Total: $${cheeseBurgerMeal.getPrice()}")
}
