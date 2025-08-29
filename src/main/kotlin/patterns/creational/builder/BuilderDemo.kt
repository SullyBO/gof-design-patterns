@file:Suppress("MagicNumber")
fun main() {
    demonstrateBuilder()
}

fun demonstrateBuilder() {
    val minionBuilder = ConcreteMinionBuilder()
    val director = Director(minionBuilder)

    val murlocTinyfin = director.constructCommonCard<ConcreteMinionBuilder>()
        .setName("Murloc Tinyfin")
        .setCost(0)
        .setAttack(1)
        .setHealth(1)
        .printResult()
}
