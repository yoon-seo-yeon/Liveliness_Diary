package com.example.liveliness_diary;

//사용자 통계 정보 저장
public class UserStats {
    private String userId;  //사용자 아이디
    private String mon;     //월
    private String tue;     //화
    private String wed;     //수
    private String thu;     //목
    private String fri;     //금
    private String sat;     //토
    private String sun;     //일

    private String mon_text;     //월
    private String tue_text;     //화
    private String wed_text;     //수
    private String thu_text;     //목
    private String fri_text;     //금
    private String sat_text;     //토
    private String sun_text;     //일

    public UserStats() {

    }

    public UserStats(String userId, String mon, String tue, String wed,
                     String thu, String fri, String sat, String sun,
                     String mon_text, String tue_text, String wed_text, String thu_text,
                     String fri_text, String sat_text, String sun_text) {

        this.userId = userId;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;

        this.mon_text = mon_text;
        this.tue_text = tue_text;
        this.wed_text = wed_text;
        this.thu_text = thu_text;
        this.fri_text = fri_text;
        this.sat_text = sat_text;
        this.sun_text = sun_text;
    }

    public String getMon_text() {
        return mon_text;
    }

    public void setMon_text(String mon_text) {
        this.mon_text = mon_text;
    }

    public String getTue_text() {
        return tue_text;
    }

    public void setTue_text(String tue_text) {
        this.tue_text = tue_text;
    }

    public String getWed_text() {
        return wed_text;
    }

    public void setWed_text(String wed_text) {
        this.wed_text = wed_text;
    }

    public String getThu_text() {
        return thu_text;
    }

    public void setThu_text(String thu_text) {
        this.thu_text = thu_text;
    }

    public String getFri_text() {
        return fri_text;
    }

    public void setFri_text(String fri_text) {
        this.fri_text = fri_text;
    }

    public String getSat_text() {
        return sat_text;
    }

    public void setSat_text(String sat_text) {
        this.sat_text = sat_text;
    }

    public String getSun_text() {
        return sun_text;
    }

    public void setSun_text(String sun_text) {
        this.sun_text = sun_text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMon() {
        return mon;
    }

    public void setMon(String mon) {
        this.mon = mon;
    }

    public String getTue() {
        return tue;
    }

    public void setTue(String tue) {
        this.tue = tue;
    }

    public String getWed() {
        return wed;
    }

    public void setWed(String wed) {
        this.wed = wed;
    }

    public String getThu() {
        return thu;
    }

    public void setThu(String thu) {
        this.thu = thu;
    }

    public String getFri() {
        return fri;
    }

    public void setFri(String fri) {
        this.fri = fri;
    }

    public String getSat() {
        return sat;
    }

    public void setSat(String sat) {
        this.sat = sat;
    }

    public String getSun() {
        return sun;
    }

    public void setSun(String sun) {
        this.sun = sun;
    }
}
