package com.example.liveliness_diary;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.List;

//회원가입
public class Join extends AppCompatActivity {
    private static final int PICK_FROM_ALBUM = 1; //앨범에서 사진 가져오기
    private Uri imageUri;
    private String pathUri;
    private File tempFile;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase; //실시간 데이터베이스
    private FirebaseStorage mStorage;   //스토리지
    //DatabaseReference는 데이터베이스의 특정 위치로 연결
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    EditText userId, userPass;
    ImageView userProfile;
    Button signBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign);

        userId = findViewById(R.id.userId);    //사용자 아이디
        userPass = findViewById(R.id.userPass);    //사용자 비밀번호
        userProfile = findViewById(R.id.userProfile); //사용자 프로필
        signBtn = findViewById(R.id.signBtn);    //회원가입 버튼

        //Authentication, Database, Storage 초기화
        //getInstance : 싱글톤 방식으로 객체 관리, 안의 value를 관리하고 싶으면 이용
//        mAuth = FirebaseAuth.getInstance(); 안씀

        mDatabase = FirebaseDatabase.getInstance(); //실시간 데이터베이스
        databaseReference = mDatabase.getReference();

        mStorage = FirebaseStorage.getInstance();
        //storageReference = FirebaseStorage.getInstance().getReference();    //스토리지
        storageReference = mStorage.getReference();    //스토리지

        userProfile.setOnClickListener(new View.OnClickListener() { //사용자 프로필 누르면
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK); //사진 가져오기
//                intent.setType(MediaStore.Images.Media.CONTENT_TYPE); //기기 기본 갤러리 접근
//                startActivityForResult.launch(intent, 1);


                /*오류
                Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(cameraApp.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(cameraApp, 2);
                }
                 */


                Intent intent = new Intent(Intent.ACTION_PICK);    //사진 가져오기
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);   //기기 기본 갤러리 접근
                startActivityForResult(intent, 1);


//                Intent galleryIntent = new Intent();
//                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/");
//                activityResult.launch(galleryIntent);
            }
        });



        signBtn.setOnClickListener(new View.OnClickListener() { //회원가입 버튼을 누르면
            @Override
            public void onClick(View view) {
                String Id = userId.getText().toString();
                String Pass = userPass.getText().toString();

                if(TextUtils.isEmpty(Id) || TextUtils.isEmpty(Pass)) {
                    Toast.makeText(Join.this, "정보를 바르게 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    //선택한 이미지가 있다면
                    if(imageUri != null) {
                        uploadToFirebase(imageUri);
                    } else {    //선택한 이미지가 없다면
                        Toast.makeText(Join.this, "사진을 선택해주세요!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)   {
            if(resultCode == RESULT_OK) {
                imageUri = data.getData();
                Log.d("TAG", "PICK_FROM_ALBUM imageUri : " + imageUri);
                userProfile.setImageURI(imageUri);
            }
        }
//        else if(resultCode == 2) {
//            if(resultCode == RESULT_OK) {
//                Bundle extras = data.getExtras();
//                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                Log.d("TAG", "PICK_FROM_ALBUM imageBitmap : " + imageBitmap);
//                userProfile.setImageBitmap(imageBitmap);
//            }
//        }
    }

    //카메라 있는지 확인 <- 흠.... 안되는듯
    private boolean isExistsCameraApplication() {
        PackageManager packageManager = getPackageManager();
        Intent cameraApp = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> cameraApps = packageManager.queryIntentActivities(cameraApp, PackageManager.MATCH_DEFAULT_ONLY);

        return cameraApps.size() > 0;
    }

    //사진 가져오기
    /*
    ActivityResultLauncher<Intent> activityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        Log.d("TAG", "PICK_FROM_ALBUM imageUri : " + imageUri);
                        userProfile.setImageURI(imageUri);
                    }
                }
            });
     */

    //파이어베이스 이미지 업로드
    /*
    private void uploadToFirebase(Uri uri) {
        //중복 검사 코드
        Toast.makeText(this, "uri1 : " + uri.toString(), Toast.LENGTH_SHORT).show();
        String Id = userId.getText().toString();
        databaseReference.child("user_list").child(Id).child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Log.d("TAG", "PICK_FROM_ALBUM value : " + value);

                //이미 있는 아이디라면
                if (value != null) {
                    Toast.makeText(Join.this, "이미 존재하는 아이디입니다!", Toast.LENGTH_SHORT).show();
                } else {    //없는 아이디라면
                    //아이디, 비밀번호, 이미지 User에 담기
                    String Id = userId.getText().toString();
                    String Pass = userPass.getText().toString();

                    Toast.makeText(Join.this, "uri2 : " + uri.toString(), Toast.LENGTH_SHORT).show();

                    StorageReference fileRef = storageReference.child("userProfileImages/" + Id);
                    fileRef.putFile(uri);

                    User user = new User(Id, Pass, uri.toString());

                    mDatabase.getReference().child("user_list").child(Id).setValue(user);
                    Toast.makeText(Join.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();

                    //회원가입 완료 후 로그인 화면 이동
                    Intent intent = new Intent(Join.this, Login.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

     */


    private void uploadToFirebase(Uri uri) {
        //중복 검사 코드
        Toast.makeText(this, "uri1 : " + uri.toString(), Toast.LENGTH_SHORT).show();
        String Id = userId.getText().toString();
        databaseReference.child("user_list").child(Id).child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                Log.d("TAG", "PICK_FROM_ALBUM value : " + value);

                //이미 있는 아이디라면
                if (value != null) {
                    Toast.makeText(Join.this, "이미 존재하는 아이디입니다!", Toast.LENGTH_SHORT).show();
                } else {    //없는 아이디라면
                    StorageReference fileRef = storageReference.child("userProfileImages/" + Id);
                    fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //아이디, 비밀번호, 이미지 User에 담기
                                    Toast.makeText(Join.this, "uri2 : " + uri.toString(), Toast.LENGTH_SHORT).show();

                                    String Id = userId.getText().toString();
                                    String Pass = userPass.getText().toString();

                                    User user = new User(Id, Pass, uri.toString());

                                    mDatabase.getReference().child("user_list").child(Id).setValue(user);

                                    //새 회원이면 통계 DB 새로 만들어주기
                                    UserStats stats = new UserStats(Id, "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                                    databaseReference.child("user_moodstats").child(Id).setValue(stats);

                                    //새 회원이면 글씨 색상 DB 새로 만들어주기
                                    User_mood user_mood = new User_mood("color", "");
                                    databaseReference.child("user_textcolor").child(Id).setValue(user_mood);

                                    //새 회원이면 포도 프로필 DB 새로 만들어주기
                                    User_grape user_grape = new User_grape("grape", "");
                                    databaseReference.child("user_grape").child(Id).setValue(user_grape);
                                    
                                    Toast.makeText(Join.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();

                                    //회원가입 완료 후 로그인 화면 이동
                                    Intent intent = new Intent(Join.this, Login.class);
                                    startActivity(intent);
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
    }


    /*전꺼(이미지 먼저 넣고 정보 넣기)*/
    /*
    private void uploadToFirebase(Uri uri) {
        Toast.makeText(Join.this, "uri1 : " + uri.toString(), Toast.LENGTH_SHORT).show();
        String Id = userId.getText().toString();
        StorageReference fileRef = storageReference.child("userProfileImages/" + Id);

        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //중복 검사 코드
                        String Id = userId.getText().toString();
                        mDatabase.getReference().child("user_list").child(Id).child("userId").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String value = snapshot.getValue(String.class);
                                Log.d("TAG", "PICK_FROM_ALBUM value : " + value);

                                //이미 있는 아이디라면
                                if (value != null) {
                                    Toast.makeText(Join.this, "이미 존재하는 아이디입니다!", Toast.LENGTH_SHORT).show();
                                } else {    //없는 아이디라면
                                    //아이디, 비밀번호, 이미지 User에 담기
                                    String Id = userId.getText().toString();
                                    String Pass = userPass.getText().toString();

                                    Toast.makeText(Join.this, "uri2 : " + uri.toString(), Toast.LENGTH_SHORT).show();

                                    User user = new User(Id, Pass, uri.toString());
                                    mDatabase.getReference().child("user_list").child(Id).setValue(user);
                                    Toast.makeText(Join.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();

                                    //회원가입 완료 후 로그인 화면 이동
                                    Intent intent = new Intent(Join.this, Login.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Join.this, "회원가입 실패!", Toast.LENGTH_SHORT).show();
            }
        });
    }

     */



    //파일 타입 가져오기
    private String getFileExtension(Uri uri) {
        //ContentResolver : 앱 사이에서 각종 데이터를 공유할 수 있게 해주는 컴포넌트
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
}
