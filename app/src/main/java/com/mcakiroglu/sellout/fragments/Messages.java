package com.mcakiroglu.sellout.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.activities.Messaging;
import com.mcakiroglu.sellout.databinding.ActivityMessagesBinding;
import com.mcakiroglu.sellout.databinding.ActivityMyStuffBinding;
import com.mcakiroglu.sellout.fragments.MyStuff;

import java.util.ArrayList;

import static com.mcakiroglu.sellout.R.layout.activity_messages;

public class Messages extends Fragment {
    BottomNavigationView bnw;
    DatabaseReference ref,ref2;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    ArrayList<String> ids = new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ActivityMessagesBinding binding;

    ListView uList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = ActivityMessagesBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();




        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        handlers();
    }

    protected void init(){
        uList = binding.listw;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("messages");
        ref2 = database.getReference("users");

    }

    protected void handlers(){

        ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> iterable =dataSnapshot.getChildren();
                for(DataSnapshot snap :iterable) {



                    ids.add(snap.getKey());
                    ref2.child(snap.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            names.add(dataSnapshot.child("username").getValue().toString());
                            uList.invalidateViews();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }




                ArrayAdapter adapter;
                adapter= new ArrayAdapter<String>(getContext(),R.layout.simple_list_item_1,names);

                uList.setAdapter(adapter);
                uList.setVisibility(View.VISIBLE);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }








        });

        uList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), Messaging.class);
                intent.putExtra("toid",ids.get(position));
                startActivity(intent);
            }
        });
    }






}
