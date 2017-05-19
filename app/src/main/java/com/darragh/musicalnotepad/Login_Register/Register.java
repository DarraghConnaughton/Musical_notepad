package com.darragh.musicalnotepad.Login_Register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.darragh.musicalnotepad.Pitch_Detector.Tuner;
import com.darragh.musicalnotepad.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private static String _email,_password,_username;
    private EditText editTextEmail;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private TextView textViewSignIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener stateListener;


    public void writeNewUser(String uid, String username, String email){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").child(uid).setValue(new User(username,email));
    }

    private boolean notValid(String email, String password, String username){
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return true;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return true;
        }
        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Please enter a username",Toast.LENGTH_SHORT).show();
            return true;
        }
        if(password.length()<6){
            Toast.makeText(this,"Password must be at least 6 characters long.",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    public void registerUser(final String username, final String email, final String password){
        if(notValid(email,password,username)){
            return;
        }
        progressDialog.setMessage("Registering User...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "Registered Successfully.",
                                    Toast.LENGTH_SHORT).show();
                            writeNewUser(task.getResult().getUser().getUid(),username,email);
                            finish();
                            startActivity(new Intent(getApplicationContext(), Tuner.class));

                        } else {
                            Toast.makeText(Register.this, "Could not register.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        setFirebase();
        instantiateView();
    }

    public void setFirebase(){
        firebaseAuth = firebaseAuth.getInstance();
        stateListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    startActivity(new Intent(getApplicationContext(), Tuner.class));
                }
            }
        };
    }

    public void instantiateView(){
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editEmail);
        editTextUsername = (EditText) findViewById(R.id.userName);
        editTextPassword = (EditText) findViewById(R.id.password);
        textViewSignIn = (TextView) findViewById(R.id.signIn);
        progressDialog = new ProgressDialog(this);
        buttonRegister.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);
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

    private void gatherUserInformation(){
        _email = editTextEmail.getText().toString().trim();
        _password = editTextPassword.getText().toString().trim();
        _username = editTextUsername.getText().toString().trim();
    }

    @Override
    public void onClick(View view){
        if(view == buttonRegister){
            gatherUserInformation();
            registerUser(_username,_email,_password);
        }
        if(view == textViewSignIn){
            finish();
            startActivity(new Intent(this, SignIn.class));
        }

    }
}

