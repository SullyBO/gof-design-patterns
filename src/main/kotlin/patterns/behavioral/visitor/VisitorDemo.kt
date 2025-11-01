fun main() {
    println("=== Visitor Pattern Demo ===")
    val document =
        listOf(
            Heading("Visitor Pattern Tutorial", 1),
            Paragraph("The Visitor pattern allows you to add new operations to existing classes without modifying them."),
            Heading("Key Benefits:", 2),
            Paragraph("It makes adding new operations easy."),
            Paragraph("It can accumulate state."),
            Paragraph("It works across class hierarchies."),
            Paragraph("It groups related operations and separates unrelated ones."),
            Image("diagram.png", "Visitor pattern UML diagram"),
            Heading("Example Use Cases", 2),
            Paragraph("Document exporters, syntax tree processors, and object serializers."),
        )

    println("=".repeat(50))
    println("HTML EXPORT")
    println("=".repeat(50))
    val htmlExporter = HtmlExporter()
    document.forEach { it.accept(htmlExporter) }
    println(htmlExporter.getHtml())

    println("\n" + "=".repeat(50))
    println("MARKDOWN EXPORT")
    println("=".repeat(50))
    val mdExporter = MarkdownExporter()
    document.forEach { it.accept(mdExporter) }
    println(mdExporter.getMarkdown())

    println("\n" + "=".repeat(50))
    println("PLAIN TEXT EXPORT")
    println("=".repeat(50))
    val textExporter = PlainTextExporter()
    document.forEach { it.accept(textExporter) }
    println(textExporter.getPlainText())
    println("=== Demo Done ===")
}
