package mk.com.findsellgive.models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Message {
    private String from;
    private String to;
    private String message;
    private Date date;

    public Message() {
    }
    public Map<String,Object> getMap(Message message){
        Map<String,Object> map = new HashMap<>();
        map.put("from",message.getFrom());
        map.put("to",message.getTo());
        map.put("message",message.getMessage());
        map.put("date",message.getDate());
        return map;
    }
    public Message(String from, String to, String message, Date date) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", message='" + message + '\'' +
                ", date=" + date +
                '}';
    }
}
