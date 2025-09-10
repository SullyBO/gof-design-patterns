fun main() {
    demonstrateBuilder()
}

fun demonstrateBuilder() {
    val minionBuilder = ConcreteMinionBuilder()
    val director = Director(minionBuilder)

    director.constructCommonCard()
        .setName("Murloc Tinyfin")
        .setCost(0)
        .setAttack(1)
        .setHealth(1)
        .printResult()
}
