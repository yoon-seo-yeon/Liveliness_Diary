package com.example.liveliness_diary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DiaryDelete extends AppCompatActivity {
    String userId, diary_date, image;

    private FirebaseStorage mStorage;   //스토리지
    private FirebaseDatabase mDatabase; //실시간 데이터베이스
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        diary_date = intent.getStringExtra("date");
        image = intent.getStringExtra("image");
        
        mStorage = FirebaseStorage.getInstance();   //스토리지
        storageReference = mStorage.getReference();

        mDatabase = FirebaseDatabase.getInstance(); //실시간 데이터베이스
        databaseReference = mDatabase.getReference();
        

        //스토리지 이미지 삭제
        onDeleteImage(userId, image, diary_date);

        //실시간 데이터베이스 삭제
        onDeleteDiary(userId, diary_date);
    }

    //스토리지 이미지 삭제
    private void onDeleteImage(String userId, String image, String date) {
        String Id = userId;
        String Date = date;
        String Image = image;

        Log.d("TAG", "PICK : " + "스토리지 성공1");
        Log.d("TAG", "PICK Id : " + Id);
        Log.d("TAG", "PICK Date : " + Date);
        Log.d("TAG", "PICK Image : " + Image);


        mStorage.getReference().child("userDiaryImage").child(Id).child(Image).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                //실시간 데이터베이스 삭제
//                onDeleteDiary(Id, Date);
                Log.d("TAG", "PICK : " + "스토리지 성공2");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "PICK : " + "스토리지 실패");
            }
        });
    }

    //실시간 데이터베이스 삭제
    private void onDeleteDiary(String userId, String date) {
        mDatabase.getReference().child("diary_list").child(userId).child(date).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(DiaryDelete.this, "삭제 완료!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DiaryDelete.this, "삭제 실패..", Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase.getReference().child("diary_list_view").child(userId).child(date).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(DiaryDelete.this, "삭제 완료!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(DiaryDelete.this, "삭제 실패..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

