package com.example.sunnypariharflash.notetaking;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Main3Activity extends AppCompatActivity {
private Toolbar toolbar;
    private EditText editText,editText2;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference dataNote;
    private ProgressDialog dialog;
    String titles;
    private RatingBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
//    toolbar = findViewById(R.id.tool_bar);
  //  setActionBar(toolbar);
        editText = findViewById(R.id.texttitle);
        editText2 = findViewById(R.id.notedatatext);
    auth = FirebaseAuth.getInstance();
bar = findViewById(R.id.starhaibhai);
           }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

     if (item.getItemId()==R.id.action_userd){
         Toast.makeText(this, "this wil update your data in firebase.", Toast.LENGTH_SHORT).show();
BackgroundTask tasks = new BackgroundTask(this);
tasks.execute();
     }
     else if (item.getItemId()==R.id.action_cross){
         Toast.makeText(this, "This will clear the note.", Toast.LENGTH_SHORT).show();
     onBackPressed();
     }
     
        return super.onOptionsItemSelected(item);
    }
    private List<NoteData> NoteInfo(String Title,String Note,String date,String time,String star){
        List<NoteData> notes = new ArrayList<>();
        NoteData datastd = new NoteData(Title,Note,date,time,star);
        notes.add(datastd);
        return notes;
    }
    private void  InsertData(){
        if(!editText.getText().toString().equals("")){
             titles = editText.getText().toString();
            user = auth.getCurrentUser();
            String uid = user.getUid();
            reference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid());
            dataNote = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid()).child("notes").child(titles);
            // dataNote.setValue(new NoteData(title,editText.getText().toString()));
            Calendar c = Calendar.getInstance();
            Date dt  = new Date();
            int h = dt.getHours();
            int min = dt.getMinutes();
            int sec = dt.getSeconds();
            String time  = h + ":" + min + ":"+ sec;
            String date = c.get(Calendar.YEAR)+ "-"+c.get(Calendar.MONTH)+"-"+c.get(Calendar.DAY_OF_MONTH);
            String starkadata = "false";
            if(bar.getNumStars()==0||bar.getNumStars()==0.5){
             starkadata = "false";   
            }
            else if (bar.getNumStars()==1){
                starkadata = "true";
            }
            List<NoteData> lists = NoteInfo(editText.getText().toString(),editText2.getText().toString(),date,time,starkadata);
            for(NoteData litd : lists){
                dataNote.setValue(litd).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Main3Activity.this, "Unable to add data.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(Main3Activity.this, "Data has been added.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            // Toast.makeText(this, "Data Has been added...", Toast.LENGTH_SHORT).show();

        }
    }
    private class BackgroundTask extends AsyncTask<Void,Void,Void>{
public BackgroundTask(Main3Activity activity){
    dialog = new ProgressDialog(activity);
}
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Adding Your Note....");
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dialog.dismiss();
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

    }
}
