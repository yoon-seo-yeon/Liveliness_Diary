package com.example.liveliness_diary;

//리사이블러뷰로 보이는 다이어리 정보들

public class UserDiaryView {
    private String userId;
    private String grapeProfile;    //포도 프로필
    private String diaryTitle;      //다이어리 제목
    private String diaryContent;    //다이어리 내용
    private String todayDate;       //오늘 날짜

    public UserDiaryView() {

    }

    public UserDiaryView(String userId, String grapeProfile, String diaryTitle, String diaryContent, String todayDate) {
        this.userId = userId;
        this.grapeProfile = grapeProfile;
        this.diaryTitle = diaryTitle;
        this.diaryContent = diaryContent;
        this.todayDate = todayDate;
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

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }
}
