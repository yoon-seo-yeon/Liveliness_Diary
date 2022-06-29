package com.example.liveliness_diary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

//다이어리 어댑터
public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.CustomViewHolder> {
    private ArrayList<UserDiaryView> arrayList;
    private Context context;

    public FirebaseStorage mStorage;   //스토리지
    public FirebaseDatabase mDatabase; //실시간 데이터베이스
    public StorageReference storageReference;
    public DatabaseReference databaseReference;

    public DiaryAdapter(ArrayList<UserDiaryView> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diary_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getGrapeProfile())
                .into(holder.user_profile);
        holder.diary_title.setText(arrayList.get(position).getDiaryTitle());
        holder.diary_content.setText(arrayList.get(position).getDiaryContent());
        holder.diary_date.setText(arrayList.get(position).getTodayDate());
        holder.diary_id.setText(arrayList.get(position).getUserId());

        //리스트 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = holder.diary_date.getText().toString();
                String userId = holder.diary_id.getText().toString();

                Intent intent = new Intent(context, DiaryClick.class);
                intent.putExtra("date", date);
                intent.putExtra("userId", userId);
                view.getContext().startActivity(intent);
            }
        });

        //삭제 버튼 클릭 이벤트
        holder.diary_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               final AlertDialog.Builder builder = new AlertDialog.Builder(context);
               builder.setTitle("삭제");
               builder.setMessage("해당 일기를 삭제하시겠습니까?");
               builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       String userId = holder.diary_id.getText().toString();
                       String image = holder.diary_title.getText().toString();
                       String date = holder.diary_date.getText().toString();

                       //스토리지 이미지 삭제
//                       onDeleteImage(userId, image, date);

                       arrayList.remove(holder.getAdapterPosition());
                       notifyItemRemoved(holder.getAdapterPosition());
                       notifyItemRangeChanged(holder.getAdapterPosition(), arrayList.size());

//                       Intent intent = new Intent(context, DiaryDelete.class);
//                       intent.putExtra("date", date);
//                       intent.putExtra("userId", userId);
//                       intent.putExtra("image", image);
//                       view.getContext().startActivity(intent);
                   }
               });
               builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       dialogInterface.cancel();
                   }
               });

               builder.show();
            }
        });
    }

    /*
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
                onDeleteDiary(Id, Date);
                Log.d("TAG", "PICK : " + "스토리지 성공2");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "PICK : " + "스토리지 실패");
            }
        });
    }
     */

    /*
    private void onDeleteDiary(String userId, String date) {
        mDatabase.getReference().child("diary_list").child(userId).child(date).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "삭제 완료!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "삭제 실패..", Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase.getReference().child("diary_list_view").child(userId).child(date).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(context, "삭제 완료!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, "삭제 실패..", Toast.LENGTH_SHORT).show();
            }
        });
    }
     */

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView user_profile; //사용자 프로필
        TextView diary_title, diary_content, diary_date, diary_id;    //다이어리 제목, 내용, 날짜
        ImageButton diary_delete, diary_edit;   //다이어리 삭제, 수정

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.user_profile = itemView.findViewById(R.id.user_profile);
            this.diary_title = itemView.findViewById(R.id.diary_title);
            this.diary_content = itemView.findViewById(R.id.diary_content);
            this.diary_date = itemView.findViewById(R.id.diary_date);
            this.diary_id = itemView.findViewById(R.id.diary_id);
            this.diary_delete = itemView.findViewById(R.id.diary_delete);
            this.diary_edit = itemView.findViewById(R.id.diary_edit);
        }
    }
}
