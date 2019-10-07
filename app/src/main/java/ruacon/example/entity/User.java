package ruacon.example.entity;

/**
 * Created by Nguyen Tien Hoang on 12/13/2016.
 */

public class User {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}