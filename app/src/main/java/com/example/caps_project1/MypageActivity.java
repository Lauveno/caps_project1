package com.example.caps_project1;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MypageActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MypageActivity extends Fragment {

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_IMAGE = 2;

    private static final int my_permission_request = 2051;

    private ImageView iv_profile;
    private int id_view;

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

        iv_profile = view.findViewById(R.id.iv_profile);
        Button btn_uploadPicture = view.findViewById(R.id.btn_uploadPicture);
        btn_uploadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        return view;
    }

    // onActivityCreated : 프래그먼트 생성 후 호출하는 함수
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


        }
*/
    }
}
