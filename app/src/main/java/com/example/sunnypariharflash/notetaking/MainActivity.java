package com.example.sunnypariharflash.notetaking;

import android.*;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
GoogleSignInClient clients;
SignInButton btns;
FirebaseAuth auth;
DatabaseReference ref;
private static int RC_SIGN_IN = 1889;
ProgressDialog dialog;
    GoogleSignInClient mGoogleSignInClient;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    SharedPreferences sp = getSharedPreferences("account",MODE_PRIVATE);
String shareds = sp.getString("uid",null);
if(!shareds.equals("")){
    startActivity(new Intent(MainActivity.this,Main2Activity.class));
}
    setContentView(R.layout.activity_main);
    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(getString(R.string.default_web_client_id))
            .build();
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
btns = findViewById(R.id.btnsignin);
btns.setSize(SignInButton.SIZE_WIDE);
btns.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
signIn();
    }
});
}
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
}

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            auth = FirebaseAuth.getInstance();
            BackgroundTask task2 = new BackgroundTask(MainActivity.this,account);
            task2.execute();
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Tag", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
    if(account!=null){

        Toast.makeText(this, account.getDisplayName(), Toast.LENGTH_SHORT).show();
    }
    else{
        Toast.makeText(this, "Need to sign In first.", Toast.LENGTH_SHORT).show();
    }
    }
    public void firebaseauthwithgoogle(GoogleSignInAccount account){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Tag", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //  updateUI(user);
                            registerUsertoFirebase(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Tag", "signInWithCredential:failure", task.getException());
                         //   Snackbar.make(findViewById(R.id.btnsignin), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }


                    }
                });
    }
    public static  List<UserProfile> UserInfo(String name,String email,String photourl){
    List<UserProfile> pro = new ArrayList<>();
    UserProfile users = new UserProfile(name,email,photourl);
    pro.add(users);
    return pro;
    }
    public void registerUsertoFirebase(final FirebaseUser firebaseUser){
    ref = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(firebaseUser.getUid());
    List<UserProfile> profile = UserInfo(firebaseUser.getDisplayName(),firebaseUser.getEmail(),firebaseUser.getPhotoUrl().toString());
    for(UserProfile usersd : profile){
        ref.setValue(usersd).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Unable to register.", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "User added successfully.", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,Main2Activity.class);
                i.putExtra("User",firebaseUser.getUid());
                SharedPreferences sp = getSharedPreferences("account",MODE_PRIVATE);
                SharedPreferences.Editor et = sp.edit();
                et.putString("uid",firebaseUser.getUid());
                et.apply();
                et.commit();
if(dialog.isShowing()){
    dialog.dismiss();
}
                Toast.makeText(MainActivity.this, firebaseUser.getDisplayName().toString()+" "+firebaseUser.getEmail().toString(), Toast.LENGTH_SHORT).show();
                startActivity(i);
            }
        });
    }
    }
    private class BackgroundTask extends AsyncTask<Void, Void, Void> {
        GoogleSignInAccount user;
        public BackgroundTask(MainActivity activity, GoogleSignInAccount user) {
            this.user = user;
            dialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Please wait...");
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                firebaseauthwithgoogle(user);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

    }
}

