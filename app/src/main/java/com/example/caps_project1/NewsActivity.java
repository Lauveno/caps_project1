package com.example.caps_project1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.AsyncTask;
import org.jsoup.Jsoup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsActivity extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private String htmlPageUrl = "http://www.naver.com/"; //파싱할 홈페이지의 URL주소


    // TODO: Rename and change types of parameters

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_news, container, false);
    }
}