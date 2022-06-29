package com.example.liveliness_diary;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeFragment extends Fragment {
    private FirebaseStorage mStorage;   //스토리지
    private FirebaseDatabase mDatabase; //실시간 데이터베이스
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private String mood_grape;    //포도 happy인지 ...
    String userId;
    private Uri grapeUrl;
    private TextView text_color;
    private ImageView grape_image;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.home, container, false);

        userId = this.getArguments().getString("userId");
        Log.d("TAG", "PICK HOME usreID : " + userId);

        mStorage = FirebaseStorage.getInstance();   //스토리지
        storageReference = mStorage.getReference();

        mDatabase = FirebaseDatabase.getInstance(); //실시간 데이터베이스
        databaseReference = mDatabase.getReference();

        text_color = (TextView) view.findViewById(R.id.color);
        grape_image = (ImageView) view.findViewById(R.id.grape_image);

        databaseReference.child("user_grape").child(userId).child("mood_grape").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mood_grape = snapshot.getValue(String.class);
                Log.d("TAG","PICK : " + "HOME!!");

                if(mood_grape.equals("happy")) {    //포도가 happy라면
                    grapeUrl = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_happy");
                }
                else if(mood_grape.equals("satis")) {
                    grapeUrl = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_satis");
                }
                else if(mood_grape.equals("usual")) {
                    grapeUrl = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_usual");
                }
                else if(mood_grape.equals("tired")) {
                    grapeUrl = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_tired");
                }
                else if(mood_grape.equals("sad")) {
                    grapeUrl = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_sad");
                }
                else if(mood_grape.equals("angry")) {
                    grapeUrl = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_angry");
                }

                grape_image.setImageURI(grapeUrl);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        databaseReference.child("user_textcolor").child(userId).child("mood_color").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String t_color= snapshot.getValue(String.class);

                Log.d("TAG", "PICK HOME t_color : " + t_color);
                text_color.setText(t_color);
                text_color.setTextColor(Color.parseColor(t_color));

//                if(t_color.equals("")) {
//                    Log.d("TAG", "PICK HOME t_color : " + t_color);
//                    text_color.setText(t_color);
//                    text_color.setTextColor(Color.parseColor(t_color));
//                }
//                else if(!t_color.equals("")) {
//                    Log.d("TAG", "PICK HOME t_color : " + t_color);
//                    text_color.setText(t_color);
//                    text_color.setTextColor(Color.parseColor(t_color));
//                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        return view;
    }
}
