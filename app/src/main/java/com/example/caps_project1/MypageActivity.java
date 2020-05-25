package com.example.caps_project1;

import android.Manifest;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.LoginFilter;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.annotations.NotNull;

import static androidx.core.app.ActivityCompat.finishAffinity;



// 프래그먼트 생명주기 : onAttach() onCreate() onViewCreated()
// onActivityCreated() onResume()
public class MypageActivity extends Fragment {
    private FirebaseAuth mAuth;




//    private static final int my_permission_request = 101;


    private int id_view;

    private Context mContext;

    ViewPager pager;
    VPAdapter adapter;



    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    // onCreate : fragment가 생성될 때 호출되는 부분
    // onCreateView : onCreate 후에 화면을 구성할 때 호출
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.activity_mypage, container, false);


        TabLayout tabLayout = view.findViewById(R.id.mypageTabLayout);
        pager = view.findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(pager);

        VPAdapter adapter = new VPAdapter(getChildFragmentManager());
        adapter.addFragment(new fragment_mypage_1(), "내 정보");
        adapter.addFragment(new fragment_mypage_2(), "내 펫");
        adapter.addFragment(new fragment_mypage_3(), "다이어리");
        pager.setAdapter(adapter);


        return view;
    }


}
