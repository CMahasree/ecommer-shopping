public class OrderItem {

    private Product product;
    private String productName;
    private int quantity;
    private double orderedPrice;

    public OrderItem(Product product, int quantity) {

        this.product = product;
        this.productName = product.getProductName();
        this.quantity = quantity;

        // Price snapshot
        this.orderedPrice = product.getPrice();
    }

    public Product getProduct() {
        return product;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getOrderedPrice() {
        return orderedPrice;
    }

    public double getSubtotal() {
        return orderedPrice * quantity;
    }
}