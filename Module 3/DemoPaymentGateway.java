public class DemoPaymentGateway implements PaymentGateway {

    private boolean paymentSuccess = true;

    public void setPaymentSuccess(boolean success) {
        paymentSuccess = success;
    }

    @Override
    public PaymentStatus processPayment(
            double amount,
            PaymentMethod method) {

        System.out.println("\nProcessing Payment...");
        System.out.println("Method: " + method);
        System.out.println("Amount: Rs. " + amount);

        if (paymentSuccess) {

            System.out.println("Payment Successful!");

            return PaymentStatus.PAID;
        }

        System.out.println("Payment Failed!");

        return PaymentStatus.FAILED;
    }

    @Override
    public void refundPayment(double amount) {

        System.out.println(
            "Refund initiated: Rs. " + amount
        );
    }
}