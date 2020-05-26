package com.example.caps_project1;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class fragment_mypage_1 extends Fragment {
    private FirebaseAuth mAuth;

    private static final int REQUEST_CODE = 101;
    private static final int PERMISSON_CAMERA = 1111;
    private static final int TAKE_PHOTO = 2222;
    private static final int TAKE_ALBUM = 3333;
    private static final int CROP_IMAGE = 4444;

    String mCurrentPhotoPath;
    Uri imageURI;
    Uri photoURI, albumURI;

    private int id_view;
    private ImageView iv_profile;

    private Context mContext;

    // popup 메뉴 생성 코드
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == 1) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // onCreate : fragment가 생성될 때 호출되는 부분
    // onCreateView : onCreate 후에 화면을 구성할 때 호출
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mypage_1, container, false);

        mAuth = FirebaseAuth.getInstance();

        iv_profile = view.findViewById(R.id.iv_profile);

        // 이미지를 클릭 시 팝업메뉴가 먼저 나온다.
//        iv_profile.setOnClickListener(new View.OnClickListener() {
//
//            // popup menu 생성
//            // inflater 를 통해 popup menu를 구현, 어떤 메뉴를 선택하는지에 따라서 결과 값이 달라진다.
//            // 카메라, 갤러리 직접 접근하는 내부 함수 만들기
//            @Override
//            public void onClick(View v) {
//
//                PopupMenu pop = new PopupMenu(mContext, v);
//                pop.getMenuInflater().inflate(R.menu.menu_popup, pop.getMenu());
//
//                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            // 카메라
//                            case R.id.one:
//                                captureCamera();
//                                break;
//
//                            // 갤러리에서 권한 받아오기
//                            case R.id.two:
//                                getAlbum();
//                                break;
//
//                            // 기본 이미지로 변경
//                            case R.id.three:
//                                iv_profile.setImageResource(R.drawable.user2);
//                        }
//                        return true;
//                    }
//                });
//                pop.show();
//                checkPermission();
//            }
//        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                Intent intent = new Intent();

                // 구글 갤러리 접근
                 intent.setType("image/*");

                // 기기 기본 갤러리 접근
//                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


        // 로그아웃
        Button logoutButton = view.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogoutDialog();
            }
        });

        // 회원 탈퇴
        Button revokeButton = view.findViewById(R.id.revokeButton);
        revokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RevokeDialog();
            }
        });

        return view;
    }


    // 회원 탈퇴 메소드
    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    }


    // 로그아웃 다이얼로그
    void LogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("로그아웃 하시겠습니까?").setPositiveButton("확인",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(mContext, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();

                        FirebaseAuth.getInstance().signOut();
                        startLoginActivity();

                    }
                }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }


    // 회원 탈퇴 다이얼로그
    void RevokeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("회원 탈퇴").setMessage("탈퇴 하시겠습니까?").setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(mContext, "탈퇴 되었습니다.", Toast.LENGTH_SHORT).show();

                        revokeAccess();

                        // 해당 앱의 루트 액티비트를 종료시킨다.
                        getActivity().finishAffinity();
                    }
                }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "취소 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();
    }


    // ActivityCompat.checkSelfPermission : 카메라 및 외부 저장소 퍼미션 상태를 체크
    private void checkPermission() {

        String temp = "";

        // 파일 읽기 권한
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }

        // 파일 쓰기 권한
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }

        if (TextUtils.isEmpty(temp) == false) {
            ActivityCompat.requestPermissions(getActivity(), temp.trim().split(" ", 1), 1);
        } else {
            Toast.makeText(mContext, "권한을 모두 허용", Toast.LENGTH_SHORT).show();
        }


        // 거부 : PackageManager.PERMISSION_DENIED 를 리턴한다.
//        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
//                PackageManager.PERMISSION_GRANTED) {
//
//            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
//                    (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA))) {
//                new AlertDialog.Builder(mContext).setTitle("알림").setMessage("저장소 권한이 거부되었습니다.").setNeutralButton("설정", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        intent.setData(Uri.parse("package: " + getActivity().getPackageName()));
//                        startActivity(intent);
//                    }
//                }).setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int i) {
//                        getActivity().finish();
//                    }
//                }).setCancelable(false).create().show();
//            } else {
//                // 퍼미션 요청 , 요청결과는 onRequestPermissionResults 에서 수신
//                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSON_CAMERA);
//            }
//        }

//        int permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
//        if (permission == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 0);
//        }
//        else {
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//            startActivityForResult(intent, 1);
//        }
    }


    // ActivityCompat.requestPermissions 를 사용한 퍼미션 요청의 결과를 리턴받는 메소드
    // 권한에 대한 응답이 있을 때 작동하는 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // 권한 허용
        if (requestCode == 1) {
            int length = permissions.length;
            for(int i=0; i<length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("maypage", "권한 허용 : " + permissions[i]);
            }
        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 0) {
//            if (grantResults[0] == 0) {
//                Toast.makeText(mContext, "카메라 권한 승인 완료", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(mContext, "카메라 권한 승인 거절", Toast.LENGTH_SHORT).show();
//
//            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && requestCode == Activity.RESULT_OK) {

                try {
                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());

                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    iv_profile.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_CODE  && requestCode == Activity.RESULT_CANCELED) {
                Toast.makeText(mContext, "사진 선택 취소", Toast.LENGTH_SHORT).show();
            }
        }
//        switch (requestCode) {
//            case TAKE_PHOTO:
//                if (requestCode == Activity.RESULT_OK) {
//                    try {
//                        Log.i("REQUEST_TAKE_PHOTO", "OK");
//                        galleryAddPic();
//
//                        iv_profile.setImageURI(imageURI);
//                    } catch (Exception e) {
//                        Log.e("REQUEST_TAKE_PHOTO", e.toString());
//                    }
//                } else {
//                    Toast.makeText(mContext, "저장공간에 접근할 수 없습니다.", Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//            case TAKE_ALBUM:
//                if (requestCode == Activity.RESULT_OK) {
//                    if (data.getData() != null) {
//                        try {
//                            File albumFile = null;
//                            albumFile = createImageFile();
//                            photoURI = data.getData();
//                            albumURI = Uri.fromFile(albumFile);
//                            cropImage();
//
//                        } catch (IOException e) {
//                            Log.e("TAKE_ALBUM_SINGLE_ERROR", e.toString());
//                        }
//                    }
//                }
//                break;
//
//            case CROP_IMAGE:
//                if (requestCode == Activity.RESULT_OK) {
//                    galleryAddPic();
//
//                    // 사진 변환 에러
//                    iv_profile.setImageURI(albumURI);
//                }
//                break;
//        }
//    }

    // 갤러리에 사진이 추가되고 선택할 수 있다.
    private void getAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);

//        intent.setType("image/*");
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);

        startActivityForResult(intent, TAKE_ALBUM);
    }


    // 사진 촬영 함수
    private void captureCamera() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    Uri providerUri = FileProvider.getUriForFile(mContext, getActivity().getPackageName(), photoFile);
                    imageURI = providerUri;

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerUri);
                    startActivityForResult(takePictureIntent, TAKE_PHOTO);
                }
            } else {
                Toast.makeText(mContext, "접근 불가능 합니다", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }


    // 촬영, 크롭된 사진에 대한 이미지 저장 함수
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";
        File imageFile = null;
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures");

        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();

        return imageFile;
    }


    // 사진을 크롭하는 함수
    // 비율을 1:1 로 크롭, 크롭한 사진은 인텐트로 넘겨서 앨범에 저장할 수 있게 한다.
    public void cropImage() {
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                .setDataAndType(photoURI, "image/*");
        cropIntent.putExtra("aspectx", 1)
                .putExtra("aspecty", 1)
                .putExtra("scale", true)
                .putExtra("output", albumURI);
        startActivityForResult(cropIntent, CROP_IMAGE);
    }


    // 갤러리에 사진 추가 함수
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(mCurrentPhotoPath);
        Uri contentURI = Uri.fromFile(file);
        mediaScanIntent.setData(contentURI);

        mContext.sendBroadcast(mediaScanIntent);
        Toast.makeText(mContext, "앨범에 저장되었습니다", Toast.LENGTH_SHORT).show();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }


}
