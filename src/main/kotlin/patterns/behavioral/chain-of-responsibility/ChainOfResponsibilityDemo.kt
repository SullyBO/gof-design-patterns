fun main() {
    val server =
        WebServer.configure {
            logging()
            rateLimiting(maxRequestsPerMinute = 5)
            authentication()
            authorization()
            validation()

            use { context ->
                println("Custom middleware processing ${context.request.path}")
                context.continueWith()
            }
        }

    println("=== Testing Web Server ===\n")

    server.processRequest("/health").also { println("Health check: ${it.status}\n") }

    server.processRequest(
        path = "/api/profile",
        headers = mapOf("Authorization" to "bearer_user_token"),
    ).also { println("Profile request: ${it.status}\n") }

    repeat(4) {
        server.processRequest("/api/profile")
    }

    println("\nNotice how each middleware either:")
    println("- Processes and continues (logging, auth success)")
    println("- Stops the chain early (rate limit, auth failure)")
    println("- Modifies the context for later middleware (adds user info)")
}
