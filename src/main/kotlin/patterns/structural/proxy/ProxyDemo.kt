fun main() {
    println("=== Proxy Demo ===")
    val githubService: GitHubService = GitHubRemoteProxy(GitHubClient())
    
    println("\n--- First call - will hit remote service ---")
    val repos1 = githubService.getUserRepos("SullyBO")
    println("Retrieved ${repos1.size} repositories")
    repos1.forEach { println("  - ${it.name}: ${it.url}") }
    
    println("\n--- Second call - will use cache ---")
    val repos2 = githubService.getUserRepos("SullyBO")
    println("Retrieved ${repos2.size} repositories")
    
    println("\n--- Profile call ---")
    val profile = githubService.getUserProfile("SullyBO")
    println("Profile: ${profile.name} at ${profile.url}")
}