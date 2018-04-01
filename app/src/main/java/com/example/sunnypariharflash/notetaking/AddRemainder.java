package com.example.sunnypariharflash.notetaking;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AddRemainder extends AppCompatActivity {
EditText edt1,edt2,edt3;
DatabaseReference reference1,reference2;
FirebaseAuth auth;
FirebaseUser user;
ProgressDialog dialog;
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remainder);
    edt1 = findViewById(R.id.subjectatremainder);
    edt2 = findViewById(R.id.datesssss);
    edt3 = findViewById(R.id.timeatremainder);
    auth = FirebaseAuth.getInstance();
    user = auth.getCurrentUser();
    reference1 = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid()).child("Remainders");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }
private List<Remainders> getRemaindersInfo(String subject,String date,String time){
        List<Remainders> remainders = new ArrayList<>();
        Remainders re = new Remainders(subject,date,time);
        remainders.add(re);
return remainders;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
if(item.getItemId()==R.id.action_userd){
    Toast.makeText(this, "This will add your remainder.", Toast.LENGTH_SHORT).show();
BackgroundTask task = new BackgroundTask(this);
task.execute();
}
else if(item.getItemId()==R.id.action_cross){
    Toast.makeText(this, "This will cancel the updating remainder.", Toast.LENGTH_SHORT).show();
    onBackPressed();
}
        return super.onOptionsItemSelected(item);
    }
    public void InsertData(){
        if(!edt1.getText().toString().equals("")&&!edt2.getText().toString().equals("")&&!edt3.getText().toString().equals("")){
            String datatata = edt3.getText().toString();
            reference2  = reference1.child(datatata);
List<Remainders> reman = getRemaindersInfo(edt1.getText().toString(),edt2.getText().toString(),edt3.getText().toString());
for(Remainders res  : reman){
    reference2.setValue(res).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(AddRemainder.this, "Unable to add Remainder.", Toast.LENGTH_SHORT).show();
        }
    }).addOnCompleteListener(new OnCompleteListener<Void>() {
        @Override
        public void onComplete(@NonNull Task<Void> task) {
            Toast.makeText(AddRemainder.this, "This remainder has been activated.", Toast.LENGTH_SHORT).show();
        }
    });
}

        }
        else{
            edt1.setError("Empty Field....");
        }
    }
    public class BackgroundTask extends AsyncTask<Void,Void,Void>{
public BackgroundTask(AddRemainder activity){
    dialog = new ProgressDialog(activity);

}
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Thread.sleep(2000);
                InsertData();
            }catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
    dialog.setMessage("Adding this Remainder...");
            dialog.show();
            super.onPreExecute();
        }

    }
}
