package com.adminsurfacetech.my_learn_hub.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.view.Gravity;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.adminsurfacetech.my_learn_hub.Fragments.AboutFragment;
import com.adminsurfacetech.my_learn_hub.Fragments.CategoryFragment;
import com.adminsurfacetech.my_learn_hub.Fragments.FeedFragment;
import com.adminsurfacetech.my_learn_hub.Fragments.PostFragment;
import com.adminsurfacetech.my_learn_hub.Fragments.ProfileFragment;
import com.adminsurfacetech.my_learn_hub.Fragments.RankingFragment;
import com.adminsurfacetech.my_learn_hub.Fragments.SettingsFragment;
import com.adminsurfacetech.my_learn_hub.Models.Post;
import com.adminsurfacetech.my_learn_hub.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PReqCode = 2 ;
    private static final int REQUESCODE = 2 ;
    ImageView popupUserImage,popupPostImage,popupAddBtn;
    TextView popupTitle,popupDescription;
    ProgressBar popupClickProgress;
    private Uri pickedImgUri = null;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    Dialog popAddpost;
    BottomNavigationView bottomNavigationView;
    MaterialSearchView searchView;
    BroadcastReceiver mRegistrationBroadcastReceiver;
    TextView usergangan;
    private Toolbar toolbar;
    private SaveTheamSatate theamSatate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theamSatate = new SaveTheamSatate(this);
        if(theamSatate.looadNightModeState()){
            setTheme(R.style.NightMode);
        }else {
            setTheme(R.style.DayMode);
        }
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        Drawable drawable = ContextCompat.getDrawable(this,R.drawable.ic_search_black_24dp);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        // init views for the header
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        usergangan = (TextView)findViewById(R.id.intro_name);
        usergangan.setText(currentUser.getDisplayName());

        // pop up post for user to post questions
        iniPopup();
        setupPopupImageClick();

        bottomNavigationView  = (BottomNavigationView)findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                switch (item.getItemId())
                {
                    case R.id.ic_home:
                        getSupportActionBar().setTitle("Home");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CategoryFragment()).commit();
                        break;
                    case R.id.ic_ranking:
                        getSupportActionBar().setTitle("Questions posted");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,  new RankingFragment()).commit();
                        break;
                    case R.id.ic_post:
                        getSupportActionBar().setTitle("Ranking");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new PostFragment()).commit();
                        break;
                    case R.id.ic_profile:
                        getSupportActionBar().setTitle("Profile");
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();
                        break;
                }
                return true;
            }
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               popAddpost.show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new CategoryFragment()).commit();
    }

    private void setupPopupImageClick() {
        popupPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // once the post image is clicked it will open inn the cameral for pick up
                checkAndRequestForPermission();

            }
        });
    }
    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(Home.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Home.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(Home.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(Home.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            //once the users clicks the permission to access the gallery it will open here
            openGallery();

    }
    private void openGallery(){
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {

            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            popupPostImage.setImageURI(pickedImgUri);


        }
    }
    private void iniPopup() {
        popAddpost = new Dialog(this);
        popAddpost.setContentView(R.layout.popup_add_post);
        popAddpost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddpost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT,Toolbar.LayoutParams.WRAP_CONTENT);
        popAddpost.getWindow().getAttributes().gravity = Gravity.TOP;

        // ini popup widgets
        popupUserImage = popAddpost.findViewById(R.id.popup_user_image);
        popupPostImage = popAddpost.findViewById(R.id.popup_img);
        popupTitle = popAddpost.findViewById(R.id.popup_title);
        popupDescription = popAddpost.findViewById(R.id.popup_description);
        popupAddBtn = popAddpost.findViewById(R.id.popup_add);
        popupClickProgress = popAddpost.findViewById(R.id.popup_progressBar);

        // load current user image using slide package
        Glide.with(Home.this).load(currentUser.getPhotoUrl()).into(popupUserImage);

        // add question post listerner
        popupAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                popupAddBtn.setVisibility(View.INVISIBLE);
                popupClickProgress.setVisibility(View.VISIBLE);

                // we need to test if all input and field are

                if (!popupTitle.getText().toString().isEmpty()
                        && !popupDescription.getText().toString().isEmpty()
                        && pickedImgUri != null ) {
                    // if everything is okay...
                    //TODO create post object and add it to firebase database
                    // get the question image first
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("question_image");
                    final StorageReference imagefilepath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imagefilepath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagefilepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageDownlaodLink = uri.toString();
                                    // now create post object here
                                    Post post = new Post(popupTitle.getText().toString(),
                                            popupDescription.getText().toString(),
                                            imageDownlaodLink,
                                            currentUser.getUid(),
                                            currentUser.getPhotoUrl().toString());
                                    // Add post to firebase
                                    addPost(post);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // something goes wrong and the picture didnt upload
                                    showMessage(e.getMessage());
                                    popupClickProgress.setVisibility(View.INVISIBLE);
                                    popupAddBtn.setVisibility(View.VISIBLE);
                                }
                            });
                        }
                    });


                }else {
                    showMessage("please verify all fields and choose an image");
                    popupAddBtn.setVisibility(View.VISIBLE);
                    popupClickProgress.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

    private void addPost(Post post) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("post").push();
        //get post unique id and post on firebase
        String key = myRef.getKey();
        post.setPostKey(key);

        // add post to firebase
        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Question successfully posted");
                popupClickProgress.setVisibility(View.INVISIBLE);
                popupAddBtn.setVisibility(View.VISIBLE);
                popAddpost.dismiss();
            }
        });

    }

    private void showMessage(String message) {
        Toast.makeText(Home.this,message,Toast.LENGTH_LONG).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        final MenuItem profileItem = menu.findItem(R.id.profileMain);
        View profileView = MenuItemCompat.getActionView(profileItem);
        CircleImageView profileImage = profileView.findViewById(R.id.userProfileImage);
        Glide.with(this).load(currentUser.getPhotoUrl()).into(profileImage);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(Home.this,Settings.class));
                break;
        }

        switch (item.getItemId()){
            case R.id.nav_Logout:
                FirebaseAuth.getInstance().signOut();
                Intent login = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(login);
                finish();
                break;
        }
      //  switch (item.getItemId()){
         //   case R.id.toolbar_per:
             //   Glide.with(this).load(currentUser.getPhotoUrl()).into(toolbar_per);
       // }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_about) {
            getSupportActionBar().setTitle("About");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AboutFragment()).commit();//
        } else if (id == R.id.nav_feedback) {
            getSupportActionBar().setTitle("Feedback");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new FeedFragment()).commit();//

        } else if (id == R.id.action_contact) {
            getSupportActionBar().setTitle("Contact");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new AboutFragment()).commit();//

        } else if (id == R.id.nav_setting) {
            getSupportActionBar().setTitle("Settings");
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment()).commit();
           // Intent intent = new Intent(Home.this , Settinglayout.class);
           // startActivity(intent);

        } else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText = "Check it out have you tried downloading LearnHub";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"just try and check");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Share with"));
            return true;
        }
        else if (id == R.id.nav_Logout) {
            FirebaseAuth.getInstance().signOut();
            Intent login = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(login);
            finish();

        } else if (id == R.id.nav_exit) {
                // TODO Auto-generated method stub
                finish();
                System.exit(0);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void updateNavHeader(){

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerview = navigationView.getHeaderView(0);
        TextView navUsername = headerview.findViewById(R.id.nav_username);
        TextView navUsermail = headerview.findViewById(R.id.nav_usermail);
        CircleImageView navuserimage = headerview.findViewById(R.id.nav_user_photo);

        navUsername.setText(currentUser.getDisplayName());
        navUsermail.setText(currentUser.getEmail());
        // get the user image through glide
        Glide.with(this).load(currentUser.getPhotoUrl()).into(navuserimage);

    }
}
