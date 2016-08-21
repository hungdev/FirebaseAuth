package com.example.hung.firebaseauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;

import butterknife.ButterKnife;
import butterknife.OnClick;

//dang lam
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.btn_login)
    Button btn_login;
    @Bind(R.id.btn_register)
    Button btn_register;
    @Bind(R.id.edt_email)
    EditText edt_email;
    @Bind(R.id.edt_password)
    EditText edt_password;
    @Bind(R.id.tv_getinfo)
    TextView tv_getinfo;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static String TAG= "Tag";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //In your sign-up activity's onCreate method, get the shared instance of the FirebaseAuth object:
        mAuth = FirebaseAuth.getInstance();
        //Set up an AuthStateListener that responds to changes in the user's sign-in state:
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                   // Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                   // Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        // ...
    }
    //Sign up
    @OnClick(R.id.btn_register)
    public void setRegister(){

        mAuth.createUserWithEmailAndPassword(edt_email.getText().toString(), edt_password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        Toast.makeText(MainActivity.this, "Register success" + mAuth.getCurrentUser().getUid().toString(),
                                Toast.LENGTH_SHORT).show();
                        if (!task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Register false",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }


    //Sign in
    @OnClick(R.id.btn_login)
    public void setLogin(){
        mAuth.signInWithEmailAndPassword(edt_email.getText().toString(), edt_password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
//                        Toast.makeText(MainActivity.this, "Login success with id: ",
//                                Toast.LENGTH_SHORT).show();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
//                            Toast.makeText(MainActivity.this, "Login false!",
//                                    Toast.LENGTH_SHORT).show();
                        }
//                        Toast.makeText(MainActivity.this, mAuth.getCurrentUser().getUid().toString(),
//                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                        intent.putExtra("id",mAuth.getCurrentUser().getUid().toString()) ;
                        startActivity(intent);
                        tv_getinfo.setText(mAuth.getCurrentUser().getUid().toString());


                    }
                });
    }
    @OnClick(R.id.btn_logout)
    public void setLogut(){
        FirebaseAuth.getInstance().signOut();
        if(mAuth.getCurrentUser()==null){
            tv_getinfo.setText("Null");
        }
        else {
            tv_getinfo.setText(mAuth.getCurrentUser().getUid().toString());
        }
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
    }

