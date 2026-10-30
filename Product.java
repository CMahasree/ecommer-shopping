public class Product {

    private String productId;
    private String productName;
    private String sellerId;
    private double price;
    private int stock;

    public Product(String productId, String productName,
                   String sellerId, double price, int stock) {

        this.productId = productId;
        this.productName = productName;
        this.sellerId = sellerId;
        this.price = price;
        this.stock = stock;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public boolean reduceStock(int quantity) {

        if (quantity <= 0 || quantity > stock) {
            return false;
        }

        stock -= quantity;
        return true;
    }

    public void restoreStock(int quantity) {

        if (quantity > 0) {
            stock += quantity;
        }
    }

    public void displayProduct() {

        System.out.println("Product ID: " + productId);
        System.out.println("Product: " + productName);
        System.out.println("Price: Rs. " + price);
        System.out.println("Stock: " + stock);
    }
}