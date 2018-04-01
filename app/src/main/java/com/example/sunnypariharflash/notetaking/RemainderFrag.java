package com.example.sunnypariharflash.notetaking;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class RemainderFrag extends Fragment {
    ListView listView;
    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference ref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vv = inflater.inflate(R.layout.fragment_remainder, container, false);
    listView = vv.findViewById(R.id.listwa);
    auth = FirebaseAuth.getInstance();
    user = auth.getCurrentUser();
    ref = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(user.getUid()).child("Remainders");
        FirebaseListAdapter<Remainders> adapter = new FirebaseListAdapter<Remainders>(getActivity(),Remainders.class,R.layout.listforreaminder,ref) {
            @Override
            protected void populateView(View v, Remainders model, int position) {
                TextView t1,t2,t3;
                t1 = v.findViewById(R.id.SubjectofRemain);
                t2 = v.findViewById(R.id.DateofRemain);
                t3 = v.findViewById(R.id.TimeofRemain);
                t1.setText(model.Subject);
                t2.setText(model.Date);
                t3.setText(model.Time);
            }
        };
        listView.setAdapter(adapter);
        return vv;
    }

}
