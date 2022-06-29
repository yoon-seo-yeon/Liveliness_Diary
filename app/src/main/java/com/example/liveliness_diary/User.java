package com.example.liveliness_diary;

//사용자 정보 저장

public class User {
    private String userId;  //사용자 아이디
    private String userPass;    //사용자 비밀번호
    private String userProfile; //사용자 프로필 사진

    public User() {

    }

    public User(String userId, String userPass, String userProfile) {
        this.userId = userId;
        this.userPass = userPass;
        this.userProfile = userProfile;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }
}
