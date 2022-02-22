package mk.com.findsellgive.models;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;


public class User {
    private String uid;
    private String fullName;
    private String email;
    private String password;
    private String profileImage;
    private Rating rating;
    private GeoPoint location;

    public User() {
    }

    public User(String uid, String fullName, String email, String password, String profileImage, Rating rating, GeoPoint location) {
        this.uid = uid;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.rating = rating;
        this.location = location;
    }

    public String getUid() {
        return uid;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Rating getRating() {
        return rating;
    }

    public Map<String, Object> getMap(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", user.getUid());
        map.put("fullName", user.getFullName());
        map.put("email", user.getEmail());
        map.put("password", user.getPassword());
        map.put("profileImage", user.getProfileImage());
        Rating rating = user.getRating();
        map.put("rating", rating.getMap(rating));
        map.put("location", user.getLocation());
        return map;
    }

    public Location getUserLocation() {
        Location location = new Location("");
        location.setLatitude(getLocation().getLatitude());
        location.setLongitude(getLocation().getLongitude());
        return location;
    }

    public GeoPoint getLocation() {
        return location;
    }
}

