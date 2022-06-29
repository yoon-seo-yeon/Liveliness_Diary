package com.example.liveliness_diary;

//리사이클러뷰 아이템을 클릭했을 시 보이는 정보들

public class UserDiary {
    private String userId;          //사용자 아이디
    private String grapeProfile;    //포도 프로필
    private String diaryTitle;      //다이어리 제목
    private String diaryContent;    //다이어리 내용
    private String diaryImage;      //다이어리 사진
    private String todayDate;       //오늘 날짜
    private String textColor;       //다이어리 글씨 색상

    public UserDiary() {

    }

    public UserDiary(String userId, String grapeProfile, String diaryTitle, String diaryContent, String diaryImage,
                     String todayDate, String textColor) {
        this.userId = userId;
        this.grapeProfile = grapeProfile;
        this.diaryTitle = diaryTitle;
        this.diaryContent = diaryContent;
        this.diaryImage = diaryImage;
        this.todayDate = todayDate;
        this.textColor = textColor;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGrapeProfile() {
        return grapeProfile;
    }

    public void setGrapeProfile(String grapeProfile) {
        this.grapeProfile = grapeProfile;
    }

    public String getDiaryTitle() {
        return diaryTitle;
    }

    public void setDiaryTitle(String diaryTitle) {
        this.diaryTitle = diaryTitle;
    }

    public String getDiaryContent() {
        return diaryContent;
    }

    public void setDiaryContent(String diaryContent) {
        this.diaryContent = diaryContent;
    }

    public String getDiaryImage() {
        return diaryImage;
    }

    public void setDiaryImage(String diaryImage) {
        this.diaryImage = diaryImage;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
}
