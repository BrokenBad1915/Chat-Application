package com.example.chatics;

public class userProfile {
    public String userUID;

    public userProfile() {
    }

    public userProfile(String userUID) {
        this.userUID = userUID;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }
}
