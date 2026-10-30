package model;

public class Seller extends User {

    private String sellerId;

    public Seller(int userId, String name, String email, String phone, String sellerId) {
        super(userId, name, email, phone);
        this.sellerId = sellerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}