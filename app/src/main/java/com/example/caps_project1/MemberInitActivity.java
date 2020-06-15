package com.example.caps_project1;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.caps_project1.database.UserInfomation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
public class MemberInitActivity extends AppCompatActivity {
    private static final String TAG = "MemberInitActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_init);
        findViewById(R.id.checkButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.checkButton) {
                    profileUpdate();
                }
            }
        });
    }
    // 회원 정보 수정 페이지에서 뒤로 가기 막기
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private void profileUpdate() {
        String pet = ((EditText) findViewById(R.id.petNameEditText)).getText().toString();
        String gender = ((EditText) findViewById(R.id.genderEditText)).getText().toString();
        String birth = ((EditText) findViewById(R.id.birthEditText)).getText().toString();
        String address = ((EditText) findViewById(R.id.addressEditText)).getText().toString();


            if (gender.length() > 0 && birth.length() > 5 && address.length() > 0
                    && pet.length() > 0) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                UserInfomation userInfo = new UserInfomation(pet, gender, birth, address);

                if (user != null) {
                    db.collection("users").document(user.getUid()).set(userInfo)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(MemberInitActivity.this, "회원정보 등록을 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MemberInitActivity.this, "회원정보 등록을 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                    Log.w(TAG, "Error", e);
                                }
                            });
                }
            } else {
                Toast.makeText(MemberInitActivity.this, "회원정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }