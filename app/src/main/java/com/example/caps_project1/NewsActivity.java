package com.example.caps_project1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

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

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private ArrayList<Data> mDataset = new ArrayList<Data>();
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_news,container,false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        return view;
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        newsData task = new newsData();
        task.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDataset.clear();
    }


    private class newsData extends AsyncTask<Void, Void, ArrayList<Data>>{

        private ProgressDialog progressDialog;
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("뉴스를 로딩중입니다");
            progressDialog.show();
        }
        @Override
        protected ArrayList<Data> doInBackground(Void... voids) {
            String htmlPageUrl = "https://search.naver.com/search.naver?where=news&sm=tab_jum&query=%EB%8F%99%EB%AC%BC&nso=so%3Ar%2Cp%3Aall%2Ca%3Aall";
            //파싱할 홈페이지의 URL주소, 동물뉴스 웹페이지

            try{
                //Document doc = Jsoup.connect(htmlPageUrl).followRedirects(true).get();
                Document doc = getDocument(htmlPageUrl);
                Elements elements = doc.select("ul.type01 li");

                for (Element element : elements) {

                    Data data = new Data();
//                    Data data2 = new Data();

                    Elements e = element.select("dt a._sp_each_title");
                    if (e != null && e.size()>0) {
                        data.setTitle(e.get(0).attr("title").substring(0, 20) + "...");
                        data.setContent(e.get(0).attr("href"));
                        mDataset.add(data);
                        Log.d("result: ","doc="+elements.text());
                        Log.d("result: ","doc="+data.getContent());
//                        Elements e2 = element.select("ul.relation_lst a");
//                        if (e2 != null && e2.size()>0){
//                            data2.setTitle(e2.get(0).attr("title").substring(0, 20) + "...");
//                            data2.setContent(e2.get(0).attr("href"));
//                            mDataset.add(data2);
//                        }
                    }
//                    if (e != null && e.size()>0)
//                        data.setContent(e.get(0).attr("href"));
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Data> data){
            super.onPostExecute(data);

            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Complete", Toast.LENGTH_LONG).show();

            mAdapter = new RecyclerAdapter(mDataset);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setAdapter(mAdapter);
        }

        public Document getDocument(String url) throws IOException {
            Document doc = null;
            doc = Jsoup.connect(url).followRedirects(true).get();
//            doc = Jsoup.connect(url).timeout(3000).get();
            return doc;
        }

    }


}