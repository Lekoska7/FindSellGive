package mk.com.findsellgive.models;


import java.util.HashMap;
import java.util.Map;


public class Rating {
    private float ratingOne;
    private float ratingTwo;
    private float ratingThree;
    private float ratingFour;
    private float ratingFive;

    public Rating() {
    }

    public Rating(float ratingOne, float ratingTwo, float ratingThree, float ratingFour, float ratingFive) {
        this.ratingOne = ratingOne;
        this.ratingTwo = ratingTwo;
        this.ratingThree = ratingThree;
        this.ratingFour = ratingFour;
        this.ratingFive = ratingFive;
    }

    public float getRatingOne() {
        return ratingOne;
    }

    public float getRatingTwo() {
        return ratingTwo;
    }

    public float getRatingThree() {
        return ratingThree;
    }

    public float getRatingFour() {
        return ratingFour;
    }

    public float getRatingFive() {
        return ratingFive;
    }

    public void addRating(float rating) {
        if(rating < 1){
           ratingOne++;
        } else if(rating>=1 && rating <1.5){
            ratingOne++;
        } else if(rating>=1.5 && rating < 2){
            ratingTwo++;
        } else if(rating >=2 && rating <2.5){
           ratingTwo++;
        } else if(rating >=2.5 && rating <3){
          ratingThree++;
        } else if(rating >=3 && rating <3.5){
           ratingThree++;
        } else if(rating>=3.5 && rating <4){
            ratingFour++;
        } else if(rating >=4 && rating <4.5){
          ratingFour++;
        } else if(rating>=4.5 && rating <5){
           ratingFive++;
        } else {
           ratingFive++;
        }
    }
    public float sum() {
        return ratingFive + ratingFour + ratingThree + ratingTwo + ratingOne;
    }

    public Map<String, Object> getMap(Rating rating) {
        Map<String, Object> map = new HashMap<>();
        map.put("ratingOne", rating.getRatingOne());
        map.put("ratingTwo", rating.getRatingTwo());
        map.put("ratingThree", rating.getRatingThree());
        map.put("ratingFour", rating.getRatingFour());
        map.put("ratingFive", rating.getRatingFive());
        return map;
    }
}
