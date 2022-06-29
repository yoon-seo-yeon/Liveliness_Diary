package com.example.liveliness_diary;

//사용자가 선택한 두가지 색
public class User_mood {
    private String mood_color;  //사용자가 선택한 두 가지 색 혼합
    private String today_date;  //오늘 날짜 

    public User_mood(String mood_color, String today_date) {
        this.mood_color = mood_color;
        this.today_date = today_date;
    }

    public String getMood_color() {
        return mood_color;
    }

    public void setMood_color(String mood_color) {
        this.mood_color = mood_color;
    }

    public String getToday_date() {
        return today_date;
    }

    public void setToday_date(String today_date) {
        this.today_date = today_date;
    }
}
