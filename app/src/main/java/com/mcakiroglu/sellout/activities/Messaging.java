package com.mcakiroglu.sellout.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.models.Message;

public class Messaging extends AppCompatActivity {
    LinearLayout linear;
    ScrollView scrollView;
    DatabaseReference ref;
    EditText messageArea;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    ImageView send;
    String toid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);

        Intent intent = getIntent();
        toid =intent.getStringExtra("toid");
        scrollView = findViewById(R.id.scrollView);
        linear = findViewById(R.id.layout1);
        messageArea = findViewById(R.id.messageArea);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference("messages");
        send = findViewById(R.id.sendButton);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){



                    Message m = new Message(user.getUid(),messageText,"2020-05-05 11:38",toid,"0");

                    ref.child(user.getUid()).child(toid).push().setValue(m);
                    ref.child(toid).child(user.getUid()).push().setValue(m);




                    messageArea.setText("");

                }

            }
        });

        ref.child(user.getUid()).child(toid).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {

                //Iterable<DataSnapshot> iterable =dataSnapshot.getChildren();
             //   for(DataSnapshot snap :data){
                    System.out.println("snap" + dataSnapshot);
                    String message = dataSnapshot.child("message").getValue().toString();
                    String from = dataSnapshot.child("fromID").getValue().toString();
                   // String to = snap.child("toID").getValue().toString();
                    //.out.println(to + "lNWT" + toid);
                    if(user.getUid().equals(from)){
                        addMessageBox(message, 1);
                    }
                    else{
                        addMessageBox(message, 2);
                    }


                }
           // }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Messaging.this);
        textView.setTextSize(25);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 7.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            lp2.topMargin = 20;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            lp2.topMargin = 20;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        linear.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }
    public void onBackPressed(){
        startActivity(new Intent(Messaging.this,Messages.class));
    }


}
