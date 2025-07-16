package model;

public class Supplier {
    public String id;
    public String name;
    public String contact;
    public String location;
    public int deliveryTime;

    public Supplier(String id, String name, String contact, String location, int deliveryTime) {
        this.id = id;
        this.name = name;
        this.contact = contact;
        this.location = location;
        this.deliveryTime = deliveryTime;
    }
}
