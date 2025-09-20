import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Facade pattern for providing a payment processor API
 * 
 * Facade provides a unified interface to a set of interfaces in
 * a subsystem. Allowing the structuring of a complex system into
 * subsystems.
 * 
 * ## Trade-offs:
 * Pros: Shield clients from subsystem components. Promotes
 * weak coupling between system and client. Doesn't prevent
 * application from using subsystem classes. Can centralize
 * orchestration logic and error handling in one place.
 * 
 * Cons: Can become a god object. Can be difficult to debug.
 * 
 * ## Kotlin-specific nice things:
 * - Extension functions can add facade methods to existing classes
 * - Sealed classes work well for representing operation results
 * - Data classes make it easy to pass structured data between subsystems
 * 
 * ## Note:
 * - Keep facades focused on coordination, not business logic
 * - Consider breaking large facades into smaller ones
 * - Data flow benefits: single entry point for complex multi-step operations
 */
class PaymentFacade {
    private val cardValidator = CardValidationService()
    private val fraudDetector = FraudDetectionSystem()
    private val transactionDb = TransactionDatabase()
    private val notificationService = NotificationService()
    private val auditLogger = AuditLogger()

    fun processPayment(
        amount: BigDecimal,
        cardInfo: String
    ) {
        cardValidator.validateCard()
        fraudDetector.detectFraud()
        transactionDb.storeTransaction(transId = 0)
        notificationService.notifyUser()
        auditLogger.log()
        println("processed payment of amount: $$amount for card: $cardInfo")
    }

    fun refundPayment(
        transId: Int
    ) {
        transactionDb.storeTransaction(transId)
        notificationService.notifyUser()
        auditLogger.log()
        println("refunded payment #$transId")
    }
}

class CardValidationService {
    fun validateCard() {
        println("Yep, that's valid.")
    }
}

class FraudDetectionSystem {
    fun detectFraud() {
        println("Clean as a whistle, no fraud detected!")
    }
}

class TransactionDatabase {
    fun storeTransaction(transId: Int) {
        println("Yeah, definitely storing $transId somewhere.")
    }
}

class NotificationService {
    fun notifyUser() {
        println("We did the thing you told us to do!")
        println("You know? the thing")
    }
}

class AuditLogger {
    fun log() {
        println("Logged")
    }
}
