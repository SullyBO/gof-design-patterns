/**
 * Proxy pattern to provide a surrogate/placeholder for another object.
 * 
 * Comes in multiple types: 
 * - Remote proxy: locally represents an obj in a different address space
 * - Virtual proxy: defers creation of expensive objects
 * - Protection proxy: controls access to an obj
 * - Smart reference: replacement for bare ptr that performs housekeeping
 * 
 * ## Trade-offs:
 * Pros: Remote proxy provides address space transparency. Virtual proxy
 * can optimize by creating objects on demand. Both protection and smart
 * references allow additional housekeeping (caching/validation/etc.)
 * 
 * Cons: Indirection increases complexity and makes debugging harder,
 * but this is easily offset by the benefits.
 * 
 * ## Kotlin-specific nice things:
 * - Can implement virtual proxies declaratively using 'by lazy' init
 * - 'by' keyword works great for custom proxies
 * - Extension functions can add proxy behavior without inheritance
 * - Sealed classes especially useful for exhaustively representing state
 * 
 * ## Notes:
 * - Implemented a smart reference in the flyweight implementation
 * - In practice, almost all remote proxies are protection proxies cus of
 * built-in HTTP client protections (timeouts, retry logic, etc)
 * 
 */
data class Repository(val name: String, val url: String)
data class Profile(val name: String, val url: String)

interface GitHubService {
    fun getUserRepos(username: String): List<Repository>
    fun getUserProfile(username: String): Profile
}

// 'by lazy' is all you need in kotlin to implement a virtual proxy
private val client: GitHubClient by lazy { GitHubClient() }

interface GitHubApiClient {
    fun fetchUserRepos(request: String): String
    fun fetchUserProfile(request: String): String
}

class GitHubClient : GitHubApiClient {
    override fun fetchUserRepos(request: String): String {
        println("GitHubClient: Received request: $request")
        println("GitHubClient: Making network calls...")
        
        Thread.sleep(500)
        
        return """[{"name":"gof-design-patterns","url":"https://github.com/user/gof-design-patterns"},{"name":"uma_statue_calculator","url":"https://github.com/user/uma_statue_calculator"}]"""
    }

    override fun fetchUserProfile(request: String): String {
        println("GitHubClient: Received request: $request")
        println("GitHubClient: Making network calls...")
        
        Thread.sleep(300)
        
        return """{"name":"SullyBO","url":"https://github.com/user"}"""
    }
}

class GitHubRemoteProxy constructor(
    private val apiClient: GitHubApiClient,
) : GitHubService {
    private val repoCache = mutableMapOf<String, List<Repository>>()
    private val profileCache = mutableMapOf<String, Profile>()

    override fun getUserRepos(username: String): List<Repository> {
        val cacheKey = "repos_$username"
        
        return if (repoCache.containsKey(cacheKey)) {
            println("RemoteProxy: Returning cached repos for $username")
            repoCache[cacheKey]!!
        } else {
            println("RemoteProxy: Cache miss, making remote call")
            
            val jsonRequest = serializeRequest("getUserRepos", username)
            val jsonResponse = apiClient.fetchUserRepos(jsonRequest)
            val deserializedResult = deserializeRepoResponse(jsonResponse)
            repoCache[cacheKey] = deserializedResult
            deserializedResult
        }
    }

    override fun getUserProfile(username: String): Profile {
        val cacheKey = "profile_$username"
        
        return if (profileCache.containsKey(cacheKey)) {
            println("RemoteProxy: Returning cached profile for $username")
            profileCache[cacheKey]!!
        } else {
            println("RemoteProxy: Cache miss, making remote call")
            
            val jsonRequest = serializeRequest("getUserProfile", username)
            val jsonResponse = apiClient.fetchUserProfile(jsonRequest)
            val deserializedResult = deserializeProfileResponse(jsonResponse)
            profileCache[cacheKey] = deserializedResult
            deserializedResult
        }
    }

    private fun serializeRequest(method: String, param: String): String {
        println("RemoteProxy: Serializing request: $method($param) -> JSON")
        return """{"method":"$method","param":"$param"}"""
    }
    
    private fun deserializeRepoResponse(jsonResponse: String): List<Repository> {
        println("RemoteProxy: Received $jsonResponse JSON repo list")
        println("RemoteProxy: Deserializing repo response from JSON -> Kotlin objects")
        return listOf(
            Repository("gof-design-patterns", "https://github.com/user/gof-design-patterns"),
            Repository("uma_statue_calculator", "https://github.com/user/uma_statue_calculator")
        )
    }
    
    private fun deserializeProfileResponse(jsonResponse: String): Profile {
        println("RemoteProxy: Received $jsonResponse JSON profile")
        println("RemoteProxy: Deserializing profile response from JSON -> Kotlin objects")
        return Profile("SullyBO", "https://github.com/user")
    }
}
