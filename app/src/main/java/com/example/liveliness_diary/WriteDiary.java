package com.example.liveliness_diary;



import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//다이어리 쓰기
public class WriteDiary extends AppCompatActivity {
    ImageView user_profile;     //사용자 프로필
    EditText diary_title, diary_content;   //다이어리 제목, 내용
    ImageButton diary_check; //다이어리 다 쓰고 난 뒤 버튼
    ImageView diary_image;  //다이어리 이미지
    Button btn_gallery, btn_photo;  //갤러리, 사진 찍기
    ProgressDialog dialog;  //프로그레스바 보여주기
    ProgressBar progressBar;    //프로그레스바 원형

    private Uri diaryImageUri;  //다이어리 이미지
    private String mood_grape;    //포도 happy인지 ...
    private Uri grapeUrl;   //포도 이미지
    private String text_color;  //텍스트 컬러
    private String grapeimage;

    private Uri photoUri;
    private File photoFile;
    private String imageFilePath;

    private FirebaseStorage mStorage;   //스토리지
    private FirebaseDatabase mDatabase; //실시간 데이터베이스
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary_write);

        user_profile = findViewById(R.id.user_profile);
        diary_title = findViewById(R.id.diary_title);
        diary_content = findViewById(R.id.diary_content);
        diary_check = findViewById(R.id.diary_check);
        diary_image = findViewById(R.id.diary_image);
        btn_gallery = findViewById(R.id.btn_gallery);
        btn_photo = findViewById(R.id.btn_photo);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(false);
        progressBar.setProgress(100);

        mStorage = FirebaseStorage.getInstance();   //스토리지
        storageReference = mStorage.getReference();

        mDatabase = FirebaseDatabase.getInstance(); //실시간 데이터베이스
        databaseReference = mDatabase.getReference();

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        //포도 프로필 넣기
        databaseReference.child("user_grape").child(userId).child("mood_grape").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mood_grape = snapshot.getValue(String.class);
                
                if(mood_grape.equals("happy")) {    //포도가 happy라면
                    user_profile.setImageResource(R.drawable.grape_happy);
                    grapeimage = "happy";
                    grapeUrl = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_happy");
                }
                else if(mood_grape.equals("satis")) {
                    user_profile.setImageResource(R.drawable.grape_satis);
                    grapeimage = "satis";
                    grapeUrl = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_satis");
                }
                else if(mood_grape.equals("usual")) {
                    user_profile.setImageResource(R.drawable.grape_usual);
                    grapeimage = "usual";
                    grapeUrl = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_usual");
                }
                else if(mood_grape.equals("tired")) {
                    user_profile.setImageResource(R.drawable.grape_tired);
                    grapeimage = "tired";
                    grapeUrl = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_tired");
                }
                else if(mood_grape.equals("sad")) {
                    user_profile.setImageResource(R.drawable.grape_sad);
                    grapeimage = "sad";
                    grapeUrl = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_sad");
                }
                else if(mood_grape.equals("angry")) {
                    user_profile.setImageResource(R.drawable.grape_angry);
                    grapeimage = "angry";
                    grapeUrl = Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + "drawable" + "/" + "grape_angry");
                }

//                Glide.with(WriteDiary.this).load(grapeUrl).into(user_profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*
        diary_image.setOnClickListener(new View.OnClickListener() { //다이어리 이미지 누르면
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);    //갤러리
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 1);
            }
        });
         */

        //권한 체크
        TedPermission.with(getApplicationContext())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한이 필요합니다.")
                .setDeniedMessage("거부하셨습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

        //갤러리 버튼 누르면
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);    //갤러리
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 1);
            }
        });

        //사진 찍기
        btn_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager()) != null) {
                    photoFile = null;

                    try {
                        photoFile = createImageFile();
                        Log.d("TAG", "PICK photoFile : " + photoFile);
                    }catch(IOException e) {

                    }

                    if(photoFile != null) {
                        diaryImageUri = FileProvider.getUriForFile(getApplicationContext(), getPackageName(), photoFile);
                        Log.d("TAG", "PICK diaryImageUri : " + diaryImageUri);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, diaryImageUri);
                        startActivityForResult(intent, 2);
                    }
                }
            }
        });

        diary_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(diaryImageUri == null) { //비어있다면
                    Toast.makeText(WriteDiary.this, "사진을 선택해주세요!", Toast.LENGTH_SHORT).show();
                }
                else {
                    //다이어리 제목 없을 시 안됨
                    if(diary_title == null) {
                        Toast.makeText(WriteDiary.this, "제목을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    }
                    uploadDiary(diaryImageUri);
                }
            }
        });
    }

    //권한 요청
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("TAG", "PICK : " + "onRequestPermissionsResult");
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            Log.d("TAG", "PICK : " + "Permission");

        }
    }

    //다이어리 올리기
    private void uploadDiary(Uri uri) {
        dialog = new ProgressDialog(WriteDiary.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("기록되는 중입니다!");
        dialog.show();

        //userDiaryImage 스토리지에 다이어리 이미지 저장 후 리얼타임 DB에 다이어리 정보 저장
        StorageReference fileRef = storageReference.child("userDiaryImage/" + userId + "/" + diary_title.getText().toString());
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //textcolor 가져오기
                        databaseReference.child("user_textcolor").child(userId).child("mood_color").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                text_color = snapshot.getValue(String.class);

                                Log.d("TAG", "PICK grapeUrl : " + grapeUrl);

                                //다이어리 프로필 저장(포도 이미지, 텍스트 컬러, 다이어리 이미지)
                                uploadDiaryProfile(grapeUrl, text_color, uri);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
            }
        });
    }

    //프로필 업로드(포도프로필, 텍스트 색상, 다이어리 이미지)
    private void uploadDiaryProfile(Uri grape, String text_color, Uri diaryimage) {
        StorageReference fileRef = storageReference.child("UserDiaryProfile/" + userId + "/" + grapeimage);
        fileRef.putFile(grape).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //현재시간 가져오기
                        long now = System.currentTimeMillis();
                        //Date 형식으로 Convert
                        Date date = new Date(now);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String getTime = sdf.format(date);

                        UserDiary userDiary = new UserDiary(userId, uri.toString(), diary_title.getText().toString(), diary_content.getText().toString(),
                                diaryimage.toString(), getTime, text_color);
                        UserDiaryView userDiaryView = new UserDiaryView(userId, uri.toString(), diary_title.getText().toString(), diary_content.getText().toString(), getTime);

                        databaseReference.child("diary_list").child(userId).child(getTime).setValue(userDiary);
                        databaseReference.child("diary_list_view").child(userId).child(getTime).setValue(userDiaryView);

//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        FragmentTransaction transaction = fragmentManager.beginTransaction();
//                        DiaryFragment diaryFragment = new DiaryFragment();
//
//                        Bundle bundle = new Bundle();
//                        bundle.putString("userId", userId);
//                        diaryFragment.setArguments(bundle);

//                        transaction.replace(R.id.diary_write, diaryFragment).commitAllowingStateLoss();

                        Intent intent = new Intent(WriteDiary.this, MainActivity.class);
                        intent.putExtra("userId", userId);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    //ImageFile의 경로를 가져올 메서드 선언
    private File createImageFile() throws IOException {
        //파일이름을 세팅 및 저장경로 세팅
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "TEST_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    //사진
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)   {    //갤러리
            if(resultCode == RESULT_OK) {
                diaryImageUri = data.getData();
                Log.d("TAG", "PICK_FROM_ALBUM diaryImageUri : " + diaryImageUri);
                diary_image.setImageURI(diaryImageUri);
            }
        }
        else if(requestCode == 2) { //사진 찍기
            if(resultCode == RESULT_OK) {
                //이미지파일을 bitmap 변수에 초기화
                //BitmapFactory.decodeFile : 로컬에 존재하는 파일을 그대로 읽어올 때 쓴다.
                //파일 경로를 파라미터로 넘겨주면 FileInputstream을 만들어서 decodeStream을 한다
                Bitmap bitmap = BitmapFactory.decodeFile(imageFilePath);

                Log.d("TAG", "PICK bitmap : " + bitmap);
                Log.d("TAG", "PICK diaryImageUri2 : " + diaryImageUri);

                ExifInterface exif = null;

                try {
                    exif = new ExifInterface(imageFilePath);
                }catch (IOException e) {
                    e.printStackTrace();
                }

                //이미지의 회전각도를 구한다
                int exifOrientation;
                int exifDegree;

                if(exif != null) {
                    exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    exifDegree = exifOrientationToDegress(exifOrientation);
                }else {
                    exifDegree = 0;
                }

                //이미지 출력
                ((ImageView) findViewById(R.id.diary_image)).setImageBitmap(rotate(bitmap, exifDegree));
            }
        }
    }

    //사진의 돌아간 각도를 게산하는 메서드 선언
    private int exifOrientationToDegress(int exifOrientation) {
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        }
        else if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    //이미지를 회전시키는 메서드 선언
    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(WriteDiary.this, "권한이 허용됨", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(WriteDiary.this, "권한이 거부됨", Toast.LENGTH_SHORT).show();
        }
    };
}
