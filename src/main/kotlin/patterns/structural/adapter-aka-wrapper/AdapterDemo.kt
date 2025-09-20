fun objectAdapterDemo() {
    val legacyHandler = LegacyFileHandler()
    val fileManager = FileAdapter(legacyHandler)

    val content = fileManager.readFile("example.txt")
    println("\nRead from file: $content")

    fileManager.writeFile("output.txt", "Hello from adapter!")
}

fun classAdapterDemo() {
    println(adaptClass())
}

fun main() {
    objectAdapterDemo()
    classAdapterDemo()
}
