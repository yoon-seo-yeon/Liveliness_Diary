package com.example.liveliness_diary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Mood_color_change extends AppCompatActivity {
    String[] mood_arr = new String[2];  //색 담을 배열
    Button mood_1, mood_2, mood_3, mood_4, mood_5, mood_6, mood_7, mood_8,
            mood_9, mood_10, mood_11, mood_12;

    private FirebaseDatabase mDatabase; //실시간 데이터베이스
    private DatabaseReference databaseReference;

    ProgressDialog dialog;  //프로그레스바 보여주기
    ProgressBar progressBar;    //프로그레스바 원형

    String userId;

    String select1 = " ";        //첫번째 기분 선택
    String select2 = " ";        //두번째 기분 선택
    String today_mood = " ";   //두 가지 색을 합친 오늘의 기분색

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_color_change);

        mDatabase = FirebaseDatabase.getInstance(); //실시간 데이터베이스
        databaseReference = mDatabase.getReference();

        mood_1 = findViewById(R.id.mood_1);     //#F7E8D5
        mood_2 = findViewById(R.id.mood_2);     //#FFF77B
        mood_3 = findViewById(R.id.mood_3);     //#FBBF87
        mood_4 = findViewById(R.id.mood_4);     //#FFBACD
        mood_5 = findViewById(R.id.mood_5);     //#F581AB
        mood_6 = findViewById(R.id.mood_6);     //#F67180
        mood_7 = findViewById(R.id.mood_7);     //#C3F882
        mood_8 = findViewById(R.id.mood_8);     //#B5D2BC
        mood_9 = findViewById(R.id.mood_9);     //#B1DEFE
        mood_10 = findViewById(R.id.mood_10);   //#919DEF
        mood_11 = findViewById(R.id.mood_11);   //#7B91CD
        mood_12 = findViewById(R.id.mood_12);   //#393C41

        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(false);
        progressBar.setProgress(100);

        //Login.java에서 userId받기
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        mood_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mood_arr[0] == null) {   //첫번째 배열방이 비어있다면
                    //Toast.makeText(Mood_color.this, "mood1", Toast.LENGTH_SHORT).show();
                    mood_arr[0] = "#F7E8D5";    //첫번째 방에 mood_1컬러 넣어주기
                } else if (mood_arr[1] == null) {   //두번째 배열방이 비어있다면
                    mood_arr[1] = "#F7E8D5";    //두번째 방에 mood_1컬러 넣어주기
                    Mood();
                } else if (mood_arr[0] != null && mood_arr[1] != null) {    //배열 두 개 방이 모두 비어있지않다면 더이상 선택 X
                    Toast.makeText(Mood_color_change.this, "두 가지 색만 선택할 수 있어!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mood_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mood_arr[0] == null) {
                    mood_arr[0] = "#FFF77B";
                } else if (mood_arr[1] == null) {
                    //Toast.makeText(Mood_color.this, "mood_arr[0] : " + mood_arr[0], Toast.LENGTH_SHORT).show();
                    mood_arr[1] = "#FFF77B";
                    Mood();
                    //Toast.makeText(Mood_color.this, "mood_arr[1] : " + mood_arr[1], Toast.LENGTH_SHORT).show();
                } else if (mood_arr[0] != null && mood_arr[1] != null) {
                    Toast.makeText(Mood_color_change.this, "두 가지 색만 선택할 수 있어!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mood_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mood_arr[0] == null) {
                    mood_arr[0] = "#FBBF87";
                } else if (mood_arr[1] == null) {
                    mood_arr[1] = "#FBBF87";
                    Mood();
                } else if (mood_arr[0] != null && mood_arr[1] != null) {
                    Toast.makeText(Mood_color_change.this, "두 가지 색만 선택할 수 있어!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mood_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mood_arr[0] == null) {
                    mood_arr[0] = "#FFBACD";
                } else if (mood_arr[1] == null) {
                    mood_arr[1] = "#FFBACD";
                    Mood();
                } else if (mood_arr[0] != null && mood_arr[1] != null) {
                    Toast.makeText(Mood_color_change.this, "두 가지 색만 선택할 수 있어!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mood_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mood_arr[0] == null) {
                    mood_arr[0] = "#F581AB";
                } else if (mood_arr[1] == null) {
                    mood_arr[1] = "#F581AB";
                    Mood();
                } else if (mood_arr[0] != null && mood_arr[1] != null) {
                    Toast.makeText(Mood_color_change.this, "두 가지 색만 선택할 수 있어!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mood_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mood_arr[0] == null) {
                    mood_arr[0] = "#F67180";
                } else if (mood_arr[1] == null) {
                    mood_arr[1] = "#F67180";
                    Mood();
                } else if (mood_arr[0] != null && mood_arr[1] != null) {
                    Toast.makeText(Mood_color_change.this, "두 가지 색만 선택할 수 있어!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mood_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mood_arr[0] == null) {
                    mood_arr[0] = "#C3F882";
                } else if (mood_arr[1] == null) {
                    mood_arr[1] = "#C3F882";
                    Mood();
                } else if (mood_arr[0] != null && mood_arr[1] != null) {
                    Toast.makeText(Mood_color_change.this, "두 가지 색만 선택할 수 있어!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mood_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mood_arr[0] == null) {
                    mood_arr[0] = "#B5D2BC";
                } else if (mood_arr[1] == null) {
                    mood_arr[1] = "#B5D2BC";
                    Mood();
                } else if (mood_arr[0] != null && mood_arr[1] != null) {
                    Toast.makeText(Mood_color_change.this, "두 가지 색만 선택할 수 있어!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mood_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mood_arr[0] == null) {
                    mood_arr[0] = "#B1DEFE";
                } else if (mood_arr[1] == null) {
                    mood_arr[1] = "#B1DEFE";
                    Mood();
                } else if (mood_arr[0] != null && mood_arr[1] != null) {
                    Toast.makeText(Mood_color_change.this, "두 가지 색만 선택할 수 있어!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mood_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mood_arr[0] == null) {
                    mood_arr[0] = "#919DEF";
                } else if (mood_arr[1] == null) {
                    mood_arr[1] = "#919DEF";
                    Mood();
                } else if (mood_arr[0] != null && mood_arr[1] != null) {
                    Toast.makeText(Mood_color_change.this, "두 가지 색만 선택할 수 있어!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mood_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mood_arr[0] == null) {
                    mood_arr[0] = "#7B91CD";
                } else if (mood_arr[1] == null) {
                    mood_arr[1] = "#7B91CD";
                    Mood();
                } else if (mood_arr[0] != null && mood_arr[1] != null) {
                    Toast.makeText(Mood_color_change.this, "두 가지 색만 선택할 수 있어!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mood_12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mood_arr[0] == null) {
                    mood_arr[0] = "#393C41";
                } else if (mood_arr[1] == null) {
                    mood_arr[1] = "#393C41";
                    Mood();
                } else if (mood_arr[0] != null && mood_arr[1] != null) {
                    Toast.makeText(Mood_color_change.this, "두 가지 색만 선택할 수 있어!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void Mood() {
        select1 = mood_arr[0];
        select2 = mood_arr[1];

        //mood1 + mood2
        if ((select1 == "#F7E8D5" && select2 == "#FFF77B") || (select1 == "#FFF77B" && select2 == "#F7E8D5")) {
            today_mood = "#fbf0a8";
        }

        //mood1 + mood3
        else if ((select1 == "#F7E8D5" && select2 == "#FBBF87") || (select1 == "#FBBF87" && select2 == "#F7E8D5")) {
            today_mood = "#f9d4ae";
        }

        //mood1 + mood4
        else if ((select1 == "#F7E8D5" && select2 == "#FFBACD") || (select1 == "#FFBACD" && select2 == "#F7E8D5")) {
            today_mood = "#fbd1d1";
        }

        //mood1 + mood5
        else if ((select1 == "#F7E8D5" && select2 == "#F581AB") || (select1 == "#F581AB" && select2 == "#F7E8D5")) {
            today_mood = "#f6b4c0";
        }

        //mood1 + mood6
        else if ((select1 == "#F7E8D5" && select2 == "#F67180") || (select1 == "#F67180" && select2 == "#F7E8D5")) {
            today_mood = "#f6acaa";
        }

        //mood1 + mood7
        else if ((select1 == "#F7E8D5" && select2 == "#C3F882") || (select1 == "#C3F882" && select2 == "#F7E8D5")) {
            today_mood = "#ddf0ac";
        }

        //mood1 + mood8
        else if ((select1 == "#F7E8D5" && select2 == "#B5D2BC") || (select1 == "#B5D2BC" && select2 == "#F7E8D5")) {
            today_mood = "#B5D2BC";
        }

        //mood1 + mood9
        else if ((select1 == "#F7E8D5" && select2 == "#B1DEFE") || (select1 == "#B1DEFE" && select2 == "#F7E8D5")) {
            today_mood = "#d4e3ea";
        }

        //mood1 + mood10
        else if ((select1 == "#F7E8D5" && select2 == "#919DEF") || (select1 == "#919DEF" && select2 == "#F7E8D5")) {
            today_mood = "#c4c2e2";
        }

        //mood1 + mood11
        else if ((select1 == "#F7E8D5" && select2 == "#7B91CD") || (select1 == "#7B91CD" && select2 == "#F7E8D5")) {
            today_mood = "#b9bcd1";
        }

        //mood1 + mood11
        else if ((select1 == "#F7E8D5" && select2 == "#393C41") || (select1 == "#393C41" && select2 == "#F7E8D5")) {
            today_mood = "#98928b";
        }

        //mood2 + mood3
        else if ((select1 == "#FFF77B" && select2 == "#FBBF87") || (select1 == "#FBBF87" && select2 == "#FFF77B")) {
            today_mood = "#fddb81";
        }

        //mood2 + mood4
        else if ((select1 == "#FFF77B" && select2 == "#FFBACD") || (select1 == "#FFBACD" && select2 == "#FFF77B")) {
            today_mood = "#ffd8a4";
        }

        //mood2 + mood5
        else if ((select1 == "#FFF77B" && select2 == "#F581AB") || (select1 == "#F581AB" && select2 == "#FFF77B")) {
            today_mood = "#fabc93";
        }

        //mood2 + mood6
        else if ((select1 == "#FFF77B" && select2 == "#F67180") || (select1 == "#F67180" && select2 == "#FFF77B")) {
            today_mood = "#fab47e";
        }

        //mood2 + mood7
        else if ((select1 == "#FFF77B" && select2 == "#C3F882") || (select1 == "#C3F882" && select2 == "#FFF77B")) {
            today_mood = "#e1f87e";
        }

        //mood2 + mood8
        else if ((select1 == "#FFF77B" && select2 == "#B5D2BC") || (select1 == "#B5D2BC" && select2 == "#FFF77B")) {
            today_mood = "#dae49c";
        }

        //mood2 + mood9
        else if ((select1 == "#FFF77B" && select2 == "#B1DEFE") || (select1 == "#B1DEFE" && select2 == "#FFF77B")) {
            today_mood = "#d8eabc";
        }

        //mood2 + mood10
        else if ((select1 == "#FFF77B" && select2 == "#919DEF") || (select1 == "#919DEF" && select2 == "#FFF77B")) {
            today_mood = "#c8cab5";
        }

        //mood2 + mood11
        else if ((select1 == "#FFF77B" && select2 == "#7B91CD") || (select1 == "#7B91CD" && select2 == "#FFF77B")) {
            today_mood = "#bdc4a4";
        }

        //mood2 + mood12
        else if ((select1 == "#FFF77B" && select2 == "#393C41") || (select1 == "#393C41" && select2 == "#FFF77B")) {
            today_mood = "#9c9a5e";
        }

        //mood3 + mood4
        else if ((select1 == "#FBBF87" && select2 == "#FFBACD") || (select1 == "#FFBACD" && select2 == "#FBBF87")) {
            today_mood = "#fdbcaa";
        }

        //mood3 + mood5
        else if ((select1 == "#FBBF87" && select2 == "#F581AB") || (select1 == "#F581AB" && select2 == "#FBBF87")) {
            today_mood = "#f8a099";
        }

        //mood3 + mood6
        else if ((select1 == "#FBBF87" && select2 == "#F67180") || (select1 == "#F67180" && select2 == "#FBBF87")) {
            today_mood = "#f89884";
        }

        //mood3 + mood7
        else if ((select1 == "#FBBF87" && select2 == "#C3F882") || (select1 == "#C3F882" && select2 == "#FBBF87")) {
            today_mood = "#dfdc84";
        }

        //mood3 + mood8
        else if ((select1 == "#FBBF87" && select2 == "#B5D2BC") || (select1 == "#B5D2BC" && select2 == "#FBBF87")) {
            today_mood = "#d8c8a2";
        }

        //mood3 + mood9
        else if ((select1 == "#FBBF87" && select2 == "#B1DEFE") || (select1 == "#B1DEFE" && select2 == "#FBBF87")) {
            today_mood = "#d6cec2";
        }

        //mood3 + mood10
        else if ((select1 == "#FBBF87" && select2 == "#919DEF") || (select1 == "#919DEF" && select2 == "#FBBF87")) {
            today_mood = "#c6aebb";
        }

        //mood3 + mood11
        else if ((select1 == "#FBBF87" && select2 == "#7B91CD") || (select1 == "#7B91CD" && select2 == "#FBBF87")) {
            today_mood = "#bba8aa";
        }

        //mood3 + mood12
        else if ((select1 == "#FBBF87" && select2 == "#393C41") || (select1 == "#393C41" && select2 == "#FBBF87")) {
            today_mood = "#9a7e64";
        }

        //mood4 + mood5
        else if ((select1 == "#FFBACD" && select2 == "#F581AB") || (select1 == "#F581AB" && select2 == "#FFBACD")) {
            today_mood = "#fa9ebc";
        }

        //mood4 + mood6
        else if ((select1 == "#FFBACD" && select2 == "#F67180") || (select1 == "#F67180" && select2 == "#FFBACD")) {
            today_mood = "#fa96a6";
        }

        //mood4 + mood7
        else if ((select1 == "#FFBACD" && select2 == "#C3F882") || (select1 == "#C3F882" && select2 == "#FFBACD")) {
            today_mood = "#e1d9a8";
        }

        //mood4 + mood8
        else if ((select1 == "#FFBACD" && select2 == "#B5D2BC") || (select1 == "#B5D2BC" && select2 == "#FFBACD")) {
            today_mood = "#dac6c4";
        }

        //mood4 + mood9
        else if ((select1 == "#FFBACD" && select2 == "#B1DEFE") || (select1 == "#B1DEFE" && select2 == "#FFBACD")) {
            today_mood = "#d8cce6";
        }

        //mood4 + mood10
        else if ((select1 == "#FFBACD" && select2 == "#919DEF") || (select1 == "#919DEF" && select2 == "#FFBACD")) {
            today_mood = "#c8acde";
        }

        //mood4 + mood11
        else if ((select1 == "#FFBACD" && select2 == "#7B91CD") || (select1 == "#7B91CD" && select2 == "#FFBACD")) {
            today_mood = "#bda6cd";
        }

        //mood4 + mood12
        else if ((select1 == "#FFBACD" && select2 == "#393C41") || (select1 == "#393C41" && select2 == "#FFBACD")) {
            today_mood = "#9c7b87";
        }

        //mood5 + mood6
        else if ((select1 == "#F581AB" && select2 == "#F67180") || (select1 == "#F67180" && select2 == "#F581AB")) {
            today_mood = "#f67996";
        }

        //mood5 + mood7
        else if ((select1 == "#F581AB" && select2 == "#C3F882") || (select1 == "#C3F882" && select2 == "#F581AB")) {
            today_mood = "#dcbc96";
        }

        //mood5 + mood8
        else if ((select1 == "#F581AB" && select2 == "#B5D2BC") || (select1 == "#B5D2BC" && select2 == "#F581AB")) {
            today_mood = "#d5aab4";
        }

        //mood5 + mood9
        else if ((select1 == "#F581AB" && select2 == "#B1DEFE") || (select1 == "#B1DEFE" && select2 == "#F581AB")) {
            today_mood = "#d3b0d4";
        }

        //mood5 + mood10
        else if ((select1 == "#F581AB" && select2 == "#919DEF") || (select1 == "#919DEF" && select2 == "#F581AB")) {
            today_mood = "#c38fcd";
        }

        //mood5 + mood11
        else if ((select1 == "#F581AB" && select2 == "#7B91CD") || (select1 == "#7B91CD" && select2 == "#F581AB")) {
            today_mood = "#b889bc";
        }

        //mood5 + mood12
        else if ((select1 == "#F581AB" && select2 == "#393C41") || (select1 == "#393C41" && select2 == "#F581AB")) {
            today_mood = "#975e76";
        }

        //mood6 + mood7
        else if ((select1 == "#F67180" && select2 == "#C3F882") || (select1 == "#C3F882" && select2 == "#F67180")) {
            today_mood = "#dcb481";
        }

        //mood6 + mood8
        else if ((select1 == "#F67180" && select2 == "#B5D2BC") || (select1 == "#B5D2BC" && select2 == "#F67180")) {
            today_mood = "#d6a29e";
        }

        //mood6 + mood9
        else if ((select1 == "#F67180" && select2 == "#B1DEFE") || (select1 == "#B1DEFE" && select2 == "#F67180")) {
            today_mood = "#d4a8bf";
        }

        //mood6 + mood10
        else if ((select1 == "#F67180" && select2 == "#919DEF") || (select1 == "#919DEF" && select2 == "#F67180")) {
            today_mood = "#c487b8";
        }

        //mood6 + mood11
        else if ((select1 == "#F67180" && select2 == "#7B91CD") || (select1 == "#7B91CD" && select2 == "#F67180")) {
            today_mood = "#b881a6";
        }

        //mood6 + mood12
        else if ((select1 == "#F67180" && select2 == "#393C41") || (select1 == "#393C41" && select2 == "#F67180")) {
            today_mood = "#985660";
        }

        //mood7 + mood8
        else if ((select1 == "#C3F882" && select2 == "#B5D2BC") || (select1 == "#B5D2BC" && select2 == "#C3F882")) {
            today_mood = "#bce59f";
        }

        //mood7 + mood9
        else if ((select1 == "#C3F882" && select2 == "#B1DEFE") || (select1 == "#B1DEFE" && select2 == "#C3F882")) {
            today_mood = "#baebc0";
        }

        //mood7 + mood10
        else if ((select1 == "#C3F882" && select2 == "#919DEF") || (select1 == "#919DEF" && select2 == "#C3F882")) {
            today_mood = "#aacab8";
        }

        //mood7 + mood11
        else if ((select1 == "#C3F882" && select2 == "#7B91CD") || (select1 == "#7B91CD" && select2 == "#C3F882")) {
            today_mood = "#9fc4a8";
        }

        //mood7 + mood12
        else if ((select1 == "#C3F882" && select2 == "#393C41") || (select1 == "#393C41" && select2 == "#C3F882")) {
            today_mood = "#7e9a62";
        }

        //mood8 + mood9
        else if ((select1 == "#B5D2BC" && select2 == "#B1DEFE") || (select1 == "#B1DEFE" && select2 == "#B5D2BC")) {
            today_mood = "#b3d8dd";
        }

        //mood8 + mood10
        else if ((select1 == "#B5D2BC" && select2 == "#919DEF") || (select1 == "#919DEF" && select2 == "#B5D2BC")) {
            today_mood = "#a3b8d6";
        }

        //mood8 + mood11
        else if ((select1 == "#B5D2BC" && select2 == "#7B91CD") || (select1 == "#7B91CD" && select2 == "#B5D2BC")) {
            today_mood = "#98b2c4";
        }

        //mood8 + mood12
        else if ((select1 == "#B5D2BC" && select2 == "#393C41") || (select1 == "#393C41" && select2 == "#B5D2BC")) {
            today_mood = "#77877e";
        }

        //mood9 + mood10
        else if ((select1 == "#B1DEFE" && select2 == "#919DEF") || (select1 == "#919DEF" && select2 == "#B1DEFE")) {
            today_mood = "#a1bef6";
        }

        //mood9 + mood11
        else if ((select1 == "#B1DEFE" && select2 == "#7B91CD") || (select1 == "#7B91CD" && select2 == "#B1DEFE")) {
            today_mood = "#96b8e6";
        }

        //mood9 + mood12
        else if ((select1 == "#B1DEFE" && select2 == "#393C41") || (select1 == "#393C41" && select2 == "#B1DEFE")) {
            today_mood = "#758da0";
        }

        //mood10 + mood11
        else if ((select1 == "#919DEF" && select2 == "#7B91CD") || (select1 == "#7B91CD" && select2 == "#919DEF")) {
            today_mood = "#8697de";
        }

        //mood10 + mood12
        else if ((select1 == "#919DEF" && select2 == "#393C41") || (select1 == "#393C41" && select2 == "#919DEF")) {
            today_mood = "#656c98";
        }

        //mood11 + mood12
        else if ((select1 == "#7B91CD" && select2 == "#393C41") || (select1 == "#393C41" && select2 == "#7B91CD")) {
            today_mood = "#5a6687";
        }

        //Toast.makeText(this, today_mood, Toast.LENGTH_SHORT).show();

        //현재시간 가져오기
        long now = System.currentTimeMillis();
        //Date 형식으로 Convert
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String getTime = sdf.format(date);

//        dialog = new ProgressDialog(Mood_color_change.this);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage("오늘의 색상이 변경되고 있습니다!");
//        dialog.show();

        databaseReference.child("user_textcolor").child(userId).child("mood_color").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String color = snapshot.getValue(String.class);

                if(color == null) {
                    // user_textcolor DB에 오늘 날짜와 선택한 글씨 색상 넣기
                    //내가 선택한 두가지 색 최종색과 오늘 날짜 저장하기
                    User_mood user_mood = new User_mood(today_mood, getTime);
                    databaseReference.child("user_textcolor").child(userId).setValue(user_mood);

                    Intent intent = new Intent(Mood_color_change.this, MainActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
                else {
                    // user_textcolor DB에 오늘 날짜와 선택한 글씨 색상 넣기
                    //내가 선택한 두가지 색 최종색과 오늘 날짜 저장하기
                    User_mood user_mood = new User_mood(today_mood, getTime);
                    databaseReference.child("user_textcolor").child(userId).setValue(user_mood);

                    Intent intent = new Intent(Mood_color_change.this, MainActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


//        finish();
    }
}
