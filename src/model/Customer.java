package model;

public class Customer extends User {

    private String address;

    public Customer(int userId, String name, String email, String phone, String address) {
        super(userId, name, email, phone);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}