/**
 * This is obviously too verbose for say a game designer to use
 * but with an actual parser, scripting can become very easy and
 * simple enough for a layperson to script logic and iterate w.o
 * needing to bother programmers unless it's to add new features.
 */
fun main() {
    val context =
        QuestContext(
            playerLevel = 12,
            gold = 600,
            inventory = setOf("stinky cheese", "key"),
            enemiesKilled = mapOf("pirate" to 200, "murloc" to 0),
        )

    val questConditions =
        AndExpression(
            GreaterThanOrEqual(
                NumericVariable("level"),
                NumberLiteral(10),
            ),
            HasItemExpression(StringLiteral("key")),
        )

    val questAccepted = questConditions.interpret(context)
    println("Quest conditions met: $questAccepted")
}
