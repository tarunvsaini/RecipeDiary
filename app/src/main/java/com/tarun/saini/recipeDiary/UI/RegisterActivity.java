package com.tarun.saini.recipeDiary.UI;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tarun.saini.recipeDiary.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText emailEditText, passwordEditText;
    private CircularProgressButton registerButton;
    private TextView loginTv;

    private static final String TAG =RegisterActivity.class.getSimpleName() ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        emailEditText = (EditText) findViewById(R.id.emailRegister);
        passwordEditText = (EditText) findViewById(R.id.passwordRegister);
        registerButton = (CircularProgressButton) findViewById(R.id.email_register_button);
        loginTv= (TextView) findViewById(R.id.textViewRegister);
        registerButton.setIndeterminateProgressMode(true);


       /* if (mAuth.getCurrentUser() !=null)
        {

        }
*/
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    finish();
                    Intent intent=new Intent(RegisterActivity.this,RecipeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    //Toast.makeText(RegisterActivity.this, "SignedIn with:"+user.getEmail(), Toast.LENGTH_SHORT).show();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                   // Toast.makeText(RegisterActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        };

        registerButton.setOnClickListener(this);


        loginTv.setOnClickListener(this);

    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View v)
    {
        if (v==registerButton)
        {
            String email= emailEditText.getText().toString();
            String password= passwordEditText.getText().toString();

            if (TextUtils.isEmpty(email))
            {
                Toast.makeText(RegisterActivity.this, "Please Enter Your Email ", Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(password))
            {
                Toast.makeText(RegisterActivity.this, "Please Enter a password", Toast.LENGTH_SHORT).show();

            }
            else if(password.length()<6)
            {
                Toast.makeText(this, "Password too small minimum 6 or more characters", Toast.LENGTH_LONG).show();
            }

            else
            {
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            registerButton.setProgress(100);

                        }
                        else {
                            registerButton.setProgress(-1);

                        }

                    }



                });

                if(registerButton.getProgress()==0)
                {
                    registerButton.setProgress(25);
                }
                else if(registerButton.getProgress()==-1)
                {
                    registerButton.setProgress(0);
                }
                else  if(registerButton.getProgress()==100)
                {
                    finish();
                    Intent intent=new Intent(RegisterActivity.this,RecipeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(intent);

                }

            }

        }
        else if(v==loginTv)
        {
            finish();
            Intent intent=new Intent(this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity(intent);

        }

    }
}
