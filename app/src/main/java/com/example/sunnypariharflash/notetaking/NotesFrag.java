package com.example.sunnypariharflash.notetaking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFrag extends Fragment {


    public NotesFrag() {
        // Required empty public constructor
    }
DatabaseReference reference;
private ListView listView;
FirebaseAuth auth;
FirebaseUser user;
List<NoteData> notesd = new ArrayList<>();
DatabaseReference databaseReference;
    private ArrayList<String> mMeetings = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vv = inflater.inflate(R.layout.fragment_notes, container, false);
    listView = vv.findViewById(R.id.listststs);
    auth = FirebaseAuth.getInstance();
    user = auth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid()).child("notes");
    FirebaseListAdapter<NoteData> listAdapter = new FirebaseListAdapter<NoteData>(getActivity(),NoteData.class,R.layout.listsetup,reference) {
        @Override
        protected void populateView(View v, NoteData model, int position) {
            TextView t1, t2, t3, t4;
            RatingBar trate;
            t1 = v.findViewById(R.id.titlelistsetup);
            t2 = v.findViewById(R.id.noteslistsetup);
            t3 = v.findViewById(R.id.datelistsetup);
            t4 = v.findViewById(R.id.timelistsetup);
            trate = v.findViewById(R.id.ratinglistsetup);
            t1.setText(model.Title);
            String datatat = model.Note;
          int length = 10;
          if(length>datatat.length()){
              length = datatat.length();
          }
            t2.setText(datatat.substring(0,length));
            t3.setText(model.Date);
            t4.setText(model.Time);
            if (model.Star.equals("true")) {
                trate.setRating(1);
            }
            else{
                trate.setRating(0);
            }
        }
    };
    listView.setAdapter(listAdapter);
        return vv;
    }

}

