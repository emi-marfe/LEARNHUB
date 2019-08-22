package com.adminsurfacetech.my_learn_hub.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adminsurfacetech.my_learn_hub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Sign_upActivity extends AppCompatActivity {

    CircleImageView ImgUserphoto;
    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri ;

    private EditText UserName,UserMail;
    private TextInputEditText UserPassword;
    private ProgressBar loadingProgress;
    private Button regBtn;
    private TextView login, toast;

    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // set up the views
        UserName = findViewById(R.id.regName);
        UserMail = findViewById(R.id.regMail);
        UserPassword = findViewById(R.id.regPassword);
        ImgUserphoto = findViewById(R.id.regUserPhoto);
        login = findViewById(R.id.gotologin);
        toast = findViewById(R.id.toasthelp);
        regBtn = findViewById(R.id.regBtn);
        loadingProgress = findViewById(R.id.regProgressBar);

        mAuth = FirebaseAuth.getInstance();
          reference = FirebaseDatabase.getInstance().getReference("Users");

        toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast_help = new Toast(getApplicationContext());
                toast_help.setGravity(Gravity.CENTER, 0, 0);
                toast_help.setDuration(Toast.LENGTH_LONG);
                LayoutInflater inflater = getLayoutInflater();
                View appear = inflater.inflate(R.layout.toast_help, (ViewGroup) findViewById(R.id.linear));
                toast_help.setView(appear);
                toast_help.show();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainActivity);
               // Window w = getWindow();
                //                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                //                getSupportActionBar();
               // ActionBar actionBar = getSupportActionBar();
                //actionBar.hide();
                finish();
            }
        });
        //  Glide.with(this).load(pickedImgUri).into()

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);
                final String email = UserMail.getText().toString();
                final String password = UserPassword.getText().toString();
                final String name = UserName.getText().toString();

                if( email.isEmpty() || name.isEmpty() || password.isEmpty()){
                    // display an error messsage when anything goes wrong
                    showMessage("please Verify all fields");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);


                }else {
                    // when all fields are all filed and everything is okay

                    createUserAccount(name,email,password);
                }

            }

        });

        loadingProgress.setVisibility(View.INVISIBLE);

        ImgUserphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 22) {

                    checkAndRequestForPermission();


                }
                else
                {
                    openGallery();
                }

            }
        });
    }

    private void createUserAccount(final String name, final String email, final String password) {
        //create a users account when all details has been taken from the users..

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            // user account will be created
                            //          FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            //          assert firebaseUser != null;
                            //         String userid = firebaseUser.getUid();


                            //           reference = FirebaseDatabase.getInstance().getReference("Users").child(name);
                            //          HashMap<String, String>hashMap = new HashMap<>();
                            //      hashMap.put("id",userid);
                            //     hashMap.put("username",name);
                            //     hashMap.put("password", password);
                            //    hashMap.put("email",email);
                            // hashMap.put("picture",imagedownload);

                            //   reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            //   @Override
                            // public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                showMessage("Account successfully created");
                                // update the users info for futher information
                                updateUserInfo(name , password, pickedImgUri,  mAuth.getCurrentUser());

                            }

                            //   }
                            // });
                        }

                        else {
                            // account creatin failed
                            showMessage("Unable to create account" + task.getException().getMessage());
                            regBtn.setVisibility(View.VISIBLE);
                            loadingProgress.setVisibility(View.INVISIBLE);
                        }
                    }

                });

    }

    //update username and image
    private void updateUserInfo(final String name, final String password, final Uri pickedImgUri, final FirebaseUser currentUser) {

        // first the picture needs to be uploaded to firebase storage
        StorageReference mstorage = FirebaseStorage.getInstance().getReference().child("users_photo");
        final StorageReference imageFilePath = mstorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                // image uploaded successfully
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String imagedownload = uri.toString();
                        String userid = firebaseUser.getUid();
                        String email = firebaseUser.getEmail();

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(name);
                        HashMap<String, String>hashMap = new HashMap<>();
                        hashMap.put("id",userid);
                        hashMap.put("username",name);
                        hashMap.put("image", imagedownload);
                        hashMap.put("email",email);
                        hashMap.put("password",password);
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                // lets check it out
                            }
                        });
                        // this uri contains the users image
                        UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();

                        currentUser.updateProfile(profleUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){

                                            // users information update successful
                                            showMessage("Registration Complete");
                                            UpdateUI();

                                        }

                                    }
                                });
                    }
                });
            }
        });

    }

    private void UpdateUI() {
        Intent homeActivity = new Intent(getApplicationContext(), Home.class);
        startActivity(homeActivity);
        finish();
    }

    // another method of showing a Toast message...

    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(), message,Toast.LENGTH_LONG).show();

    }

    private void openGallery(){
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }


    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(Sign_upActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Sign_upActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(Sign_upActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(Sign_upActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            openGallery();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            ImgUserphoto.setImageURI(pickedImgUri);


        }
    }
}
