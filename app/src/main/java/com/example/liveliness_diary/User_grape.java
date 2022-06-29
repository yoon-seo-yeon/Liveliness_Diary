package com.example.liveliness_diary;

//사용자 포도 프로필
public class User_grape {
    private String mood_grape;  //사용자 포도 프로필
    private String today_date;  //오늘 날짜

    public User_grape(String mood_grape, String today_date) {
        this.mood_grape = mood_grape;
        this.today_date = today_date;
    }

    public String getMood_grape() {
        return mood_grape;
    }

    public void setMood_grape(String mood_grape) {
        this.mood_grape = mood_grape;
    }

    public String getToday_date() {
        return today_date;
    }

    public void setToday_date(String today_date) {
        this.today_date = today_date;
    }
}
