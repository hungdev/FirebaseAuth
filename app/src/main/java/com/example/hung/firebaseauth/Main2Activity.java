package com.example.hung.firebaseauth;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Main2Activity extends AppCompatActivity {

    @Bind(R.id.tv_show)
    TextView tv_show;
    @Bind(R.id.btn_logout)
    TextView btn_logout;
    @Bind(R.id.edt_name)
    EditText edt_name;
    @Bind(R.id.btn_update)
    Button btn_update;
    @Bind(R.id.imv_photo)
    ImageView imv_photo;
    @Bind(R.id.btn_photo)
    Button btn_photo;
    String id;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseUser user;
    Uri photoUrl;
    private  static int PICK_IMAGE=0;
    Uri selectedImage;
    String picturePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    //Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    //Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        Intent intent = getIntent();
        //id  = getIntent().getExtras().get("id").toString();
        id = intent.getStringExtra("id");
        //TextView tv_show =(TextView)findViewById(R.id.tv_show);
        tv_show.setText(id);
    }

    @OnClick(R.id.btn_update)
    public void setUpdate() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Jane Q. User")
                //.setPhotoUri(Uri.parse("https://www.cloudflare.com/media/cloudflare-logo.png"))
               // .setPhotoUri(Uri.parse(picturePath))
                .setPhotoUri(selectedImage)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Log.d(TAG, "User profile updated.");
                            Toast.makeText(getBaseContext(), "User profile updated.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    @OnClick(R.id.btn_logout)
    public void setBtn_logout() {
        //FirebaseAuth.getInstance().signOut();
//        Intent intent = new Intent(this,MainActivity.class);
//        //intent.putExtra("id",mAuth.getCurrentUser().getUid().toString()) ;
//        startActivity(intent);
        tv_show.setText(user.getDisplayName().toString());
        photoUrl = user.getPhotoUrl();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUrl);
            imv_photo.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @OnClick(R.id.btn_photo)
    public void selectPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
             selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//             picturePath = cursor.getString(columnIndex);
//            cursor.close();

            //ImageView imageView = (ImageView) findViewById(R.id.imgView);
           // imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }
}
