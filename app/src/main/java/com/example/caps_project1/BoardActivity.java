package com.example.caps_project1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BoardActivity#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BoardActivity extends Fragment {

    // db 에 앱이 직접 저장하는게 아니고 DatabaseReference 를 매개체 삼아 저장하고 읽어오는 방식
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private static final String TAG = "BoardActivity";

    private ArrayList<Board> mDataset = new ArrayList<Board>(); //이용할 ArrayList?!
    private RecyclerView bdRecyclerView;
    private BDAdapter mAdapter;
    private Context mContext;
    private Board mBoard; //data class
    // private FirebaseUser user;

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
        // findViewById(R.id.board_btn).setOnClickListener(OnClickListener);
    }
    /* 게시물 작성 시 Firebase의 Cloud Firestore에 작성된 게시글의 정보를 저장한다.
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.board_btn) {
                    writeUpdate();
                }
            }
        };

        private void writeUpdate() {
            final String title = ((EditText) findViewById(R.id.board_title)).getText().toString();
            final String Contents = ((EditText) findViewById(R.id.board_content)).getText().toString();


            if (title.length() > 1 && Contents.length() > 1) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                WriteInfo writeInfo = new WriteInfo(title, Contents, user.getUid());
                uploader(writeInfo);

            } else {
                Toast.makeText(BoardActivity.this, "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
            }
        }

        private void uploader(WriteInfo writeInfo){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("posts").add(writeInfo)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.w(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    */
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
