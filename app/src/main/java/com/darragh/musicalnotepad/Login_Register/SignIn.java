package com.darragh.musicalnotepad.Login_Register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.darragh.musicalnotepad.Pagers.MainActivity;
import com.darragh.musicalnotepad.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity implements View.OnClickListener{
    private Button signIn, googleSignIn;
    private EditText emailText;
    private EditText passwordText;
    private TextView signUp;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener stateListener;
    private static GoogleApiClient mGoogleApiClient;
    private static GoogleSignInOptions gso;
    private static final int RC_SIGN_IN = 9001;
    private static DatabaseReference databaseReference;
    private static String googleUID, googleUsername, googleEmail;
    private Uri profilePhoto;


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void writeNewUser(String uid, String username, String email){
        User user = new User(username,email);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        for(UserInfo profile: firebaseUser.getProviderData()){
            profilePhoto = profile.getPhotoUrl();
        }
        user.profilePhoto=profilePhoto.toString();
        Map<String, Object> map = new HashMap<>();
        map.put(getResources().getString(R.string.users)+uid,user);
        databaseReference.updateChildren(map);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(SignIn.this, "Error login in",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            googleUID = task.getResult().getUser().getUid();
                            Toast.makeText(SignIn.this, "Successful login",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                googleUsername = account.getDisplayName();
                googleEmail = account.getEmail();
                firebaseAuthWithGoogle(account);
            }
        }
    }

    private void setGoogleComponents(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),"Please enter password",Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void setStateListener(){
        stateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(googleUID!=null){
                                if(!dataSnapshot.child("users").child(googleUID).exists()){
                                    writeNewUser(googleUID,googleUsername,googleEmail);
                                }
                            }
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        };
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = firebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            startActivity(new Intent(this, MainActivity.class));
        }
        instantiateView();
        setGoogleComponents();
        setStateListener();
    }

    private void instantiateView(){
        emailText = (EditText) findViewById(R.id.signInEmail);
        passwordText = (EditText) findViewById(R.id.signInPassword);
        findViewById(R.id.signInButton).setOnClickListener(this);
        signIn = (Button) findViewById(R.id.buttonSignIn);
        signUp = (TextView) findViewById(R.id.signUp);
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }
    private void userLogin(String email, String password){
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            finish();
                            Toast.makeText(SignIn.this, "Successful login.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            Toast.makeText(SignIn.this, "Error login in",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(stateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (stateListener != null) {
            firebaseAuth.removeAuthStateListener(stateListener);
        }
    }

    @Override
    public void onClick(View view){
        if(view == signIn){
            userLogin(emailText.getText().toString().trim(), passwordText.getText().toString().trim());
        } else if(view == signUp){
            finish();
            startActivity(new Intent(this, Register.class));
        } else {
            signIn();
        }
    }
}
