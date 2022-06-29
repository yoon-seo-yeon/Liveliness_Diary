package com.example.liveliness_diary;

//다이어리 보여주기

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Diary extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UserDiaryView> arrayList;

    private FirebaseStorage mStorage;   //스토리지
    private FirebaseDatabase mDatabase; //실시간 데이터베이스
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_main);

        mStorage = FirebaseStorage.getInstance();   //스토리지
        storageReference = mStorage.getReference();

        mDatabase = FirebaseDatabase.getInstance(); //실시간 데이터베이스
        databaseReference = mDatabase.getReference();

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        recyclerView = findViewById(R.id.diary_recyclerview);
        recyclerView.setHasFixedSize(true); //라사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();  //UserDiaryView객체를 담을 array리스트

        databaseReference.child("diary_list_view").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();  //데이터 쌓이는것 방지를 위해 기존 배열리스트가 존재하지 않게 초기화
                for(DataSnapshot snapshot : datasnapshot.getChildren()) {   //반복문으로 데이터 List를 추출
                    UserDiaryView userDiaryView = snapshot.getValue(UserDiaryView.class);   //만들어뒀던 UserDiaryView 객체에 데이터를 담는다
                    arrayList.add(userDiaryView);   //담은 데이터를 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //디비를 가져오던 중 에러 발생 시
                Log.d("TAG", "PICK : " + "오류");
            }
        });
        adapter = new DiaryAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);   //리사이클러뷰에 어댑터 연결

        findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Diary.this, WriteDiary.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
    }
}
