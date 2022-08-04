package uz.nits.chatting.adapter;

public class ChatModel {

    String user1;
    String user2;
    String userTitle;
    String userName;
    String message;
    String time;

    public ChatModel(String user1, String user2, String userTitle, String userName, String message, String time) {
        this.user1 = user1;
        this.user2 = user2;
        this.userTitle = userTitle;
        this.userName = userName;
        this.message = message;
        this.time = time;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public String getUserTitle() {
        return userTitle;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}
