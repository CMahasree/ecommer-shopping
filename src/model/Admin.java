package model;

public class Admin extends User {

    private String adminId;

    public Admin(int userId, String name, String email, String phone, String adminId) {
        super(userId, name, email, phone);
        this.adminId = adminId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }
}