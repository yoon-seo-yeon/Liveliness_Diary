package com.example.liveliness_diary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

//로그인
public class Login extends AppCompatActivity {
    private static final int PICK_FROM_ALBUM = 1; //앨범에서 사진 가져오기
    private Uri imageUri;
    private String pathUri;
    private File tempFile;
    private FirebaseDatabase mDatabase; //실시간 데이터베이스
    private FirebaseStorage mStorage;   //스토리지
    //DatabaseReference는 데이터베이스의 특정 위치로 연결
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    EditText loginId, loginPass;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginId = findViewById(R.id.loginId);//사용자 아이디
        loginPass = findViewById(R.id.loginPass);    //사용자 비밀번호
        loginBtn = findViewById(R.id.loginBtn);    //로그인 버튼

        //Database, Storage 초기화
        //getInstance : 싱글톤 방식으로 객체 관리, 안의 value를 관리하고 싶으면 이용
        mDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance();
        databaseReference = mDatabase.getReference();
        storageReference = FirebaseStorage.getInstance().getReference();

        loginBtn.setOnClickListener(new View.OnClickListener() { //회원가입 버튼을 누르면
            @Override
            public void onClick(View view) {
//                Toast.makeText(Login.this, "들어옴", Toast.LENGTH_SHORT).show();
                String Id = loginId.getText().toString();
                String Pass = loginPass.getText().toString();

                if (TextUtils.isEmpty(Id) || TextUtils.isEmpty(Pass)) {  //아이디, 비밀번호를 입력안했다면
                    Toast.makeText(Login.this, "정보를 바르게 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                } else {  //입력했다면
                    mDatabase.getReference().child("user_list").child(Id).child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String value = snapshot.getValue(String.class);
//                            Toast.makeText(Login.this, "왜 안들어와", Toast.LENGTH_SHORT).show();
                            //이미 있는 아이디라면
                            if (value != null) {
                                //비밀번호 맞는지 확인
                                mDatabase.getReference().child("user_list").child(Id).child("userPass").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String value = snapshot.getValue(String.class);
                                        String Datapass = value;    //실시간데이터베이스에서 가져온 비밀번호
                                        String Pass = loginPass.getText().toString();
                                        Log.d("TAG", "PICK : " + "들어오냐");
//                                        Toast.makeText(Login.this, "Datapass : " + Datapass, Toast.LENGTH_SHORT).show();
//                                        Toast.makeText(Login.this, "Pass : " + Pass, Toast.LENGTH_SHORT).show();

                                        //일치하다면 로그인 성공
                                        if (Datapass.equals(Pass)) {
                                            Log.d("TAG", "PICK : " + "로그인 성공");
                                            Toast.makeText(Login.this, "로그인 성공!", Toast.LENGTH_SHORT).show();

                                            //user_textcolor에 오늘의 mood_color(글씨 색상) 데이터 가져오기
                                            databaseReference.child("user_textcolor").child(Id).child("today_date").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String today_date = snapshot.getValue(String.class);
                                                    Log.d("TAG", "PICK color : " + today_date);

                                                    //현재시간 가져오기
                                                    long now = System.currentTimeMillis();
                                                    //Date 형식으로 Convert
                                                    Date date = new Date(now);
                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                    String getTime = sdf.format(date);

                                                    if (today_date.equals(getTime)) {    //user_textcolor에 오늘 날짜가 들어있다면
                                                        Log.d("TAG", "PICK : " + "mood_color 존재");
                                                        //user_grape 오늘의 mood_grape 데이터 가져오기
                                                        databaseReference.child("user_grape").child(Id).child("today_date").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                String today_date = dataSnapshot.getValue(String.class);

                                                                //현재시간 가져오기
                                                                long now = System.currentTimeMillis();
                                                                //Date 형식으로 Convert
                                                                Date date = new Date(now);
                                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                                String getTime = sdf.format(date);

                                                                if (today_date.equals(getTime)) {    //mood_grape에 오늘 날짜가 들어있다면
                                                                    Log.d("TAG", "PICK : " + "mood_grape 존재");
                                                                    //다이어리 화면
                                                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                                                    intent.putExtra("userId", Id);
                                                                    startActivity(intent);

                                                                }
                                                                else {    //mood_grape에 오늘 날짜가 들어있지 않다면
                                                                    Log.d("TAG", "PICK : " + "mood_grape 존재하지않음");
                                                                    //포도 선택하는 곳으로 가기
                                                                    Intent intent = new Intent(Login.this, Mood_grape.class);
                                                                    intent.putExtra("userId", Id);
                                                                    startActivity(intent);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });
                                                    } else if(today_date != getTime){    ////user_textcolor에 오늘 날짜가 들어가있지 않다면
                                                        Log.d("TAG", "PICK : " + "mood_color 존재하지않음");
                                                        //글씨 색상 선택하는 곳으로 가기
                                                        Intent intent = new Intent(Login.this, Mood_color.class);
                                                        intent.putExtra("userId", Id);
                                                        startActivity(intent);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        } else {    //가져온 비밀번호와 입력한 비밀번호가 일치하지 않는다면
                                            Toast.makeText(Login.this, "아이디 또는 비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } else {    //없는 아이디라면
                                Toast.makeText(Login.this, "아이디 또는 비밀번호가 일치하지 않습니다!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }
}