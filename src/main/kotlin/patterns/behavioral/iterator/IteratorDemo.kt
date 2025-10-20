fun main() {
    val fruits = ConcreteCollection(listOf("Apple", "Banana", "Cherry", "Date", "Elderberry"))

    val iterator = fruits.createIterator()

    println("=== Basic Iteration ===")
    while (!iterator.isDone()) {
        println(iterator.getCurrentItem())
        iterator.next()
    }

    println("\n=== After Reset ===")
    iterator.reset()
    println("First item after reset: ${iterator.getCurrentItem()}")

    println("\n=== Partial Iteration (first 3 items) ===")
    iterator.reset()
    var count = 0
    while (!iterator.isDone() && count < 3) {
        println("${count + 1}. ${iterator.getCurrentItem()}")
        iterator.next()
        count++
    }

    println("\n=== Multiple Passes ===")
    for (pass in 1..2) {
        iterator.reset()
        println("Pass $pass:")
        while (!iterator.isDone()) {
            print("${iterator.getCurrentItem()} ")
            iterator.next()
        }
        println()
    }

    println("\n=== Integer Collection ===")
    val numbers = ConcreteCollection(listOf(10, 20, 30, 40, 50))
    val numIterator = numbers.createIterator()

    var sum = 0
    while (!numIterator.isDone()) {
        sum += numIterator.getCurrentItem()
        numIterator.next()
    }
    println("Sum of all numbers: $sum")
}
