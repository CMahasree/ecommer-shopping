import java.util.List;
import java.util.ArrayList;

public class Shipment {

    private String shipmentId;
    private String sellerId;
    private List<OrderItem> items;
    private ShipmentStatus status;

    public Shipment(String shipmentId, String sellerId,
                    List<OrderItem> items) {

        this.shipmentId = shipmentId;
        this.sellerId = sellerId;
        this.items = new ArrayList<>(items);

        this.status = ShipmentStatus.PLACED;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public boolean updateStatus(ShipmentStatus nextStatus) {

        if (nextStatus.ordinal() == status.ordinal() + 1) {

            status = nextStatus;

            return true;
        }

        System.out.println(
            "Invalid status! Cannot skip delivery stages."
        );

        return false;
    }
}