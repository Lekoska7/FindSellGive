package mk.com.findsellgive.models;

import java.util.HashMap;
import java.util.Map;


public class RatedUser {
    private String uid;
    private String name;
    private boolean isRated;
    private Rating rating;

    public RatedUser() {
    }

    public RatedUser(String uid, String name, boolean isRated, Rating rating) {
        this.uid = uid;
        this.name = name;
        this.isRated = isRated;
        this.rating = rating;
    }

    public Map<String, Object> getMap(RatedUser ratedUser) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", ratedUser.getUid());
        map.put("name", ratedUser.getName());
        map.put("isRated", ratedUser.isRated());
        Rating rating = ratedUser.getRating();
        map.put("rating", rating.getMap(rating));
        return map;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public boolean isRated() {
        return isRated;
    }

    public Rating getRating() {
        return rating;
    }
}
