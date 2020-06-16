package com.example.caps_project1;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

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
    private static final String TAG = "BoardActivity";

    private ArrayList<Board> mDataset = new ArrayList<Board>(); //이용할 ArrayList?!
    private RecyclerView bdRecyclerView;
    private BDAdapter mAdapter;
    private Context mContext;
    private Board mBoard; //data class
    private HashMap<String, String> UIdList = new HashMap<String, String>();;
    private FirebaseUser user;
    private FirebaseFirestore db;
    public BoardActivity() {
        // Required empty public constructor
        update();
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
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeUpdate();
                clearEdit();
            }

            private void writeUpdate() {
                final String title = ((EditText) view.findViewById(R.id.board_title)).getText().toString();
                final String Contents = ((EditText) view.findViewById(R.id.board_content)).getText().toString();
                user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;

                if (title.length() > 1 && Contents.length() > 1) {

                    WriteInfo writeInfo = new WriteInfo(title, Contents, UIdList.get(user.getUid()));
                    uploader(writeInfo);
                    Toast.makeText(getActivity(), "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                }
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
                mDataset.add(new Board(writeInfo.getTitle(), writeInfo.getContents(), writeInfo.getPublisher()));
            }

            public void clearEdit() {
                EditText editText = (EditText) view.findViewById(R.id.board_title);
                EditText editText2 = (EditText) view.findViewById(R.id.board_content);

                String empty_text = "";

                editText.setText(empty_text);
                editText2.setText(empty_text);

            }
        });
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
            try {

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Board> board){
            super.onPostExecute(board);

            progressDialog.dismiss();
            mAdapter = new BDAdapter(mDataset);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            bdRecyclerView.setLayoutManager(mLayoutManager);
            bdRecyclerView.setItemAnimator(new DefaultItemAnimator());
            bdRecyclerView.setHasFixedSize(true);
            bdRecyclerView.setAdapter(mAdapter);

            Toast.makeText(getActivity(), "Complete", Toast.LENGTH_LONG).show();
        }
    }
    public void update(){
        mDataset.clear();
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        user.getUid();
        Log.d("result : ",user.getUid());
        db = FirebaseFirestore.getInstance();
        db.collection("posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int index = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getData().get("publisher") != null){
                                    String title = document.getData().get("title").toString();
                                    String content = document.getData().get("contents").toString();
                                    String publisher = document.getData().get("publisher").toString();
                                    mDataset.add(new Board(title,content, publisher));
                                    Log.d("result : ",String.valueOf(mDataset.get(index).getTitle()));
                                    index++;
                                }
                            }
                        } else {
                            Log.w("result : ", "Error getting documents.", task.getException());
                        }
                    }
                });
        db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String user_name_doc="";
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(user.getUid().equals(document.getId())){

                                    user_name_doc = document.getData().get("name").toString();
                                    Log.d("result : ",user_name_doc);
                                    UIdList.put(user.getUid(),user_name_doc);

                                }
                            }
                        } else {
                            Log.w("result : ", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

}