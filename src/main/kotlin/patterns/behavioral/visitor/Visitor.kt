/**
 * Visitor pattern for operations on object structures
 *
 * Separates algorithms from the objects they operate on by moving
 * operations into separate visitor classes. Each visitor impl ops
 * for different element types, and elements accept visitors by
 * calling the appropriate visit method. This enables adding new
 * ops without modifying element classes.
 *
 * ## Trade-offs:
 * Pros: Easy to add new ops. Keeps related ops together and
 * separates unrelated ones. Can accumulate state. Works across
 * class hierarchies.
 *
 * Cons: Adding a new element type requires updating ALL visitors.
 * Breaks encapsulation unless eles are accessed via a safe API.
 * Circular dependency between visitors and elements is a HUGE and
 * serious concern.
 *
 * ## Kotlin-specific nice things:
 * - Sealed classes + exhaustive 'when' work great for ele hierarchies
 * - Extension functions can replace this pattern for simple cases
 *
 * ## Note:
 * - Proceed with caution, circular dependency is no joke
 * - Viable if operations change a lot but element types are stable (rare)
 */
interface Visitor {
    fun visitParagraph(paragraph: Paragraph)

    fun visitImage(image: Image)

    fun visitHeading(heading: Heading)
}

interface Element {
    fun accept(visitor: Visitor)
}

// Concrete visitors
class HtmlExporter : Visitor {
    private val result = StringBuilder()

    override fun visitParagraph(paragraph: Paragraph) {
        result.append("<p>${paragraph.text}</p>\n")
    }

    override fun visitImage(image: Image) {
        result.append("<img src=\"${image.url}\" alt=\"${image.altText}\" />\n")
    }

    override fun visitHeading(heading: Heading) {
        result.append("<h${heading.level}>${heading.text}</h${heading.level}>\n")
    }

    fun getHtml() = result.toString()
}

class MarkdownExporter : Visitor {
    private val result = StringBuilder()

    override fun visitParagraph(paragraph: Paragraph) {
        result.append("${paragraph.text}\n\n")
    }

    override fun visitImage(image: Image) {
        result.append("![${image.altText}](${image.url})\n\n")
    }

    override fun visitHeading(heading: Heading) {
        result.append("${"#".repeat(heading.level)} ${heading.text}\n\n")
    }

    fun getMarkdown() = result.toString()
}

class PlainTextExporter : Visitor {
    private val result = StringBuilder()

    override fun visitParagraph(paragraph: Paragraph) {
        result.append("${paragraph.text}\n\n")
    }

    override fun visitImage(image: Image) {
        result.append("[Image: ${image.altText}]\n\n")
    }

    override fun visitHeading(heading: Heading) {
        result.append("${heading.text.uppercase()}\n${"=".repeat(heading.text.length)}\n\n")
    }

    fun getPlainText() = result.toString()
}

// Concrete doc elements
class Paragraph(val text: String) : Element {
    override fun accept(visitor: Visitor) {
        visitor.visitParagraph(this)
    }
}

class Image(val url: String, val altText: String) : Element {
    override fun accept(visitor: Visitor) {
        visitor.visitImage(this)
    }
}

class Heading(val text: String, val level: Int) : Element {
    override fun accept(visitor: Visitor) {
        visitor.visitHeading(this)
    }
}
