package com.example.caps_project1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caps_project1.database.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private EditText et_name, et_email, et_password, et_password_check, et_phone;
    private FirebaseAuth mAuth; // FirebaseAuth 인스턴스 선언
    private FirebaseDatabase database; // FirebaseDatabase 인스턴스 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance(); // FirebaseAuth 인스턴스 초기화
        database = FirebaseDatabase.getInstance(); // FirebaseDatabase 인스턴스 초기화
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp_Email();
            }
        });

        et_name = findViewById(R.id.nameEditText);
        et_email = findViewById(R.id.emailEditText);
        et_password = findViewById(R.id.passwordEditText);
        et_password_check = findViewById(R.id.passwordCheckEditText);
        et_phone = findViewById(R.id.phoneEditText);
    }

    // 이메일 로그인
    private void signUp_Email() {
        String name = et_name.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String passwordCheck = et_password_check.getText().toString();
        String phone = et_phone.getText().toString();

        // 이름, 이메일, 비밀번호, 비밀번호 확인, 휴대폰 번호 모두 입력했는지 확인
        if(name.length() > 0 && email.length() > 0 && password.length() > 5 && passwordCheck.length() > 5 && phone.length() > 9) {
            if (password.equals(passwordCheck)) { // 비밀번호 2중 확인
                // createUserWithEmailAndPassword : 비밀번호 기반의 계정을 생성
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "회원가입에 성공하였습니다.");
                                    Toast.makeText(SignUpActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    startLoginActivity();
                                    updateUI(user);

                                    // Firebase Realtime Database : 사용자 정보 (이메일, 페스워드) 저장
                                    UserData userdata = new UserData();
                                    userdata.userName = et_name.getText().toString();
                                    userdata.userEmail = et_email.getText().toString();
                                    userdata.userPassword = et_password.getText().toString();
                                    userdata.userPhoneNumber = et_phone.getText().toString();
                                    String uid = Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).getUid();
                                    database.getReference().child("users").child(uid).setValue(userdata);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    if (task.getException() != null) {
                                        Log.w(TAG, "비밀번호가 일치하지 않습니다.", task.getException());
                                        Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        updateUI(null);
                                    }
                                }
                            }
                        });
            } else {
                Toast.makeText(SignUpActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (email.length() == 0 && password.length() == 0 && passwordCheck.length() == 0) {
                Toast.makeText(SignUpActivity.this, "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (password.length() <= 5 && passwordCheck.length() > 5) {
                Toast.makeText(SignUpActivity.this, "비밀번호를 6자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
            } else if (phone.length() <= 9) {
                Toast.makeText(SignUpActivity.this, "휴대폰 번호가 정확하지 않습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // 활동을 초기화할 때 사용자가 현재 로그인되어 있는지 확인
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}
