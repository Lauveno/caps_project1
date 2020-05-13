package com.example.caps_project1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MypageActivity#newInstance} factory method to
 * create an instance of this fragment.
 */

// 프래그먼트 생명주기 : onAttach() onCreate() onViewCreated()
// onActivityCreated() onResume()
public class MypageActivity extends Fragment {

    private static final int PERMISSON_CAMERA = 1111;
    private static final int TAKE_PHOTO = 2222;
    private static final int TAKE_ALBUM = 3333;
    private static final int CROP_IMAGE = 4444;

    String mCurrentPhotoPath;
    Uri imageURI;
    Uri phtoURI, albumURI;

    private static final int my_permission_request = 101;

    private ImageView iv_profile;
    private int id_view;

    Context thiscontext;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MypageActivity() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MypageActivity newInstance(String param1, String param2) {
        MypageActivity fragment = new MypageActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // onCreate : fragment가 생성될 때 호출되는 부분
    // onCreateView : onCreate 후에 화면을 구성할 때 호출
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_mypage, container, false);
        thiscontext = container.getContext();

        iv_profile = view.findViewById(R.id.iv_profile);
        Button btn_uploadPicture = view.findViewById(R.id.btn_uploadPicture);

        btn_uploadPicture.setOnClickListener(new View.OnClickListener() {

            // popup menu 생성
            // inflater 를 통해 popup menu를 구현, 어떤 메뉴를 선택하는지에 따라서 결과 값이 달라진다.
            // 카메라, 갤러리 직접 접근하는 내부 함수 만들기
            @Override
            public void onClick(View v) {
                int permission = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                if (permission == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 0);
                }
                else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 1);
                }

                PopupMenu pop = new PopupMenu(thiscontext, v);
                pop.getMenuInflater().inflate(R.menu.menu_popup, pop.getMenu());

                pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            // 카메라
                            case R.id.one:
                                captureCamera();
                                break;

                            // 갤러리에서 권한 받아오기
                            case R.id.two:
                                getAlbum();
                                break;
                        }
                        return true;
                    }
                });
                pop.show();
            }
        });


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0) {
            if (grantResults[0] == 0) {
                Toast.makeText(thiscontext, "카메라 권한 승인 완료", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(thiscontext, "카메라 권한 승인 거절", Toast.LENGTH_SHORT).show();

            }
        }
    }

    // 갤러리에 사진이 추가되고 선택할 수 있다.
    private void getAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (photoFile != null) {
                    Uri providerUri = FileProvider.getUriForFile(thiscontext, getActivity().getPackageName(), photoFile);
                    imageURI = providerUri;

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerUri);
                    startActivityForResult(takePictureIntent, TAKE_PHOTO);
                }
            } else {
                Toast.makeText(thiscontext, "접근 불가능 합니다", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 촬영, 크롭된 사진에 대한 이미지 저장 함수
    private File createImageFile() {
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
                .setDataAndType(phtoURI, "image/*");
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

        thiscontext.sendBroadcast(mediaScanIntent);
        Toast.makeText(thiscontext, "앨범에 저장되었습니다", Toast.LENGTH_SHORT).show();
    }

    // onActivityCreated : 프래그먼트 생성 후 호출하는 함수
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


        }

    }
}
