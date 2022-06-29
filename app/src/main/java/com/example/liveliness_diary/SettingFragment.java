package com.example.liveliness_diary;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingFragment extends Fragment {
    String userId;
    ImageView userProfile;  //사용자 프로필
    TextView mood_color_change; //오늘의 색상 바꾸기
    TextView mood_grape_change; //오늘의 프로필 바꾸기

//    public SettingFragment() {
//
//    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.diary_write);
//
//        Intent intent = getIntent();
//        userId = intent.getStringExtra("userId");
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.setup, container, false);

        userId = this.getArguments().getString("userId");
        userProfile = view.findViewById(R.id.user_profile);
        mood_color_change = view.findViewById(R.id.mood_color_change);
        mood_grape_change = view.findViewById(R.id.mood_grape_change);

        //색 바꾸기
        mood_color_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Mood_color_change.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        //포도 바꾸기
        mood_grape_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Mood_grape_change.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        return view;
    }
}
