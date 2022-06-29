package com.example.liveliness_diary;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

//다이어리 리스트 클릭
public class DiaryClick extends AppCompatActivity {
    private String date;    //날짜
    private String userId;  //사용자 아이디

    private FirebaseStorage mStorage;   //스토리지
    private FirebaseDatabase mDatabase; //실시간 데이터베이스
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    ImageView user_profile, diary_image; //사용자 포도 프로필, 다이어리 이미지
    TextView diary_title, diary_date, diary_content, today_color;    //다이어리 제목, 날짜, 내용, 색상

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_click);

        mStorage = FirebaseStorage.getInstance();   //스토리지
        storageReference = mStorage.getReference();

        mDatabase = FirebaseDatabase.getInstance(); //실시간 데이터베이스
        databaseReference = mDatabase.getReference();

        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        userId = intent.getStringExtra("userId");

        user_profile = findViewById(R.id.user_profile);
        diary_title = findViewById(R.id.diary_title);
        diary_content = findViewById(R.id.diary_content);
        diary_date = findViewById(R.id.diary_date);
        diary_image = findViewById(R.id.diary_image);
        today_color = findViewById(R.id.today_color);

//        Toast.makeText(this, "date : " + date, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "userId : " + userId, Toast.LENGTH_SHORT).show();

        databaseReference.child("diary_list").child(userId).child(date).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserDiary userDiary = snapshot.getValue(UserDiary.class);

                Log.d("TAG", "PICK : grapeProfile : " + userDiary.getGrapeProfile());
                Log.d("TAG", "PICK : diaryImage : " + userDiary.getDiaryImage());
                Log.d("TAG", "PICK : diaryTitle : " + userDiary.getDiaryTitle());
                Log.d("TAG", "PICK : diaryContent : " + userDiary.getDiaryContent());
                Log.d("TAG", "PICK : textcolor : " + userDiary.getTextColor());
                Log.d("TAG", "PICK : todaydate : " + userDiary.getTodayDate());

                Glide.with(DiaryClick.this).load(userDiary.getGrapeProfile()).into(user_profile);
                diary_title.setText(userDiary.getDiaryTitle());
                diary_date.setText(userDiary.getTodayDate());

                String text_color = userDiary.getTextColor();
//                Log.d("TAG", "PICK text_color : " + text_color);

                today_color.setText(userDiary.getTextColor());
                today_color.setTextColor(Color.parseColor(text_color));

                diary_content.setText(userDiary.getDiaryContent());
                diary_content.setTextColor(Color.parseColor(text_color));

//                Log.d("TAG", "PICK diary_image : " + userDiary.getDiaryImage());

                Glide.with(DiaryClick.this).load(userDiary.getDiaryImage()).into(diary_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
