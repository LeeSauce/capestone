package org.example.capestone_group_02.gabes_code;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    // todo : add one for user images, probably a list

    public User(int id, String username, String password, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
