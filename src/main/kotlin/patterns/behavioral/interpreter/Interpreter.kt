/**
 * Interpreter pattern for evaluating quest condition expressions
 * 
 * Defines a representation for a language's grammar along with an 
 * interpreter that uses the representation to interpret sentences 
 * in the language. Each grammar rule becomes a class, w/ terminal 
 * expressions representing leaf nodes and non-terminal expressions
 * representing language composition rules. The pattern evaluates 
 * expressions by recursively interpreting the abstract syntax tree
 * 
 * ## Trade-offs:
 * Pros: Easy to change and extend grammar (just add expr classes.)
 * Each grammar rule is explicit and easy to understand. Works well
 * for domain-specific languages (DSL) mini-languages. Separates 
 * parsing from evaluation.
 * 
 * Cons: Complex grammars are difficult to maintain due to class 
 * explosion from implementing one class per rule. Performance 
 * can suffer with deep expression trees. No built-in optimization.
 * Better tools exist for complex parsing (parser generators, AST
 * walkers.)
 * 
 * ## Kotlin-specific nice things:
 * - Data classes make expression nodes concise with minimal boilerplate
 * - Sealed interfaces restrict expr hierarchies at compile-time
 * 
 * ## Note:
 * - Best for small, stable domain-specific languages (DSLs)
 * - For parsing, use parser generators. Parser generators save time and
 * maintaining a self-documenting grammar file has its own benefits
 * - Keep grammar super simple for designer UX and maintainability
 */
interface BooleanExpression {
    fun interpret(context: QuestContext): Boolean
}

interface NumericExpression {
    fun interpret(context: QuestContext): Int
}

interface StringExpression {
    fun interpret(context: QuestContext): String
}

// Terminal expressions (leaf nodes)
class NumberLiteral(val value: Int) : NumericExpression {
    override fun interpret(context: QuestContext) = value
}

class NumericVariable(val name: String) : NumericExpression {
    override fun interpret(context: QuestContext): Int {
        return when(name) {
            "level" -> context.playerLevel
            "gold" -> context.gold
            else -> 0
        }
    }
}

class StringLiteral(val value: String) : StringExpression {
    override fun interpret(context: QuestContext) = value
}

class BooleanLiteral(val value: Boolean) : BooleanExpression {
    override fun interpret(context: QuestContext) = value
}

// Non-terminal expressions (ops and other expressions)
class GreaterThanOrEqual(
    val left: NumericExpression,
    val right: NumericExpression
) : BooleanExpression {
    override fun interpret(context: QuestContext): Boolean {
        return left.interpret(context) >= right.interpret(context)
    }
}

class Equals(
    val left: NumericExpression,
    val right: NumericExpression
) : BooleanExpression {
    override fun interpret(context: QuestContext): Boolean {
        return left.interpret(context) == right.interpret(context)
    }
}

class GreaterThan(
    val left: NumericExpression,
    val right: NumericExpression
) : BooleanExpression {
    override fun interpret(context: QuestContext): Boolean {
        return left.interpret(context) > right.interpret(context)
    }
}

class LessThan(
    val left: NumericExpression,
    val right: NumericExpression
) : BooleanExpression {
    override fun interpret(context: QuestContext): Boolean {
        return left.interpret(context) < right.interpret(context)
    }
}

// Logical non-terminals
class AndExpression(
    val left: BooleanExpression,
    val right: BooleanExpression
) : BooleanExpression {
    override fun interpret(context: QuestContext): Boolean {
        return left.interpret(context) && right.interpret(context)
    }
}

class OrExpression(
    val left: BooleanExpression,
    val right: BooleanExpression
) : BooleanExpression {
    override fun interpret(context: QuestContext): Boolean {
        return left.interpret(context) || right.interpret(context)
    }
}

class NotExpression(
    val expression: BooleanExpression
) : BooleanExpression {
    override fun interpret(context: QuestContext): Boolean {
        return !expression.interpret(context)
    }
}

// Domain-specific non-terminals
class HasItemExpression(
    val itemName: StringExpression
) : BooleanExpression {
    override fun interpret(context: QuestContext): Boolean {
        return context.hasItem(itemName.interpret(context))
    }
}

class KilledEnemyExpression(
    val enemyType: StringExpression,
    val minCount: NumericExpression
) : BooleanExpression {
    override fun interpret(context: QuestContext): Boolean {
        val enemyName = enemyType.interpret(context)
        val required = minCount.interpret(context)
        return context.getEnemyKillCount(enemyName) >= required
    }
}

class QuestContext(
    val playerLevel: Int,
    val gold: Int,
    val inventory: Set<String>,
    val enemiesKilled: Map<String, Int>
) {
    fun hasItem(itemName: String): Boolean = inventory.contains(itemName)
    
    fun getEnemyKillCount(enemyType: String): Int = enemiesKilled[enemyType] ?: 0
}