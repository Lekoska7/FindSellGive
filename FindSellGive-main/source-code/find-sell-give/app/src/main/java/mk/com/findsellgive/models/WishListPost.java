package mk.com.findsellgive.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class WishListPost {
    private String uid;
    private String question;
    private int category;
    private int commentsCount;
    private Date datePosted;
    private String id;
    private GeoPoint location;

    public WishListPost() {
    }

    public WishListPost(String uid, String question, int category, int commentsCount, Date datePosted, String id, GeoPoint location) {
        this.uid = uid;
        this.question = question;
        this.category = category;
        this.commentsCount = commentsCount;
        this.datePosted = datePosted;
        this.id = id;
        this.location = location;
    }

    public Map<String, Object> getWishListPostMap(WishListPost wishListPost) {
        Map<String, Object> wishListPostMap = new HashMap<>();
        wishListPostMap.put("uid", wishListPost.getUid());
        wishListPostMap.put("id",wishListPost.getId());
        wishListPostMap.put("question", wishListPost.getQuestion());
        wishListPostMap.put("category", wishListPost.getCategory());
        wishListPostMap.put("datePosted", wishListPost.getDatePosted());
        wishListPostMap.put("commentsCount", wishListPost.getCommentsCount());
        wishListPostMap.put("location",wishListPost.getLocation());
        return wishListPostMap;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public String getUid() {
        return uid;
    }

    public String getQuestion() {
        return question;
    }

    public int getCategory() {
        return category;
    }

    public Date getDatePosted() {
        return datePosted;
    }
}
