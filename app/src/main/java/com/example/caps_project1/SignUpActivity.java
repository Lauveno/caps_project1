package com.example.caps_project1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;

    private EditText et_email, et_password, et_password_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplication(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        findViewById(R.id.signUpButton).setOnClickListener(onClickListener);


        et_email = findViewById(R.id.emailEditText);
        et_password = findViewById(R.id.passwordEditText);
        et_password_check = findViewById(R.id.passwordCheckEditText);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.signUpButton) {
                signUp_Email();
            }
        }
    };

    private void signUp_Email() {
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String passwordCheck = et_password_check.getText().toString();

        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            if (password.equals(passwordCheck)) {

                // createUserWithEmailAndPassword : 비밀번호 기반의 계정을 생성
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "회원가입에 성공하셨습니다.");
                                    Toast.makeText(SignUpActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    if (task.getException() != null) {
                                        //Log.w(TAG, "비밀번호가 일치하지 않습니다.", task.getException());
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
            Toast.makeText(SignUpActivity.this, "이메일 또는 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            //finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 활동을 초기화할 때 사용자가 현재 로그인되어 있는지 확인
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}
