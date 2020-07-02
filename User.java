package com.example.inventorymanager;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String name; // user name
    public String authority; // user authority
    public String business;  // user business(company)
    public String email; //user email
    public String id; //user id
    public Boolean approval;  //whether user is approval or not
    public String password;  //user password

    public User() {
    }

    public User(String name, String authority, String business, String email, String id, String password) {
        this.name = name;
        this.authority = authority;
        this.business = business;
        this.email = email;
        this.id = id;
        this.password = password;
        approval = true;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("email", email);
        result.put("pw", password);
        result.put("approval", approval);
        result.put("business", business);
        result.put("name", name);
        result.put("authority",authority);

        return result;
    }
    private String getId(){
        return id;
    }

}