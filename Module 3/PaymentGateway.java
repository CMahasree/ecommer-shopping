public interface PaymentGateway {

    PaymentStatus processPayment(
        double amount,
        PaymentMethod method
    );

    void refundPayment(double amount);
}