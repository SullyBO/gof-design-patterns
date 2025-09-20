fun main() {
    val myFunction = Function("memoryHog", ::memoryHog)

    val profiledFunction = ProfilerDecorator(myFunction)

    profiledFunction.execute()

    // OR simply, try extension function elegance
    ::memoryHog.profile("memoryHog")

    // Can even inline call or use a lambda expr
}

fun memoryHog(): List<String> {
    val result = mutableListOf<String>()

    repeat(100_000) { i ->
        val wastefulString = "Processing item number $i with extra padding".repeat(10)
        result.add(wastefulString)

        val temp = (0..100).map { "temp_$it$wastefulString" }
        temp.forEach { /* nothing to see here, wasting cycles */ }
    }

    return result
}
