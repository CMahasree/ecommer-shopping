import java.util.ArrayList;
import java.util.List;

public class Cart {

    private List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, int quantity) {

        if (product == null || quantity <= 0) {
            System.out.println("Invalid cart item!");
            return;
        }

        items.add(new CartItem(product, quantity));
    }

    public List<CartItem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clearCart() {
        items.clear();
    }

    public void displayCart() {

        for (CartItem item : items) {

            System.out.println(
                item.getProduct().getProductName()
                + " | Quantity: " + item.getQuantity()
            );
        }
    }
}