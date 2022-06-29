package com.example.liveliness_diary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class DiaryFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UserDiaryView> arrayList;

    private FirebaseStorage mStorage;   //스토리지
    private FirebaseDatabase mDatabase; //실시간 데이터베이스
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.diary_main, container, false);

        userId = this.getArguments().getString("userId");

        mStorage = FirebaseStorage.getInstance();   //스토리지
        storageReference = mStorage.getReference();

        mDatabase = FirebaseDatabase.getInstance(); //실시간 데이터베이스
        databaseReference = mDatabase.getReference();

        recyclerView = view.findViewById(R.id.diary_recyclerview);
        recyclerView.setHasFixedSize(true); //라사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();  //UserDiaryView객체를 담을 array리스트

        databaseReference.child("diary_list_view").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                Log.d("TAG", "PICk : " + "들어옴1");

                arrayList.clear();  //데이터 쌓이는것 방지를 위해 기존 배열리스트가 존재하지 않게 초기화
                for(DataSnapshot snapshot : datasnapshot.getChildren()) {   //반복문으로 데이터 List를 추출
                    UserDiaryView userDiaryView = snapshot.getValue(UserDiaryView.class);   //만들어뒀던 UserDiaryView 객체에 데이터를 담는다
                    arrayList.add(userDiaryView);   //담은 데이터를 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                    Log.d("TAG", "PICk : " + "들어옴2");
                }

                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
                Log.d("TAG", "PICk : " + "들어옴3");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //디비를 가져오던 중 에러 발생 시
                Log.d("TAG", "PICK : " + "오류");
            }
        });

        adapter = new DiaryAdapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter);   //리사이클러뷰에 어댑터 연결
        Log.d("TAG", "PICk : " + "들어옴4");

        view.findViewById(R.id.floatingActionButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WriteDiary.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });

        return view;
    }
}
