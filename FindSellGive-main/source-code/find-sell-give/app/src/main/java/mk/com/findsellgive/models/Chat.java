package mk.com.findsellgive.models;

import java.util.HashMap;
import java.util.Map;


public class Chat {
    private String chatName;//so kogo razgovaram
    private Message lastMessage;

    public Chat(String chatName, Message lastMessage) {
        this.chatName = chatName;
        this.lastMessage = lastMessage;
    }

    public Chat() {
    }

    public Map<String, Object> getChatMap(Chat chat) {
        Map<String, Object> chatMap = new HashMap<>();
        Message lastMessage = chat.getLastMessage();
        chatMap.put("chatName", chat.getChatName());
        chatMap.put("lastMessage", lastMessage.getMap(lastMessage));
        return chatMap;
    }
    public String getChatName() {
        return chatName;
    }

    public Message getLastMessage() {
        return lastMessage;
    }


    @Override
    public String toString() {
        return "chat = {" +
                "" +getChatName()+"\n"+
                "" +getLastMessage().toString()+"\n"+
                "}";
    }
}
