import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // =================================
        // INITIAL PRODUCT DATA
        // =================================

        Product p1 = new Product(
            "P101", "Laptop", "SELLER01", 45000, 10
        );

        Product p2 = new Product(
            "P102", "Headphones", "SELLER02", 1000, 20
        );

        Product p3 = new Product(
            "P103", "Mouse", "SELLER01", 300, 15
        );


        // =================================
        // CUSTOMER
        // =================================

        Customer customer = new Customer(
            "C101", "Arun"
        );


        // =================================
        // CART
        // =================================

        Cart cart = new Cart();

        cart.addItem(p1, 1);
        cart.addItem(p2, 2);
        cart.addItem(p3, 1);


        // =================================
        // PAYMENT AND ORDER SERVICE
        // =================================

        DemoPaymentGateway gateway =
            new DemoPaymentGateway();

        OrderService orderService =
            new OrderService(gateway);


        // =================================
        // MAIN MENU
        // =================================

        while (true) {

            System.out.println("\n================================");
            System.out.println("       E-COMMERCE SHOPPING");
            System.out.println("================================");

            System.out.println("1. View Cart");
            System.out.println("2. Place Order");
            System.out.println("3. View Order");
            System.out.println("4. Update Shipment Status");
            System.out.println("5. Cancel Order");
            System.out.println("6. View Stock");
            System.out.println("7. Add Demo Items to Cart");
            System.out.println("8. View All Orders");
            System.out.println("9. Test Payment Failure");
            System.out.println("10. Exit");

            System.out.print("\nEnter your choice: ");

            int choice = sc.nextInt();

            switch (choice) {

                // -------------------------
                // VIEW CART
                // -------------------------

                case 1:

                    cart.displayCart();

                    break;


                // -------------------------
                // PLACE ORDER
                // -------------------------

                case 2:

                    System.out.println("\nSelect Payment Method");

                    System.out.println("1. CARD");
                    System.out.println("2. UPI");
                    System.out.println("3. NET BANKING");
                    System.out.println("4. COD");

                    System.out.print("Enter choice: ");

                    int paymentChoice = sc.nextInt();

                    if (paymentChoice < 1 ||
                        paymentChoice > 4) {

                        System.out.println("Invalid payment method!");

                        break;
                    }

                    PaymentMethod method =
                        PaymentMethod.values()[paymentChoice - 1];

                    Order order = orderService.placeOrder(
                        customer, cart, method
                    );

                    if (order != null) {

                        orderService.viewOrder(
                            order.getOrderId()
                        );
                    }

                    break;


                // -------------------------
                // VIEW ORDER
                // -------------------------

                case 3:

                    System.out.print("Enter Order ID: ");

                    String orderId = sc.next();

                    orderService.viewOrder(orderId);

                    break;


                // -------------------------
                // UPDATE SHIPMENT
                // -------------------------

                case 4:

                    System.out.print("Enter Order ID: ");

                    String updateOrderId = sc.next();

                    System.out.print("Enter Shipment ID: ");

                    String shipmentId = sc.next();

                    System.out.println("\nSelect Next Status");

                    System.out.println("1. PACKED");
                    System.out.println("2. SHIPPED");
                    System.out.println("3. OUT FOR DELIVERY");
                    System.out.println("4. DELIVERED");

                    System.out.print("Enter choice: ");

                    int statusChoice = sc.nextInt();

                    if (statusChoice < 1 ||
                        statusChoice > 4) {

                        System.out.println("Invalid status!");

                        break;
                    }

                    ShipmentStatus status =
                        ShipmentStatus.values()[statusChoice];

                    orderService.updateShipmentStatus(
                        updateOrderId,
                        shipmentId,
                        status
                    );

                    break;


                // -------------------------
                // CANCEL ORDER
                // -------------------------

                case 5:

                    System.out.print("Enter Order ID: ");

                    String cancelId = sc.next();

                    orderService.cancelOrder(cancelId);

                    break;


                // -------------------------
                // VIEW STOCK
                // -------------------------

                case 6:

                    System.out.println("\nPRODUCT STOCK DETAILS");

                    p1.displayProduct();

                    System.out.println("----------------");

                    p2.displayProduct();

                    System.out.println("----------------");

                    p3.displayProduct();

                    break;


                // -------------------------
                // ADD ITEMS AGAIN
                // -------------------------

                case 7:

                    if (!cart.isEmpty()) {

                        System.out.println(
                            "Cart already contains items!"
                        );

                        break;
                    }

                    cart.addItem(p1, 1);
                    cart.addItem(p2, 2);
                    cart.addItem(p3, 1);

                    System.out.println(
                        "Demo items added successfully!"
                    );

                    break;


                // -------------------------
                // VIEW ALL ORDERS
                // -------------------------

                case 8:

                    orderService.viewAllOrders();

                    break;


                // -------------------------
                // TEST PAYMENT FAILURE
                // -------------------------

                case 9:

                    gateway.setPaymentSuccess(false);

                    System.out.println(
                        "Demo payment failure enabled."
                    );

                    break;


                // -------------------------
                // EXIT
                // -------------------------

                case 10:

                    System.out.println(
                        "Thank you for using the application!"
                    );

                    sc.close();

                    return;


                default:

                    System.out.println("Invalid choice!");
            }
        }
    }
}