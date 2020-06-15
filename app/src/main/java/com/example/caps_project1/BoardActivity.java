package com.example.caps_project1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.caps_project1.database.WriteInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
    private ArrayList<String> docIdList = new ArrayList<>();;
    private FirebaseUser user;
    private FirebaseFirestore db;
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
        boardData task = new boardData();
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
        final View view =  inflater.inflate(R.layout.activity_board,container,false);
        bdRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view_board);


        Button button = (Button) view.findViewById(R.id.board_btn);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View view){
                writeUpdate();

            }

            private void writeUpdate() {
                String title = ((EditText) view.findViewById(R.id.board_title)).getText().toString();
                String Contents = ((EditText) view.findViewById(R.id.board_content)).getText().toString();

                if (title.length() > 1 && Contents.length() > 1) {
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    WriteInfo writeInfo = new WriteInfo(title, Contents, user.getUid());
                    uploader(writeInfo);
                    Toast.makeText(getActivity(), "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                clearEdit();
            }

            private void uploader(WriteInfo writeInfo) {
                db = FirebaseFirestore.getInstance();
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
                mDataset.add(new Board(writeInfo.getTitle(),writeInfo.getContents(), writeInfo.getPublisher()));
            }
            public void clearEdit(){
                EditText editText = (EditText)view.findViewById(R.id.board_title);
                EditText editText2 = (EditText)view.findViewById(R.id.board_content);

                String emptytext="";

                editText.setText(emptytext);
                editText2.setText(emptytext);

            }
        });

        //방금 입력한 내용 db에 들어간거 버튼 누르면 그것도 목록에 나와야 함. 목록 업데이트
//        public void ReadUpdate(){
//
//        }

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
                mDataset.add(new Board("제목3","내용블라블라", "익명"));
                //database 게시글 가져와야 함
                DocumentReference contact = db.collection("posts").document();
                contact.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            //DocumentSnapshot doc = task.getResult();
                            //Log.d("doc result : ",doc.toString());
                            //doc.
                        }
                    }
                });

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