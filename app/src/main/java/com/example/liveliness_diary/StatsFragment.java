package com.example.liveliness_diary;

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

import java.util.Random;

public class StatsFragment extends Fragment {
    //사용자 아이디
    String userId;

    //이미지
    ImageView mood_sun, mood_mon, mood_tue, mood_wed, mood_thu, mood_fri, mood_sat;

    //글들
    TextView mood_sun_text, mood_mon_text, mood_tue_text, mood_wed_text, mood_thu_text, mood_fri_text, mood_sat_text;

    private FirebaseStorage mStorage;   //스토리지
    private FirebaseDatabase mDatabase; //실시간 데이터베이스
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    Uri Friuri;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ViewGroup view = (ViewGroup)inflater.inflate(R.layout.mood_stats, container, false);

        userId = this.getArguments().getString("userId");
        Log.d("TAG", "PICK stats userId : " + userId);

        mood_mon = view.findViewById(R.id.mood_mon);
        mood_tue = view.findViewById(R.id.mood_tue);
        mood_wed = view.findViewById(R.id.mood_wed);
        mood_thu = view.findViewById(R.id.mood_thu);
        mood_fri = view.findViewById(R.id.mood_fri);
        mood_sat = view.findViewById(R.id.mood_sat);
        mood_sun = view.findViewById(R.id.mood_sun);

        mood_mon_text = view.findViewById(R.id.mood_mon_text);
        mood_tue_text = view.findViewById(R.id.mood_tue_text);
        mood_wed_text = view.findViewById(R.id.mood_wed_text);
        mood_thu_text = view.findViewById(R.id.mood_thu_text);
        mood_fri_text = view.findViewById(R.id.mood_fri_text);
        mood_sat_text = view.findViewById(R.id.mood_sat_text);
        mood_sun_text = view.findViewById(R.id.mood_sun_text);

        mStorage = FirebaseStorage.getInstance();   //스토리지
        storageReference = mStorage.getReference();

        mDatabase = FirebaseDatabase.getInstance(); //실시간 데이터베이스
        databaseReference = mDatabase.getReference();

        Random random = new Random();   //랜덤 발생

        //행복, 만족, 보통, 피곤, 슬픔, 화남
        String happy[] = {"오늘 하루 너무 기뻤어!", "행복한 하루였다!", "웃음 가득한 하루였어!", "기분 좋은 하루였어!", "오늘 하루 짱!"};
        String satis[] = {"오늘 하루 너무 만족스럽다!", "만족스러운 하루였다!", "괜찮은 하루였어!", "나쁘지않은 하루였어!", "오늘 하루 괜찮네!"};
        String usual[] = {"오늘 하루 너무 괜찮네!", "괜찮은 하루였다!", "보통과 같은 하루였어!", "괜찮은 하루였어!", "오늘 괜찮네!"};
        String tired[] = {"오늘 하루 너무 힘들었어..", "피곤한 하루였다..", "하쒸.. 너무 피곤해ㅜㅜ", "피곤해 죽겠다..", "오늘 너무 피곤해.. 자고싶어.."};
        String sad[] = {"오늘 하루 너무 슬퍼..", "슬픈 하루였다..", "눈물나는 하루였다..", "핫쒸.. 눈물나와", "오늘 너무 슬퍼.. 울고싶다"};
        String angry[] = {"오늘 하루 너무 화나!!", "화 가득한 하루였다..!", "울분나는 하루였다!", "화가 난다!!!","화나 죽겠어!!"};
        String no[] = {"바쁜 하루였나보네?", "기록이 없어.__.", "기록되지 않은 하루야,_,", "이 날도 수고 많았어!","이 날은 기록이 없어!"};

        //Mon
        databaseReference.child("user_moodstats").child(userId).child("mon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Mon = snapshot.getValue(String.class);
                Log.d("TAG", "PICK 월ㅋ : " + Mon);

                if(Mon.equals("mon")) {
                    mood_mon.setImageResource(R.drawable.noimage);

                    int rand = random.nextInt(5);
                    mood_mon_text.setText(no[rand]);
                }

                else {
                    int rand = random.nextInt(5);

                    //mon_text가져오기
                    databaseReference.child("user_moodstats").child(userId).child("mon_text").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String mon_text = snapshot.getValue(String.class);
                            if(mon_text.equals("happy")) {
                                mood_mon.setImageResource(R.drawable.grape_happy);
                                mood_mon_text.setText(happy[rand]);
                            }
                            else if(mon_text.equals("satis")) {
                                mood_mon.setImageResource(R.drawable.grape_satis);
                                mood_mon_text.setText(satis[rand]);
                            }
                            else if(mon_text.equals("usual")) {
                                mood_mon.setImageResource(R.drawable.grape_usual);
                                mood_mon_text.setText(usual[rand]);
                            }
                            else if(mon_text.equals("tired")) {
                                mood_mon.setImageResource(R.drawable.grape_tired);
                                mood_mon_text.setText(tired[rand]);
                            }
                            else if(mon_text.equals("sad")) {
                                mood_mon.setImageResource(R.drawable.grape_sad);
                                mood_mon_text.setText(sad[rand]);
                            }
                            else if(mon_text.equals("angry")) {
                                mood_mon.setImageResource(R.drawable.grape_angry);
                                mood_mon_text.setText(angry[rand]);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    /*
                    StorageReference MonImage = storageReference.child("UserStatsImages/" + userId + "/Mon");
                    MonImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(StatsFragment.this).load(uri).into(mood_mon);

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

                     */
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
                    mood_tue.setImageResource(R.drawable.noimage);
                    int rand = random.nextInt(5);
                    mood_tue_text.setText(no[rand]);
                }

                else {
                    int rand = random.nextInt(5);

                    //tue_text가져오기
                    databaseReference.child("user_moodstats").child(userId).child("tue_text").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String tue_text = snapshot.getValue(String.class);

                            if(tue_text.equals("happy")) {
                                mood_tue.setImageResource(R.drawable.grape_happy);
                                mood_tue_text.setText(happy[rand]);
                            }
                            else if(tue_text.equals("satis")) {
                                mood_tue.setImageResource(R.drawable.grape_satis);
                                mood_tue_text.setText(satis[rand]);
                            }
                            else if(tue_text.equals("usual")) {
                                mood_tue.setImageResource(R.drawable.grape_usual);
                                mood_tue_text.setText(usual[rand]);
                            }
                            else if(tue_text.equals("tired")) {
                                mood_tue.setImageResource(R.drawable.grape_tired);
                                mood_tue_text.setText(tired[rand]);
                            }
                            else if(tue_text.equals("sad")) {
                                mood_tue.setImageResource(R.drawable.grape_sad);
                                mood_tue_text.setText(sad[rand]);
                            }
                            else if(tue_text.equals("angry")) {
                                mood_tue.setImageResource(R.drawable.grape_angry);
                                mood_tue_text.setText(angry[rand]);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    /*
                    StorageReference TueImage = storageReference.child("UserStatsImages/" + userId + "/Tue");
                    TueImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(StatsFragment.this).load(uri).into(mood_tue);

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

                     */
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
                    mood_wed.setImageResource(R.drawable.noimage);
                    int rand = random.nextInt(5);
                    mood_wed_text.setText(no[rand]);
                }

                else {
                    int rand = random.nextInt(5);

                    //wed_text가져오기
                    databaseReference.child("user_moodstats").child(userId).child("wed_text").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String wed_text = snapshot.getValue(String.class);

                            if(wed_text.equals("happy")) {
                                mood_wed.setImageResource(R.drawable.grape_happy);
                                mood_wed_text.setText(happy[rand]);
                            }
                            else if(wed_text.equals("satis")) {
                                mood_wed.setImageResource(R.drawable.grape_satis);
                                mood_wed_text.setText(satis[rand]);
                            }
                            else if(wed_text.equals("usual")) {
                                mood_wed.setImageResource(R.drawable.grape_usual);
                                mood_wed_text.setText(usual[rand]);
                            }
                            else if(wed_text.equals("tired")) {
                                mood_wed.setImageResource(R.drawable.grape_tired);
                                mood_wed_text.setText(tired[rand]);
                            }
                            else if(wed_text.equals("sad")) {
                                mood_wed.setImageResource(R.drawable.grape_sad);
                                mood_wed_text.setText(sad[rand]);
                            }
                            else if(wed_text.equals("angry")) {
                                mood_wed.setImageResource(R.drawable.grape_angry);
                                mood_wed_text.setText(angry[rand]);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    /*
                    StorageReference WedImage = storageReference.child("UserStatsImages/" + userId + "/Wed");
                    WedImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(StatsFragment.this).load(uri).into(mood_wed);

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

                     */
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
                    mood_thu.setImageResource(R.drawable.noimage);
                    int rand = random.nextInt(5);
                    mood_thu_text.setText(no[rand]);
                }

                else {
                    int rand = random.nextInt(5);

                    //thu_text가져오기
                    databaseReference.child("user_moodstats").child(userId).child("thu_text").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String thu_text = snapshot.getValue(String.class);

                            if(thu_text.equals("happy")) {
                                mood_thu.setImageResource(R.drawable.grape_happy);
                                mood_thu_text.setText(happy[rand]);
                            }
                            else if(thu_text.equals("satis")) {
                                mood_thu.setImageResource(R.drawable.grape_satis);
                                mood_thu_text.setText(satis[rand]);
                            }
                            else if(thu_text.equals("usual")) {
                                mood_thu.setImageResource(R.drawable.grape_usual);
                                mood_thu_text.setText(usual[rand]);
                            }
                            else if(thu_text.equals("tired")) {
                                mood_thu.setImageResource(R.drawable.grape_tired);
                                mood_thu_text.setText(tired[rand]);
                            }
                            else if(thu_text.equals("sad")) {
                                mood_thu.setImageResource(R.drawable.grape_sad);
                                mood_thu_text.setText(sad[rand]);
                            }
                            else if(thu_text.equals("angry")) {
                                mood_thu.setImageResource(R.drawable.grape_angry);
                                mood_thu_text.setText(angry[rand]);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    /*
                    StorageReference ThuImage = storageReference.child("UserStatsImages/" + userId + "/Thu");
                    ThuImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(StatsFragment.this).load(uri).into(mood_thu);

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

                     */
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
                    mood_fri.setImageResource(R.drawable.noimage);
                    int rand = random.nextInt(5);
                    mood_fri_text.setText(no[rand]);
                }

                else {
                    int rand = random.nextInt(5);

                    //fri_text가져오기
                    databaseReference.child("user_moodstats").child(userId).child("fri_text").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String fri_text = snapshot.getValue(String.class);

                            if(fri_text.equals("happy")) {
                                mood_fri.setImageResource(R.drawable.grape_happy);
                                mood_fri_text.setText(happy[rand]);
                            }
                            else if(fri_text.equals("satis")) {
                                mood_fri.setImageResource(R.drawable.grape_satis);
                                mood_fri_text.setText(satis[rand]);
                            }
                            else if(fri_text.equals("usual")) {
                                mood_fri.setImageResource(R.drawable.grape_usual);
                                mood_fri_text.setText(usual[rand]);
                            }
                            else if(fri_text.equals("tired")) {
                                mood_fri.setImageResource(R.drawable.grape_tired);
                                mood_fri_text.setText(tired[rand]);
                            }
                            else if(fri_text.equals("sad")) {
                                mood_fri.setImageResource(R.drawable.grape_sad);
                                mood_fri_text.setText(sad[rand]);
                            }
                            else if(fri_text.equals("angry")) {
                                mood_fri.setImageResource(R.drawable.grape_angry);
                                mood_fri_text.setText(angry[rand]);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    /*
                    StorageReference FriImage = storageReference.child("UserStatsImages/" + userId + "/Fri");
                    FriImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(StatsFragment.this).load(uri).into(mood_fri);
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
                     */
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("TAG", "PICK : " + "통계 오류");
            }
        });

        //Sat
        databaseReference.child("user_moodstats").child(userId).child("sat").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Sat = snapshot.getValue(String.class);
                Log.d("TAG", "PICK 토ㅋ : " + Sat);

                if(Sat.equals("sat")) {
                    mood_sat.setImageResource(R.drawable.noimage);
                    int rand = random.nextInt(5);
                    mood_sat_text.setText(no[rand]);
                }

                else if(snapshot.exists()){
                    int rand = random.nextInt(5);

                    //sat_text가져오기
                    databaseReference.child("user_moodstats").child(userId).child("sat_text").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String sat_text = snapshot.getValue(String.class);

                            if(sat_text.equals("happy")) {
                                mood_sat.setImageResource(R.drawable.grape_happy);
                                mood_sat_text.setText(happy[rand]);
                            }
                            else if(sat_text.equals("satis")) {
                                mood_sat.setImageResource(R.drawable.grape_satis);
                                mood_sat_text.setText(satis[rand]);
                            }
                            else if(sat_text.equals("usual")) {
                                mood_sat.setImageResource(R.drawable.grape_usual);
                                mood_sat_text.setText(usual[rand]);
                            }
                            else if(sat_text.equals("tired")) {
                                mood_sat.setImageResource(R.drawable.grape_tired);
                                mood_sat_text.setText(tired[rand]);
                            }
                            else if(sat_text.equals("sad")) {
                                mood_sat.setImageResource(R.drawable.grape_sad);
                                mood_sat_text.setText(sad[rand]);
                            }
                            else if(sat_text.equals("angry")) {
                                mood_sat.setImageResource(R.drawable.grape_angry);
                                mood_sat_text.setText(angry[rand]);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    /*
                    StorageReference SatImage = storageReference.child("UserStatsImages/" + userId + "/Sat");
                    SatImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(StatsFragment.this).load(uri).into(mood_sat);

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

                     */
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
                    mood_sun.setImageResource(R.drawable.noimage);
                    int rand = random.nextInt(5);
                    mood_sun_text.setText(no[rand]);
                }

                else {
                    int rand = random.nextInt(5);

                    //sun_text가져오기
                    databaseReference.child("user_moodstats").child(userId).child("sun_text").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String sun_text = snapshot.getValue(String.class);

                            if(sun_text.equals("happy")) {
                                mood_sun.setImageResource(R.drawable.grape_happy);
                                mood_sun_text.setText(happy[rand]);
                            }
                            else if(sun_text.equals("satis")) {
                                mood_sun.setImageResource(R.drawable.grape_satis);
                                mood_sun_text.setText(satis[rand]);
                            }
                            else if(sun_text.equals("usual")) {
                                mood_sun.setImageResource(R.drawable.grape_usual);
                                mood_sun_text.setText(usual[rand]);
                            }
                            else if(sun_text.equals("tired")) {
                                mood_sun.setImageResource(R.drawable.grape_tired);
                                mood_sun_text.setText(tired[rand]);
                            }
                            else if(sun_text.equals("sad")) {
                                mood_sun.setImageResource(R.drawable.grape_sad);
                                mood_sun_text.setText(sad[rand]);
                            }
                            else if(sun_text.equals("angry")) {
                                mood_sun.setImageResource(R.drawable.grape_angry);
                                mood_sun_text.setText(angry[rand]);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    /*
                    StorageReference SunImage = storageReference.child("UserStatsImages/" + userId + "/Sun");
                    SunImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Glide.with(StatsFragment.this).load(uri).into(mood_sun);

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

                     */
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

//        public void

        return view;
    }
}
