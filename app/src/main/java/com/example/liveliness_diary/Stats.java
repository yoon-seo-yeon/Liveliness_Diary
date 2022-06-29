package com.example.liveliness_diary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

//사용자 기분 통계
public class Stats extends AppCompatActivity {
//    이미지
    ImageView mood_sun, mood_mon, mood_tue, mood_wed, mood_thu, mood_fri, mood_sat;

//    글들
    TextView mood_sun_text, mood_mon_text, mood_tue_text, mood_wed_text, mood_thu_text, mood_fri_text, mood_sat_text;

    private FirebaseStorage mStorage;   //스토리지
    private FirebaseDatabase mDatabase; //실시간 데이터베이스
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    String userId;  //사용자 아이디
//    int rand;   //랜덤

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood_stats);
//        Toast.makeText(this, "ㅋㅋㅋ", Toast.LENGTH_SHORT).show();

        mood_mon = findViewById(R.id.mood_mon);
        mood_tue = findViewById(R.id.mood_tue);
        mood_wed = findViewById(R.id.mood_wed);
        mood_thu = findViewById(R.id.mood_thu);
        mood_fri = findViewById(R.id.mood_fri);
        mood_sat = findViewById(R.id.mood_sat);
        mood_sun = findViewById(R.id.mood_sun);

        mood_mon_text = findViewById(R.id.mood_mon_text);
        mood_tue_text = findViewById(R.id.mood_tue_text);
        mood_wed_text = findViewById(R.id.mood_wed_text);
        mood_thu_text = findViewById(R.id.mood_thu_text);
        mood_fri_text = findViewById(R.id.mood_fri_text);
        mood_sat_text = findViewById(R.id.mood_sat_text);
        mood_sun_text = findViewById(R.id.mood_sun_text);

        mStorage = FirebaseStorage.getInstance();   //스토리지
        storageReference = mStorage.getReference();

        mDatabase = FirebaseDatabase.getInstance(); //실시간 데이터베이스
        databaseReference = mDatabase.getReference();

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        Random random = new Random();   //랜덤 발생

        //행복, 만족, 보통, 피곤, 슬픔, 화남
        String happy[] = {"오늘 하루 너무 기뻤어!", "행복한 하루였다!", "웃음 가득한 하루였어!", "기분 좋은 하루였어!", "오늘 하루 짱!"};
        String satis[] = {"오늘 하루 너무 만족스럽다!", "만족스러운 하루였다!", "괜찮은 하루였어!", "나쁘지않은 하루였어!", "오늘 하루 괜찮네!"};
        String usual[] = {"오늘 하루 너무 괜찮네!", "괜찮은 하루였다!", "보통과 같은 하루였어!", "괜찮은 하루였어!", "오늘 괜찮네!"};
        String tired[] = {"오늘 하루 너무 힘들었어..", "피곤한 하루였다..", "하쒸.. 너무 피곤해ㅜㅜ", "피곤해 죽겠다..", "오늘 너무 피곤해.. 자고싶어.."};
        String sad[] = {"오늘 하루 너무 슬퍼..", "슬픈 하루였다..", "눈물나는 하루였다..", "핫쒸.. 눈물나와", "오늘 너무 슬퍼.. 울고싶다"};
        String angry[] = {"오늘 하루 너무 화나!!", "화 가득한 하루였다..!", "울분나는 하루였다!", "화가 난다!!!", "화나 죽겠어!!"};

        /*Test
        mStorage.getReference("UserStatsImages/" + userId + "/Sun").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Toast.makeText(UserStats.this, "쫘라랑", Toast.LENGTH_SHORT).show();
                Glide.with(UserStats.this).load(uri).into(mood_sun);
                //mood_sun.setImageURI(uri);
            }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(UserStats.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });
         */

        //Mon
        databaseReference.child("user_moodstats").child(userId).child("mon").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Mon = snapshot.getValue(String.class);
                Log.d("TAG", "PICK 월ㅋ : " + Mon);

                if(Mon.equals("mon")) {
                    mood_mon.setImageResource(R.drawable.no);
                    mood_mon_text.setText("멘트 없음");
                }

                else {
                    Toast.makeText(Stats.this, "월있음", Toast.LENGTH_SHORT).show();

                    StorageReference MonImage = storageReference.child("UserStatsImages/" + userId + "/Mon");
                    MonImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(Stats.this).load(uri).into(mood_mon);

                            int rand = random.nextInt(5);

                            //mon_text가져오기
                            databaseReference.child("user_moodstats").child(userId).child("mon_text").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String mon_text = snapshot.getValue(String.class);
                                    if(mon_text.equals("happy")) {
                                        mood_mon_text.setText(happy[rand]);
                                    }
                                    else if(mon_text.equals("satis")) {
                                        mood_mon_text.setText(satis[rand]);
                                    }
                                    else if(mon_text.equals("usual")) {
                                        mood_mon_text.setText(usual[rand]);
                                    }
                                    else if(mon_text.equals("tired")) {
                                        mood_mon_text.setText(tired[rand]);
                                    }
                                    else if(mon_text.equals("sad")) {
                                        mood_mon_text.setText(sad[rand]);
                                    }
                                    else if(mon_text.equals("angry")) {
                                        mood_mon_text.setText(angry[rand]);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Tue
        databaseReference.child("user_moodstats").child(userId).child("tue").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Tue = snapshot.getValue(String.class);
                Log.d("TAG", "PICK 화ㅋ : " + Tue);

                if(Tue.equals("tue")) {
                    mood_tue.setImageResource(R.drawable.no);
                    mood_tue_text.setText("멘트 없음");
                }

                else {
                    Toast.makeText(Stats.this, "화있음", Toast.LENGTH_SHORT).show();

                    StorageReference TueImage = storageReference.child("UserStatsImages/" + userId + "/Tue");
                    TueImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(Stats.this).load(uri).into(mood_tue);

                            int rand = random.nextInt(5);

                            //tue_text가져오기
                            databaseReference.child("user_moodstats").child(userId).child("tue_text").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String tue_text = snapshot.getValue(String.class);

                                    if(tue_text.equals("happy")) {
                                        mood_tue_text.setText(happy[rand]);
                                    }
                                    else if(tue_text.equals("satis")) {
                                        mood_tue_text.setText(satis[rand]);
                                    }
                                    else if(tue_text.equals("usual")) {
                                        mood_tue_text.setText(usual[rand]);
                                    }
                                    else if(tue_text.equals("tired")) {
                                        mood_tue_text.setText(tired[rand]);
                                    }
                                    else if(tue_text.equals("sad")) {
                                        mood_tue_text.setText(sad[rand]);
                                    }
                                    else if(tue_text.equals("angry")) {
                                        mood_tue_text.setText(angry[rand]);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Wed
        databaseReference.child("user_moodstats").child(userId).child("wed").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Wed = snapshot.getValue(String.class);
                Log.d("TAG", "PICK 수ㅋ : " + Wed);

                if(Wed.equals("wed")) {
                    mood_wed.setImageResource(R.drawable.no);
                    mood_wed_text.setText("멘트 없음");
                }

                else {
                    Toast.makeText(Stats.this, "수있음", Toast.LENGTH_SHORT).show();

                    StorageReference WedImage = storageReference.child("UserStatsImages/" + userId + "/Wed");
                    WedImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(Stats.this).load(uri).into(mood_wed);

                            int rand = random.nextInt(5);

                            //wed_text가져오기
                            databaseReference.child("user_moodstats").child(userId).child("wed_text").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String wed_text = snapshot.getValue(String.class);

                                    if(wed_text.equals("happy")) {
                                        mood_wed_text.setText(happy[rand]);
                                    }
                                    else if(wed_text.equals("satis")) {
                                        mood_wed_text.setText(satis[rand]);
                                    }
                                    else if(wed_text.equals("usual")) {
                                        mood_wed_text.setText(usual[rand]);
                                    }
                                    else if(wed_text.equals("tired")) {
                                        mood_wed_text.setText(tired[rand]);
                                    }
                                    else if(wed_text.equals("sad")) {
                                        mood_wed_text.setText(sad[rand]);
                                    }
                                    else if(wed_text.equals("angry")) {
                                        mood_wed_text.setText(angry[rand]);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Thu
        databaseReference.child("user_moodstats").child(userId).child("thu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Thu = snapshot.getValue(String.class);
                Log.d("TAG", "PICK 목ㅋ : " + Thu);

                if(Thu.equals("thu")) {
                    mood_thu.setImageResource(R.drawable.no);
                    mood_thu_text.setText("멘트 없음");
                }

                else {
                    Toast.makeText(Stats.this, "목있음", Toast.LENGTH_SHORT).show();

                    StorageReference ThuImage = storageReference.child("UserStatsImages/" + userId + "/Thu");
                    ThuImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(Stats.this).load(uri).into(mood_thu);

                            int rand = random.nextInt(5);

                            //thu_text가져오기
                            databaseReference.child("user_moodstats").child(userId).child("thu_text").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String thu_text = snapshot.getValue(String.class);

                                    if(thu_text.equals("happy")) {
                                        mood_thu_text.setText(happy[rand]);
                                    }
                                    else if(thu_text.equals("satis")) {
                                        mood_thu_text.setText(satis[rand]);
                                    }
                                    else if(thu_text.equals("usual")) {
                                        mood_thu_text.setText(usual[rand]);
                                    }
                                    else if(thu_text.equals("tired")) {
                                        mood_thu_text.setText(tired[rand]);
                                    }
                                    else if(thu_text.equals("sad")) {
                                        mood_thu_text.setText(sad[rand]);
                                    }
                                    else if(thu_text.equals("angry")) {
                                        mood_thu_text.setText(angry[rand]);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Fri
        databaseReference.child("user_moodstats").child(userId).child("fri").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Fri = snapshot.getValue(String.class);
                Log.d("TAG", "PICK 금ㅋ : " + Fri);

                if(Fri.equals("fri")) {
                    mood_fri.setImageResource(R.drawable.no);
                    mood_fri_text.setText("멘트 없음");
                }

                else {
                    Toast.makeText(Stats.this, "금있음", Toast.LENGTH_SHORT).show();

                    StorageReference FriImage = storageReference.child("UserStatsImages/" + userId + "/Fri");
                    FriImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(Stats.this).load(uri).into(mood_fri);

                            int rand = random.nextInt(5);

                            //fri_text가져오기
                            databaseReference.child("user_moodstats").child(userId).child("fri_text").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String fri_text = snapshot.getValue(String.class);

                                    if(fri_text.equals("happy")) {
                                        mood_fri_text.setText(happy[rand]);
                                    }
                                    else if(fri_text.equals("satis")) {
                                        mood_fri_text.setText(satis[rand]);
                                    }
                                    else if(fri_text.equals("usual")) {
                                        mood_fri_text.setText(usual[rand]);
                                    }
                                    else if(fri_text.equals("tired")) {
                                        mood_fri_text.setText(tired[rand]);
                                    }
                                    else if(fri_text.equals("sad")) {
                                        mood_fri_text.setText(sad[rand]);
                                    }
                                    else if(fri_text.equals("angry")) {
                                        mood_fri_text.setText(angry[rand]);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Sat
        databaseReference.child("user_moodstats").child(userId).child("sat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Sat = snapshot.getValue(String.class);
                Log.d("TAG", "PICK 토ㅋ : " + Sat);

                if(Sat.equals("sat")) {
//                    Toast.makeText(UserStats.this, "토토토토토토토톹토", Toast.LENGTH_SHORT).show();
                    mood_sat.setImageResource(R.drawable.no);
                    mood_sat_text.setText("멘트 없음");
                }

                else if(snapshot.exists()){
                    Toast.makeText(Stats.this, "토있음", Toast.LENGTH_SHORT).show();

                    StorageReference SatImage = storageReference.child("UserStatsImages/" + userId + "/Sat");
                    SatImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(Stats.this).load(uri).into(mood_sat);

                            int rand = random.nextInt(5);

                            //sat_text가져오기
                            databaseReference.child("user_moodstats").child(userId).child("sat_text").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String sat_text = snapshot.getValue(String.class);

                                    if(sat_text.equals("happy")) {
                                        mood_sat_text.setText(happy[rand]);
                                    }
                                    else if(sat_text.equals("satis")) {
                                        mood_sat_text.setText(satis[rand]);
                                    }
                                    else if(sat_text.equals("usual")) {
                                        mood_sat_text.setText(usual[rand]);
                                    }
                                    else if(sat_text.equals("tired")) {
                                        mood_sat_text.setText(tired[rand]);
                                    }
                                    else if(sat_text.equals("sad")) {
                                        mood_sat_text.setText(sad[rand]);
                                    }
                                    else if(sat_text.equals("angry")) {
                                        mood_sat_text.setText(angry[rand]);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //Sun
        databaseReference.child("user_moodstats").child(userId).child("sun").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Sun = snapshot.getValue(String.class);
                Log.d("TAG", "PICK 일ㅋ : " + Sun);

                if(Sun.equals("sun")) {
                    mood_sun.setImageResource(R.drawable.no);
                    mood_sun_text.setText("멘트 없음");
                }

                else {
                    Toast.makeText(Stats.this, "일있음", Toast.LENGTH_SHORT).show();

                    StorageReference SunImage = storageReference.child("UserStatsImages/" + userId + "/Sun");
                    SunImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(Stats.this).load(uri).into(mood_sun);

                            int rand = random.nextInt(5);

                            //sun_text가져오기
                            databaseReference.child("user_moodstats").child(userId).child("sun_text").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String sun_text = snapshot.getValue(String.class);

                                    if(sun_text.equals("happy")) {
                                        mood_sun_text.setText(happy[rand]);
                                    }
                                    else if(sun_text.equals("satis")) {
                                        mood_sun_text.setText(satis[rand]);
                                    }
                                    else if(sun_text.equals("usual")) {
                                        mood_sun_text.setText(usual[rand]);
                                    }
                                    else if(sun_text.equals("tired")) {
                                        mood_sun_text.setText(tired[rand]);
                                    }
                                    else if(sun_text.equals("sad")) {
                                        mood_sun_text.setText(sad[rand]);
                                    }
                                    else if(sun_text.equals("angry")) {
                                        mood_sun_text.setText(angry[rand]);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        /*
        databaseReference.child("user_moodstats").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Stats stats = snapshot.getValue(Stats.class);

                String Mon, Tue, Wed, Thu, Fri, Sat, Sun, id;

                        id = stats.getUserId();
                        Mon = stats.getMon();
                        Tue = stats.getTue();
                        Wed = stats.getWed();
                        Thu = stats.getThu();
                        Fri = stats.getFri();
                        Sat = stats.getSat();
                        Sun = stats.getSun();

                        Log.d("TAG", "PICK" + id);
                        Log.d("TAG", "PICK 월있음 : " + Mon);
                        Log.d("TAG", "PICK 화있음 : " + Tue);
                        Log.d("TAG", "PICK 수있음 : " + Wed);
                        Log.d("TAG", "PICK 목있음 : " + Thu);
                        Log.d("TAG", "PICK 금있음 : " + Fri);
                        Log.d("TAG", "PICK 토있음 : " + Sat);
                        Log.d("TAG", "PICK 일있음 : " + Sun);

//                int rand;
//                for(int i = 0; i < happy.length; i++) {
//                    rand = (int) (Math.random() * 5);
//                }

                        if (Mon == null) { //월요일 이미지가 있다면
                            Toast.makeText(UserStats.this, "월있음", Toast.LENGTH_SHORT).show();

                            StorageReference MonImage = storageReference.child("UserStatsImages/" + userId + "/Mon.png");
                            MonImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(UserStats.this).load(uri).into(mood_mon);
                                }
                            });

                            Toast.makeText(UserStats.this, "월없음", Toast.LENGTH_SHORT).show();
                            mood_mon.setImageResource(R.drawable.no);
                            mood_mon_text.setText("멘트 없음");
                        } else if (Mon != null) {  //월요일 이미지가 없다면
                            Toast.makeText(UserStats.this, "월없음", Toast.LENGTH_SHORT).show();
                            mood_mon.setImageResource(R.drawable.no);
                            mood_mon_text.setText("멘트 없음");
                        }

                        if (Tue != null) { //화요일 이미지가 있다면
//                    Toast.makeText(UserStats.this, "화있음", Toast.LENGTH_SHORT).show();
                            StorageReference MonImage = storageReference.child("UserStatsImages/" + userId + "/Tue.png");
                            MonImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(UserStats.this).load(uri).into(mood_tue);
                                }
                            });
                        } else if (Tue == null) {
                            mood_tue.setImageResource(R.drawable.no);
                            mood_tue_text.setText("멘트 없음");
                        }

                        if (Wed != null) { //수요일 이미지가 있다면
//                    Toast.makeText(UserStats.this, "수있음", Toast.LENGTH_SHORT).show();
                            StorageReference MonImage = storageReference.child("UserStatsImages/" + userId + "/Wed.png");
                            MonImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(UserStats.this).load(uri).into(mood_wed);
                                }
                            });
                        } else if (Wed == null) {
                            mood_wed.setImageResource(R.drawable.no);
                            mood_wed_text.setText("멘트 없음");
                        }

                        if (Thu != null) { //목요일 이미지가 있다면
//                    Toast.makeText(UserStats.this, "목있음", Toast.LENGTH_SHORT).show();
                            StorageReference MonImage = storageReference.child("UserStatsImages/" + userId + "/Thu.png");
                            MonImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(UserStats.this).load(uri).into(mood_thu);
                                }
                            });
                        } else if (Thu == null) {
                            mood_thu.setImageResource(R.drawable.no);
                            mood_thu_text.setText("멘트 없음");
                        }

                        if (Fri != null) { //금요일 이미지가 있다면
//                    Toast.makeText(UserStats.this, "금있음", Toast.LENGTH_SHORT).show();
                            StorageReference MonImage = storageReference.child("UserStatsImages/" + userId + "/Fri.png");
                            MonImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(UserStats.this).load(uri).into(mood_fri);
                                }
                            });
                        } else if (Fri == null) {
                            mood_fri.setImageResource(R.drawable.no);
                            mood_fri_text.setText("멘트 없음");
                        }

                        if (Sat != null) { //토요일 이미지가 있다면
//                    Toast.makeText(UserStats.this, "토있음", Toast.LENGTH_SHORT).show();
                            StorageReference MonImage = storageReference.child("UserStatsImages/" + userId + "/Sat.png");
                            MonImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(UserStats.this).load(uri).into(mood_sat);
                                }
                            });
                        } else if (Sat == null) {
                            mood_sat.setImageResource(R.drawable.no);
                            mood_sat_text.setText("멘트 없음");
                        }

                        if (Sun == null) { //일요일 이미지가 있다면
                            Toast.makeText(UserStats.this, "일있음", Toast.LENGTH_SHORT).show();
                            StorageReference MonImage = storageReference.child("UserStatsImages/" + userId + "/Sun.png");
                            MonImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(UserStats.this).load(uri).into(mood_sun);
                                }
                            });
                        } else if (Sun != null) {
                            Toast.makeText(UserStats.this, "일없음", Toast.LENGTH_SHORT).show();
                            mood_sun.setImageResource(R.drawable.no);
                            mood_sun_text.setText("멘트 없음");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

         */
    }
}
