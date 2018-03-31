package com.example.sunnypariharflash.notetaking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


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
        final String[] d = new String[1];
        reference = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid()).child("notes");
    reference.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            d[0] = dataSnapshot.getValue().toString();
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
    databaseReference = reference.child(d[0]);
        FirebaseListAdapter<NoteData> adapter = new FirebaseListAdapter<NoteData>(getActivity(),NoteData.class,R.layout.listsetup,databaseReference) {
            @Override
            protected void populateView(View v, NoteData model, int position) {
                TextView textView,textView1,textView2,textView3;
                RatingBar ratingBar;
                textView =  v.findViewById(R.id.titlelistsetup);
                textView1 = v.findViewById(R.id.noteslistsetup);
                textView2 = v.findViewById(R.id.datelistsetup);
                textView3 = v.findViewById(R.id.timelistsetup);
                ratingBar = v.findViewById(R.id.ratinglistsetup);
                textView.setText(model.getTitle());
                textView1.setText(model.getNote());
                textView2.setText(model.getDate());
                textView3.setText(model.getTime());
                String datas = model.getStar();
               int rate = 0;
                if(datas=="true"){
                    rate = 1;
                }
                else {
                    rate = 0;
                }
                ratingBar.setNumStars(rate);
            }
        };
        listView.setAdapter(adapter);
        return vv;
    }

}
