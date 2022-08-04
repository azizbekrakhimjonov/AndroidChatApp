package uz.nits.chatting;

public class User {
    String phone;
    String userName;

    public User(String phone, String userName) {
        this.phone = phone;
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public String getUserName() {
        return userName;
    }
}
