import java.math.BigDecimal

fun main() {
    val paymentProcessor = PaymentFacade()

    paymentProcessor.processPayment(
        BigDecimal("123.44"),
        "**** **** **** 1234",
    )
}
