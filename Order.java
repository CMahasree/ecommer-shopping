import java.util.ArrayList;
import java.util.List;

public class Order {

    private String orderId;
    private Customer customer;

    private List<OrderItem> items;
    private List<Shipment> shipments;

    private double itemTotal;
    private double deliveryCharge;
    private double totalAmount;

    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    private boolean cancelled;

    public Order(String orderId, Customer customer,
                 List<OrderItem> items,
                 PaymentMethod paymentMethod,
                 PaymentStatus paymentStatus) {

        this.orderId = orderId;
        this.customer = customer;

        this.items = new ArrayList<>(items);
        this.shipments = new ArrayList<>();

        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;

        this.cancelled = false;

        calculateTotal();
    }

    private void calculateTotal() {

        itemTotal = 0;

        for (OrderItem item : items) {
            itemTotal += item.getSubtotal();
        }

        deliveryCharge = itemTotal < 500 ? 50 : 0;

        totalAmount = itemTotal + deliveryCharge;
    }

    public void addShipment(Shipment shipment) {
        shipments.add(shipment);
    }

    public String getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public List<Shipment> getShipments() {
        return shipments;
    }

    public double getItemTotal() {
        return itemTotal;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus status) {
        paymentStatus = status;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    // Calculate overall order status
    public OrderStatus getStatus() {

        if (cancelled) {
            return OrderStatus.CANCELLED;
        }

        if (shipments.isEmpty()) {
            return OrderStatus.PLACED;
        }

        int minimumProgress = 4;

        for (Shipment shipment : shipments) {

            minimumProgress = Math.min(
                minimumProgress,
                shipment.getStatus().ordinal()
            );
        }

        return OrderStatus.values()[minimumProgress];
    }
}