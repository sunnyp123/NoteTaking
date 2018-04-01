package com.example.sunnypariharflash.notetaking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AddRemainder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remainder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
if(item.getItemId()==R.id.action_userd){
    Toast.makeText(this, "This will add your remainder.", Toast.LENGTH_SHORT).show();
}
else if(item.getItemId()==R.id.action_cross){
    Toast.makeText(this, "This will cancel the updating remainder.", Toast.LENGTH_SHORT).show();
}
        return super.onOptionsItemSelected(item);
    }
}
