package com.example.liveliness_diary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//포도(프로필)정하기
public class Mood_grape_change extends AppCompatActivity {
    private Uri imageUri;
    private FirebaseStorage mStorage;   //스토리지
    private FirebaseDatabase mDatabase; //실시간 데이터베이스
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    ImageView mood_happy, mood_satis, mood_usual, mood_tired, mood_sad, mood_angry;
//    Button btn_moodgrade;

    String userId, userPass;
    String today_week;  //오늘 요일 담기
    String today_grape; //오늘 포도 담기

    ProgressDialog dialog;  //프로그레스바 보여주기
    ProgressBar progressBar;    //프로그레스바 원형

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_grape_change);
//        setContentView(R.layout.login);

        mood_happy = findViewById(R.id.mood_happy);
        mood_satis = findViewById(R.id.mood_satis);
        mood_usual = findViewById(R.id.mood_usual);
        mood_tired = findViewById(R.id.mood_tired);
        mood_sad = findViewById(R.id.mood_sad);
        mood_angry = findViewById(R.id.mood_angry);
//        btn_moodgrade = findViewById(R.id.btn_moodgrade);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(false);
        progressBar.setProgress(100);

        mStorage = FirebaseStorage.getInstance();   //스토리지
        storageReference = mStorage.getReference();

        mDatabase = FirebaseDatabase.getInstance(); //실시간 데이터베이스
        databaseReference = mDatabase.getReference();

        //Login.java에서 사용자의 아이디, 비밀번호 변수 받기
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        //현재시간 가져오기
        long now = System.currentTimeMillis();
        //Date 형식으로 Convert
        Date date = new Date(now);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);    //현재 요일 구하기

        switch (dayWeek) {
            case 1 :
                today_week = "Sun";
                //Sun = "Sun";
                break;

            case 2 :
                today_week = "Mon";
                //Mon = "Mon";
                break;

            case 3 :
                today_week = "Tue";
                //Tue = "Tue";
                break;

            case 4 :
                today_week = "Wed";
                //Wed = "Wed";
                break;

            case 5 :
                today_week = "Thu";
                //Thu = "Thu";
                break;

            case 6 :
                today_week = "Fri";
                //Fri = "Fri";
                break;

            case 7 :
                today_week = "Sat";
                //Sat = "Sat";
                break;
        }

        //내가 임의로 요일 설정
//        today_week = "Thu";

        //기쁨
        mood_happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_happy");
                today_grape = "happy";  //오늘의 감정 포도에 happy 담기
                uploadToFirebase(today_grape);
            }
        });

        //만족
        mood_satis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_satis");
                today_grape = "satis";
                uploadToFirebase(today_grape);
            }
        });

        //보통
        mood_usual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_usual");
                today_grape = "usual";
                uploadToFirebase(today_grape);
            }
        });

        //피곤
        mood_tired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_tired");
                today_grape = "tired";
                uploadToFirebase(today_grape);
            }
        });

        //슬픔
        mood_sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_sad");
                today_grape = "sad";
                uploadToFirebase(today_grape);
            }
        });

        //화남
        mood_angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUri = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_angry");
                today_grape = "angry";
                uploadToFirebase(today_grape);
            }
        });
    }

    //파이어베이스 통계 정보 저장
    private void uploadToFirebase(String mood_grape_text) {
//        dialog = new ProgressDialog(Mood_grape_change.this);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage("오늘의 프로필이 변경되고 있습니다!");
//        dialog.show();

        String grape = mood_grape_text;

        databaseReference.child("user_moodstats").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserStats stats = snapshot.getValue(UserStats.class);

                String Mon, Tue, Wed, Thu, Fri, Sat, Sun, id;
                String Mon_text, Tue_text, Wed_text, Thu_text, Fri_text, Sat_text, Sun_text;

                id = stats.getUserId();
                Mon = stats.getMon();
                Tue = stats.getTue();
                Wed = stats.getWed();
                Thu = stats.getThu();
                Fri = stats.getFri();
                Sat = stats.getSat();
                Sun = stats.getSun();

                Mon_text = stats.getMon_text();
                Tue_text = stats.getTue_text();
                Wed_text = stats.getWed_text();
                Thu_text = stats.getThu_text();
                Fri_text = stats.getFri_text();
                Sat_text = stats.getSat_text();
                Sun_text = stats.getSun_text();

                //값이 없다면 요일에 맞는 String값 넣어주기
                if(Mon.equals("")) {
                    Mon = "mon";
                }
                if(Tue.equals("")) {
                    Tue = "tue";
                }
                if(Wed.equals("")) {
                    Wed = "wed";
                }
                if(Thu.equals("")) {
                    Thu = "thu";
                }
                if(Fri.equals("")) {
                    Fri = "fri";
                }
                if(Sat.equals("")) {
                    Sat = "sat";
                }
                if(Sun.equals("")) {
                    Sun = "sun";
                }

                Log.d("TAG", "PICK grape_change : " + id);
                Log.d("TAG", "PICK 월m : " + Mon);
                Log.d("TAG", "PICK 화 : " + Tue);
                Log.d("TAG", "PICK 수 : " + Wed);
                Log.d("TAG", "PICK 목 : " + Thu);
                Log.d("TAG", "PICK 금 : " + Fri);
                Log.d("TAG", "PICK 토 : " + Sat);
                Log.d("TAG", "PICK 일 : " + Sun);

                if (today_week.equals("Mon")) {
                    Mon = grape;
                    Mon_text = grape;
                } else if (today_week.equals("Tue")) {
                    Tue = grape;
                    Tue_text = grape;
                } else if (today_week.equals("Wed")) {
                    Wed = grape;
                    Wed_text = grape;
                } else if (today_week.equals("Thu")) {
                    Thu = grape;
                    Thu_text = grape;
                } else if (today_week.equals("Fri")) {
                    Fri = grape;
                    Fri_text = mood_grape_text;
                } else if (today_week.equals("Sat")) {
                    Sat = grape;
                    Sat_text = grape;
                } else if (today_week.equals("Sun")) {
                    Sun = grape;
                    Sun_text = grape;
                }

                //통계 DB에 넣기
                UserStats stats2 = new UserStats(userId, Mon, Tue, Wed, Thu, Fri, Sat, Sun, Mon_text,
                        Tue_text, Wed_text, Thu_text, Fri_text, Sat_text, Sun_text);
                databaseReference.child("user_moodstats").child(userId).setValue(stats2);
                Log.d("TAG", "PICK user_moodstats : " + "끼야호호호");

                //현재시간 가져오기
                long now = System.currentTimeMillis();
                //Date 형식으로 Convert
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String getTime = sdf.format(date);

                // user_grape DB에 오늘 날짜와 선택한 포도 넣기
                User_grape user_grape = new User_grape(mood_grape_text, getTime);
                databaseReference.child("user_grape").child(userId).setValue(user_grape);
                Log.d("TAG", "PICK user_grape : " + "야랼로루루");

                Log.d("TAG", "PICK : " + "들어오냐?");

                //다이어리 리사이클러뷰로 가기
                Intent intent = new Intent(Mood_grape_change.this, MainActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);

                Log.d("TAG", "PICK : " + "들어옴");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}
