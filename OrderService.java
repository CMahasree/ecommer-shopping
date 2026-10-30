import java.util.*;

public class OrderService {

    // Store all orders dynamically
    private Map<String, Order> orders = new LinkedHashMap<>();

    private int orderCounter = 1001;
    private int shipmentCounter = 501;

    private PaymentGateway paymentGateway;

    public OrderService(PaymentGateway paymentGateway) {
        this.paymentGateway = paymentGateway;
    }


    // ==================================
    // 1. PLACE ORDER
    // ==================================

    public Order placeOrder(
            Customer customer,
            Cart cart,
            PaymentMethod method) {

        if (cart == null || cart.isEmpty()) {

            System.out.println("Cart is empty!");

            return null;
        }

        // STEP 1: Validate all product stocks

        for (CartItem item : cart.getItems()) {

            Product product = item.getProduct();

            int quantity = item.getQuantity();

            if (quantity <= 0 ||
                quantity > product.getStock()) {

                System.out.println(
                    "Order rejected! Out of stock: "
                    + product.getProductName()
                );

                return null;
            }
        }


        // STEP 2: Create order item snapshots

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cart.getItems()) {

            OrderItem orderItem = new OrderItem(
                item.getProduct(),
                item.getQuantity()
            );

            orderItems.add(orderItem);
        }


        // STEP 3: Calculate item total

        double itemTotal = 0;

        for (OrderItem item : orderItems) {

            itemTotal += item.getSubtotal();
        }


        // STEP 4: Calculate delivery charge

        double deliveryCharge =
            itemTotal < 500 ? 50 : 0;

        double grandTotal = itemTotal + deliveryCharge;


        // STEP 5: Validate COD

        if (method == PaymentMethod.COD &&
            grandTotal > 5000) {

            System.out.println(
                "COD not allowed above Rs. 5000!"
            );

            return null;
        }


        // STEP 6: Payment processing

        PaymentStatus paymentStatus;

        if (method == PaymentMethod.COD) {

            paymentStatus = PaymentStatus.PENDING;

        } else {

            paymentStatus =
                paymentGateway.processPayment(
                    grandTotal, method
                );

            if (paymentStatus != PaymentStatus.PAID) {

                System.out.println(
                    "Payment failed. Order rejected!"
                );

                return null;
            }
        }


        // STEP 7: Reduce stock only after validation
        // and successful online payment

        for (CartItem item : cart.getItems()) {

            boolean reduced =
                item.getProduct().reduceStock(
                    item.getQuantity()
                );

            if (!reduced) {

                System.out.println(
                    "Stock update failed!"
                );

                return null;
            }
        }


        // STEP 8: Create order

        String orderId = "ORD" + orderCounter++;

        Order order = new Order(
            orderId,
            customer,
            orderItems,
            method,
            paymentStatus
        );


        // STEP 9: Group items by seller

        Map<String, List<OrderItem>> sellerItems =
            new LinkedHashMap<>();

        for (OrderItem item : orderItems) {

            String sellerId =
                item.getProduct().getSellerId();

            sellerItems.putIfAbsent(
                sellerId,
                new ArrayList<>()
            );

            sellerItems.get(sellerId).add(item);
        }


        // STEP 10: Create separate shipment
        // for each seller

        for (Map.Entry<String, List<OrderItem>> entry
                : sellerItems.entrySet()) {

            String shipmentId =
                "SHP" + shipmentCounter++;

            Shipment shipment = new Shipment(
                shipmentId,
                entry.getKey(),
                entry.getValue()
            );

            order.addShipment(shipment);
        }


        // STEP 11: Store order

        orders.put(orderId, order);

        // Clear cart after successful order
        cart.clearCart();

        System.out.println(
            "\nOrder placed successfully!"
        );

        System.out.println(
            "Order ID: " + orderId
        );

        System.out.println(
            "Total Amount: Rs. " + grandTotal
        );

        return order;
    }


    // ==================================
    // 2. UPDATE SHIPMENT STATUS
    // ==================================

    public void updateShipmentStatus(
            String orderId,
            String shipmentId,
            ShipmentStatus nextStatus) {

        Order order = orders.get(orderId);

        if (order == null) {

            System.out.println("Order not found!");

            return;
        }

        if (order.isCancelled()) {

            System.out.println("Order is cancelled!");

            return;
        }

        for (Shipment shipment : order.getShipments()) {

            if (shipment.getShipmentId().equals(shipmentId)) {

                boolean updated =
                    shipment.updateStatus(nextStatus);

                if (!updated) {
                    return;
                }

                System.out.println(
                    "Shipment status updated!"
                );

                System.out.println(
                    "Shipment Status: " + shipment.getStatus()
                );

                System.out.println(
                    "Overall Order Status: " + order.getStatus()
                );

                // COD payment collected after
                // every shipment is delivered

                if (order.getStatus() == OrderStatus.DELIVERED
                    && order.getPaymentMethod() == PaymentMethod.COD
                    && order.getPaymentStatus() == PaymentStatus.PENDING) {

                    order.setPaymentStatus(PaymentStatus.PAID);

                    System.out.println(
                        "COD payment collected successfully!"
                    );
                }

                return;
            }
        }

        System.out.println("Shipment not found!");
    }


    // ==================================
    // 3. CANCEL ORDER
    // ==================================

    public void cancelOrder(String orderId) {

        Order order = orders.get(orderId);

        if (order == null) {

            System.out.println("Order not found!");

            return;
        }

        if (order.isCancelled()) {

            System.out.println("Order already cancelled!");

            return;
        }


        // Cannot cancel if any shipment is shipped

        for (Shipment shipment : order.getShipments()) {

            if (shipment.getStatus().ordinal()
                    >= ShipmentStatus.SHIPPED.ordinal()) {

                System.out.println(
                    "Cancellation rejected! "
                    + "Order has already shipped."
                );

                return;
            }
        }


        // Restore stock

        for (OrderItem item : order.getItems()) {

            item.getProduct().restoreStock(
                item.getQuantity()
            );
        }


        // Refund prepaid payment

        if (order.getPaymentStatus() == PaymentStatus.PAID) {

            paymentGateway.refundPayment(
                order.getTotalAmount()
            );

            order.setPaymentStatus(
                PaymentStatus.REFUNDED
            );
        }


        // Keep order record for history

        order.setCancelled(true);

        System.out.println(
            "Order cancelled successfully!"
        );

        System.out.println(
            "Stock restored successfully!"
        );
    }


    // ==================================
    // 4. VIEW ORDER DETAILS
    // ==================================

    public void viewOrder(String orderId) {

        Order order = orders.get(orderId);

        if (order == null) {

            System.out.println("Order not found!");

            return;
        }

        System.out.println("\n========== ORDER DETAILS ==========");

        System.out.println(
            "Order ID: " + order.getOrderId()
        );

        System.out.println(
            "Customer: " + order.getCustomer().getCustomerName()
        );

        System.out.println(
            "Order Status: " + order.getStatus()
        );

        System.out.println(
            "Payment Method: " + order.getPaymentMethod()
        );

        System.out.println(
            "Payment Status: " + order.getPaymentStatus()
        );

        System.out.println("\n---------- PRODUCTS ----------");

        for (OrderItem item : order.getItems()) {

            System.out.println(
                item.getProductName()
                + " | Quantity: " + item.getQuantity()
                + " | Price: Rs. " + item.getOrderedPrice()
                + " | Subtotal: Rs. " + item.getSubtotal()
            );
        }

        System.out.println("\n---------- BILL ----------");

        System.out.println(
            "Item Total: Rs. " + order.getItemTotal()
        );

        System.out.println(
            "Delivery Charge: Rs. " + order.getDeliveryCharge()
        );

        System.out.println(
            "Grand Total: Rs. " + order.getTotalAmount()
        );

        System.out.println("\n---------- SHIPMENTS ----------");

        for (Shipment shipment : order.getShipments()) {

            System.out.println(
                "Shipment ID: " + shipment.getShipmentId()
            );

            System.out.println(
                "Seller ID: " + shipment.getSellerId()
            );

            System.out.println(
                "Shipment Status: " + shipment.getStatus()
            );

            System.out.println("------------------------------");
        }
    }


    // ==================================
    // 5. VIEW ALL ORDERS
    // ==================================

    public void viewAllOrders() {

        if (orders.isEmpty()) {

            System.out.println("No orders available!");

            return;
        }

        for (Order order : orders.values()) {

            System.out.println(
                order.getOrderId()
                + " | Customer: "
                + order.getCustomer().getCustomerName()
                + " | Status: "
                + order.getStatus()
            );
        }
    }
}