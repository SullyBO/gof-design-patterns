/**
 * 'By lazy' custom Singleton implementation and demonstration
 * to illustrate when it might be appropriate to opt for custom behavior
 * over the idiomatic and simple object keyword
 *
 * Example of database management with different logging
 * configurations seemed most appropriate for this demo
 */
fun main() {
    println("=== Database Singleton Demo ===")

    val db = DatabaseManager.instance

    db.connect()
    val users = db.query("SELECT * FROM users")
    println("Found users: $users")

    db.connect()

    db.query("SELECT COUNT(*) FROM orders")

    db.disconnect()

    println("\n=== Verifying singleton behavior ===")
    val db2 = DatabaseManager.instance
    println("Same instance? ${db === db2}")
}

// Configuration data class
data class DatabaseConfig(
    val host: String,
    val port: Int,
    val database: String,
    val environment: String,
    val maxConnections: Int,
)

// Simple logger implementations
interface Logger {
    fun info(message: String)

    fun debug(message: String)

    fun error(message: String)
}

class ConsoleLogger : Logger {
    override fun info(message: String) = println("[INFO] $message")

    override fun debug(message: String) = println("[DEBUG] $message")

    override fun error(message: String) = println("[ERROR] $message")
}

class NoOpLogger : Logger {
    override fun info(message: String) {
        println(message)
    }

    override fun debug(message: String) {
        println(message)
    }

    override fun error(message: String) {
        println(message)
    }
}

// Hardcoded config loader for demonstration
fun loadDatabaseConfig(): DatabaseConfig {
    return DatabaseConfig(
        host = "localhost",
        port = 5432,
        database = "my_app_db",
        environment = "development",
        maxConnections = 10,
    )
}

// DatabaseManager customized Singleton class
class DatabaseManager private constructor(
    private val config: DatabaseConfig,
    private val logger: Logger,
) {
    private var isConnected = false

    companion object {
        val instance: DatabaseManager by lazy {
            val config = loadDatabaseConfig()
            val logger = ConsoleLogger()
            DatabaseManager(config, logger)
        }
    }

    fun connect() {
        if (isConnected) {
            logger.info("Already connected to database")
            return
        }

        logger.info("Connecting to ${config.host}:${config.port}/${config.database}")
        logger.info("Setting up connection pool with ${config.maxConnections} connections")

        isConnected = true
        logger.info("Successfully connected!")
    }

    fun query(sql: String): List<String> {
        if (!isConnected) {
            logger.error("Cannot execute query - not connected!")
            return emptyList()
        }

        logger.debug("Executing: $sql")

        val results = listOf("Row 1: John Doe", "Row 2: Jane Smith", "Row 3: Bob Wilson")
        logger.info("Query returned ${results.size} rows")

        return results
    }

    fun disconnect() {
        if (!isConnected) {
            logger.info("Already disconnected")
            return
        }

        logger.info("Closing all connections...")
        isConnected = false
        logger.info("Disconnected successfully")
    }
}
