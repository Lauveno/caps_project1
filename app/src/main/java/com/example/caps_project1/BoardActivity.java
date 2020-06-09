package com.example.caps_project1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoardActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardActivity extends Fragment {

    // db 에 앱이 직접 저장하는게 아니고 DatabaseReference 를 매개체 삼아 저장하고 읽어오는 방식
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    private ArrayList<Board> mDataset = new ArrayList<Board>(); //이용할 ArrayList?!
    private RecyclerView bdRecyclerView;
    private BDAdapter mAdapter;
    private Context mContext;
    private Board mBoard; //data class

    public BoardActivity() {
        // Required empty public constructor
    }

    public static BoardActivity newInstance(String param1, String param2) {
        BoardActivity fragment = new BoardActivity();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mDataset.add(new Board("제목1","사람1", "내용블라블라"));
        mDataset.add(new Board("제목2","사람2", "내용블라블라"));
        BoardActivity.boardData task = new BoardActivity.boardData();
        task.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDataset.clear();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_board,container,false);
        bdRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_board);
        return view;
    }

    private class boardData extends AsyncTask<Void, Void, ArrayList<Board>> {

        private ProgressDialog progressDialog;
        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("게시글을 로딩중입니다");
            progressDialog.show();
        }
        @Override
        protected ArrayList<Board> doInBackground(Void... voids) {
            try{

                mDataset.add(new Board("제목1","사람1", "내용블라블라"));

                mDataset.add(new Board("제목1","사람1", "내용블라블라"));

                mDataset.add(new Board("제목1","사람1", "내용블라블라"));

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Board> board){
            super.onPostExecute(board);

            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Complete", Toast.LENGTH_LONG).show();

            mAdapter = new BDAdapter(mDataset);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            bdRecyclerView.setLayoutManager(mLayoutManager);
            bdRecyclerView.setItemAnimator(new DefaultItemAnimator());
            bdRecyclerView.setHasFixedSize(true);
            bdRecyclerView.setAdapter(mAdapter);
        }


    }

}
