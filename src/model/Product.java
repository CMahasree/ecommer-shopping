package model;

public class Product {

    private int productId;
    private String name;
    private Category category;
    private Seller seller;
    private double price;
    private int stockQuantity;
    private boolean active;

    public Product(int productId, String name, Category category,
                   Seller seller, double price, int stockQuantity) {

        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }

        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }

        this.productId = productId;
        this.name = name;
        this.category = category;
        this.seller = seller;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.active = true;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Seller getSeller() {
        return seller;
    }

    public double getPrice() {
        return price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public boolean isActive() {
        return active;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        this.price = price;
    }

    public void setStockQuantity(int stockQuantity) {
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stockQuantity = stockQuantity;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isOutOfStock() {
        return stockQuantity == 0;
    }
}