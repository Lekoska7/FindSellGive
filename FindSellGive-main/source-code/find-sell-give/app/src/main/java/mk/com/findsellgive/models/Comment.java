package mk.com.findsellgive.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Comment {
    private String uid;
    private String text;
    private String imageUrl;
    private Date datePosted;

    public Comment(String uid, String text, String imageUrl, Date datePosted) {
        this.uid = uid;
        this.text = text;
        this.imageUrl = imageUrl;
        this.datePosted = datePosted;
    }

    public Comment() {
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static Map<String, Object> getCommentMap(Comment comment) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid",comment.getUid());
        map.put("text",comment.getText());
        map.put("imageUrl",comment.getImageUrl());
        map.put("datePosted",comment.getDatePosted());
        return map;
    }

    public String getUid() {
        return uid;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Date getDatePosted() {
        return datePosted;
    }
}
