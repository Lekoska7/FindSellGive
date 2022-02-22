package mk.com.findsellgive.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;


public class Product {
    private String id;
    private String imageUrl;
    private String name;
    private String description;
    private boolean condition;
    private int category;
    private int purpose;
    private String city;
    private double price;
    private String uid;
    private GeoPoint location;

    public Product() {
    }

    public Product(String id, String imageUrl, String name, String description, boolean condition, int category, int purpose, String city, double price, String uid, GeoPoint location) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.name = name;
        this.description = description;
        this.condition = condition;
        this.category = category;
        this.purpose = purpose;
        this.city = city;
        this.price = price;
        this.uid = uid;
        this.location = location;
    }

    //    //Nash konstruktor koj gi setira vrednostite od baza vo objektot
    public Product(Map<String, Object> data) {
        if (data != null) {
            if (data.containsKey("category") && data.get("category") != null) {
                this.category = ((Number) data.get("category")).intValue();
            } else {
                this.category = 0;
            }
            if (data.containsKey("imageUrl") && data.get("imageUrl") != null) {
                String imageUrl = (String) data.get("imageUrl");
                if (imageUrl != null) {
                    this.imageUrl = imageUrl;
                }
            } else {
                this.imageUrl = "";
            }
            if (data.containsKey("city") && data.get("city") != null) {
                String city = (String) data.get("city");
                if (city != null) {
                    this.city = city;
                }
            } else {
                this.city = "Struga";
            }
            if (data.containsKey("description") && data.get("description") != null) {
                String description = (String) data.get("description");
                if (description != null) {
                    this.description = description;
                }
            } else {
                this.description = "Nema opis";
            }
            if (data.containsKey("price") && data.get("price") != null) {
                Number p = (Number) data.get("price");
                this.price = p.doubleValue();
            } else {
                this.price = 0;
            }
            if (data.containsKey("purpose") && data.get("purpose") != null) {
                this.purpose = ((Number) data.get("purpose")).intValue();
            } else {
                this.purpose = 0;
            }
            if (data.containsKey("condition") && data.get("condition") != null) {
                this.condition = (boolean) data.get("condition");
            } else {
                this.condition = true;
            }
            if (data.containsKey("name") && data.get("name") != null) {
                this.name = (String) data.get("name");
            } else {
                this.name = "";
            }
            if (data.containsKey("uid") && data.get("uid") != null) {
                this.uid = (String) data.get("uid");
            } else {
                this.uid = "";
            }
            if (data.containsKey("id") && data.get("id") != null) {
                this.id = (String) data.get("id");
            } else {
                this.id = "";
            }
            if (data.containsKey("location") && data.get("location") != null) {
                this.location = (GeoPoint) data.get("location");
            } else {
                this.location = null;
            }
        }
    }

    public GeoPoint getLocation() {
        return location;
    }

    public Map<String, Object> getProductMap(Product product) {
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("category", product.getCategory());
        productMap.put("city", product.getCity());
        productMap.put("condition", product.isCondition());
        productMap.put("description", product.getDescription());
        productMap.put("imageUrl", product.getImageUrl());
        productMap.put("name", product.getName());
        productMap.put("price", product.getPrice());
        productMap.put("purpose", product.getPurpose());
        productMap.put("uid", product.getUid());
        productMap.put("id", product.getId());
        productMap.put("location", product.getLocation());
        return productMap;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }

    public String getCity() {
        return city;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCondition() {
        return condition;
    }

    public double getPrice() {
        return price;
    }

    public int getPurpose() {
        return purpose;
    }

    public int getCategory() {
        return category;
    }

    public String getProductCategory(int category) {
        switch (category) {
            case 0:
                return "Electronics";
            case 1:
                return "Housing";
            case 2:
                return "Entertainment";
            case 3:
                return "Other";
            default:
                return "Electronics";
        }
    }

    @Override
    public String toString() {
        return "Product{" +
                "imageUrl='" + imageUrl + '\'' +
                ", description='" + description + '\'' +
                ", condition='" + condition + '\'' +
                ", price=" + price +
                '}';
    }
}
