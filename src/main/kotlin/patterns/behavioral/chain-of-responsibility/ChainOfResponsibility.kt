/**
 * Chain of Responsibility pattern for middleware pattern
 *
 * CoR allows us to avoid coupling the request sender & receiver.
 * Giving more than one object agency in making decisions about and
 * handling the request. Chain the receiving objects & pass the req
 * along the chain in from more specific to more generalized objects.
 *
 * ## Trade-offs:
 * Pros: Reduced coupling. Added flexibility in assigning
 * responsibilities.
 *
 * Cons: No guarantee an object will receive and properly handle
 * the request, unlike when explicitly assigning handling of req.
 *
 * # Kotlin-specific nice things:
 * - Extension functions for fluent chain building
 * - Sealed classes for type-safe results
 * - Lambda handlers
 * - Data classes for immutable context
 *
 * # Note:
 * - I really wanted to come up with a realistic and useful example
 * because this pattern is important, but unfortunately the complexity
 * got out of hand very quickly and it easily could've kept getting worse.
 * On the bright side, middleware coding is for sure something every
 * programmer should know how to do.
 */
data class HttpRequest(
    val path: String,
    val method: String = "GET",
    val headers: Map<String, String> = emptyMap(),
    val body: String = "",
)

data class HttpResponse(
    val status: Int,
    val body: String,
    val headers: Map<String, String> = emptyMap(),
)

data class User(
    val id: String,
    val role: String,
    val permissions: Set<String>,
)

data class RequestContext(
    val request: HttpRequest,
    val user: User? = null,
    val metadata: Map<String, Any> = emptyMap(),
) {
    fun withUser(user: User) = copy(user = user)

    fun withMetadata(
        key: String,
        value: Any,
    ) = copy(metadata = metadata + (key to value))

    fun withMetadata(pairs: Map<String, Any>) = copy(metadata = metadata + pairs)
}

sealed class MiddlewareResult {
    data class Continue(val context: RequestContext) : MiddlewareResult()

    data class Terminate(val response: HttpResponse) : MiddlewareResult()

    inline fun onContinue(action: (RequestContext) -> Unit): MiddlewareResult {
        if (this is Continue) action(context)
        return this
    }

    inline fun onTerminate(action: (HttpResponse) -> Unit): MiddlewareResult {
        if (this is Terminate) action(response)
        return this
    }
}

fun interface Middleware {
    fun process(context: RequestContext): MiddlewareResult
}

fun middleware(handler: (RequestContext) -> MiddlewareResult): Middleware = Middleware(handler)

fun RequestContext.continueWith() = MiddlewareResult.Continue(this)

fun RequestContext.terminateWith(
    status: Int,
    body: String,
    headers: Map<String, String> = emptyMap(),
) = MiddlewareResult.Terminate(HttpResponse(status, body, headers))

class MiddlewarePipeline constructor(private val middlewares: List<Middleware>) {
    fun process(context: RequestContext): MiddlewareResult {
        return middlewares.fold(MiddlewareResult.Continue(context) as MiddlewareResult) { result, middleware ->
            when (result) {
                is MiddlewareResult.Continue -> middleware.process(result.context)
                is MiddlewareResult.Terminate -> result
            }
        }
    }

    companion object {
        fun builder() = MiddlewarePipelineBuilder()
    }
}

class MiddlewarePipelineBuilder {
    private val middlewares = mutableListOf<Middleware>()

    fun use(middleware: Middleware) = apply { middlewares.add(middleware) }

    fun use(handler: (RequestContext) -> MiddlewareResult) = use(middleware(handler))

    fun logging() = use(LoggingMiddleware)

    fun rateLimiting(maxRequestsPerMinute: Int = 60) = use(RateLimitingMiddleware(maxRequestsPerMinute))

    fun authentication() = use(AuthenticationMiddleware)

    fun authorization() = use(AuthorizationMiddleware)

    fun validation() = use(ValidationMiddleware)

    fun build() = MiddlewarePipeline(middlewares.toList())
}

object LoggingMiddleware : Middleware {
    override fun process(context: RequestContext): MiddlewareResult {
        val request = context.request
        println("${request.method} ${request.path}")

        return context
            .withMetadata("startTime", System.currentTimeMillis())
            .withMetadata("requestId", generateRequestId())
            .continueWith()
    }

    private fun generateRequestId() = "req_${System.currentTimeMillis()}_${(1000..9999).random()}"
}

class RateLimitingMiddleware(private val maxRequestsPerMinute: Int = 60) : Middleware {
    private val requestCounts = mutableMapOf<String, MutableList<Long>>()

    override fun process(context: RequestContext): MiddlewareResult {
        val clientIp = context.request.headers["X-Forwarded-For"] ?: "127.0.0.1"

        cleanupOldRequests(clientIp)

        return if (isRateLimitExceeded(clientIp)) {
            println("Rate limit exceeded for $clientIp")
            context.terminateWith(429, "Rate limit exceeded. Try again later.")
        } else {
            recordRequest(clientIp)
            println("Rate limit check passed for $clientIp (${requestCounts[clientIp]?.size}/$maxRequestsPerMinute)")
            context.continueWith()
        }
    }

    private fun cleanupOldRequests(clientIp: String) {
        val oneMinuteAgo = System.currentTimeMillis() - 60_000
        requestCounts[clientIp]?.removeIf { it < oneMinuteAgo }
    }

    private fun isRateLimitExceeded(clientIp: String) = requestCounts[clientIp]?.size ?: 0 >= maxRequestsPerMinute

    private fun recordRequest(clientIp: String) {
        requestCounts.getOrPut(clientIp) { mutableListOf() }.add(System.currentTimeMillis())
    }
}

object AuthenticationMiddleware : Middleware {
    private val users =
        mapOf(
            "bearer_admin_token" to User("admin", "admin", setOf("read", "write", "delete")),
            "bearer_user_token" to User("user123", "user", setOf("read")),
            "bearer_guest_token" to User("guest", "guest", setOf()),
        )

    override fun process(context: RequestContext): MiddlewareResult {
        val request = context.request

        return when {
            request.path.isPublicEndpoint() -> {
                println("Public endpoint, skipping authentication")
                context.continueWith()
            }

            else -> {
                val authHeader = request.headers["Authorization"]
                when {
                    authHeader == null -> {
                        println("Missing authentication token")
                        context.terminateWith(401, "Authentication required")
                    }

                    authHeader !in users -> {
                        println("Invalid authentication token")
                        context.terminateWith(401, "Invalid token")
                    }

                    else -> {
                        val user = users[authHeader]!!
                        println("Authenticated user: ${user.id} (${user.role})")
                        context.withUser(user).continueWith()
                    }
                }
            }
        }
    }
}

object AuthorizationMiddleware : Middleware {
    override fun process(context: RequestContext): MiddlewareResult {
        val request = context.request

        return when {
            request.path.isPublicEndpoint() -> context.continueWith()

            context.user == null -> context.terminateWith(403, "Access denied")

            else -> {
                val user = context.user
                val method = request.method
                val path = request.path
                val permission = request.method.requiredPermission()

                if (user.permissions.contains(permission)) {
                    println("Authorization check passed for ${user.id}")
                    context.continueWith()
                } else {
                    println("User ${user.id} lacks $permission permission for $method $path")
                    context.terminateWith(403, "Insufficient permissions")
                }
            }
        }
    }
}

object ValidationMiddleware : Middleware {
    private const val MAX_BODY_SIZE = 1_000_000
    private const val REQUIRED_CONTENT_TYPE = "application/json"

    override fun process(context: RequestContext): MiddlewareResult {
        val request = context.request

        return when {
            request.needsContentTypeValidation() && !request.hasValidContentType() -> {
                println("Invalid content type: ${request.headers["Content-Type"]}")
                context.terminateWith(400, "Content-Type must be application/json for API requests")
            }

            request.body.length > MAX_BODY_SIZE -> {
                println("Request body too large: ${request.body.length} bytes")
                context.terminateWith(413, "Request body too large")
            }

            else -> {
                println("Request validation passed")
                context.continueWith()
            }
        }
    }

    private fun HttpRequest.needsContentTypeValidation() = path.startsWith("/api") && method in listOf("POST", "PUT")

    private fun HttpRequest.hasValidContentType() = headers["Content-Type"] == REQUIRED_CONTENT_TYPE
}

private fun String.isPublicEndpoint() = startsWith("/public") || this == "/health"

private fun String.requiredPermission() =
    when {
        this == "GET" -> "read"
        this in listOf("POST", "PUT") -> "write"
        this == "DELETE" -> "delete"
        else -> "read"
    }

class RequestHandler {
    private val routes: Map<String, (HttpRequest, User?) -> HttpResponse> =
        mapOf(
            "/health" to { _, _ -> HttpResponse(200, "OK") },
            "/public/info" to { _, _ -> HttpResponse(200, "Public information") },
            "/api/profile" to { _, user -> HttpResponse(200, "Profile for user ${user?.id}") },
        )

    fun handleRequest(context: RequestContext): HttpResponse {
        logRequestProcessing(context)

        val response =
            routes[context.request.path]
                ?.invoke(context.request, context.user)
                ?: handleSpecialRoutes(context.request, context.user)
                ?: HttpResponse(404, "Not found")

        println("Returning ${response.status} response")
        return response
    }

    private fun handleSpecialRoutes(
        request: HttpRequest,
        user: User?,
    ): HttpResponse? {
        return when {
            request.path.startsWith("/api/admin") -> {
                if (user?.role == "admin") {
                    HttpResponse(200, "Admin data")
                } else {
                    HttpResponse(403, "Admin access required")
                }
            }
            else -> null
        }
    }

    private fun logRequestProcessing(context: RequestContext) {
        val requestId = context.metadata["requestId"]
        val startTime = context.metadata["startTime"] as? Long ?: System.currentTimeMillis()
        val processingTime = System.currentTimeMillis() - startTime
        println("Processing request $requestId (${processingTime}ms so far)")
    }
}

class WebServer constructor(private val pipeline: MiddlewarePipeline) {
    private val requestHandler = RequestHandler()

    fun processRequest(
        path: String,
        method: String = "GET",
        headers: Map<String, String> = emptyMap(),
        body: String = "",
    ): HttpResponse {
        val request = HttpRequest(path, method, headers, body)
        val context = RequestContext(request)

        return when (val result = pipeline.process(context)) {
            is MiddlewareResult.Continue -> requestHandler.handleRequest(result.context)
            is MiddlewareResult.Terminate -> result.response
        }
    }

    companion object {
        fun configure(block: MiddlewarePipelineBuilder.() -> Unit): WebServer {
            val pipeline = MiddlewarePipeline.builder().apply(block).build()
            return WebServer(pipeline)
        }
    }
}
