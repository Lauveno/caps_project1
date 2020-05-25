package com.example.caps_project1;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsActivity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private String htmlPageUrl = "https://www.cabn.kr/news/section.html?sec_no=8";
    //파싱할 홈페이지의 URL주소, 동물뉴스 웹페이지

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    //private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<String> mDataset = new ArrayList<String>(Arrays.asList("첫 번째 뉴스의 제목","두 번째 뉴스의 제목","세 번째 뉴스의 제목"));
    private Context mContext;
    private Data mData;

    public NewsActivity() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewsActivity newInstance(String param1, String param2) {
        NewsActivity fragment = new NewsActivity();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareData();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_news,container,false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        mAdapter = new RecyclerAdapter(mDataset);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void prepareData(){
        mDataset.add(new String("news1"));
        mDataset.add(new String("news2"));
        //oncreate 될때마다 불러오기 때문에 refresh 시킬 수 있는 code로 수정하기
        //지금은 페이지 왔다갔다 하면 new1,2가 추가만 됨
    }

}