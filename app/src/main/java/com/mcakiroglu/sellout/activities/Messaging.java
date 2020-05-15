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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mcakiroglu.sellout.R;
import com.mcakiroglu.sellout.models.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Messaging extends AppCompatActivity {
    LinearLayout linear;
    ScrollView scrollView;
    DatabaseReference ref;
    EditText messageArea;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    ImageView send;
    String toid;
    EditText text;
    ArrayList<String> readid = new ArrayList<>();

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAALJdeEzo:APA91bGOfkE8j5N_gt3puouWw8ALsq0PFcMAO_6SVQWq1bCfbviNpvr8MWkzEZDDQIcg_NDIePepIvNd2iVq3iHD5qpRFy2VyN6UiIIYEr94ufPV8RU8j57QpZ3YPpMv0GU_FyIjPD6m";
    final private String contentType = "application/json";
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
        text = findViewById(R.id.messageArea);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Date now = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm z");
                    String date = sdf.format(now);

                    Message m = new Message(user.getUid(),messageText,date,"0",toid);

                    ref.child(user.getUid()).child(toid).push().setValue(m);
                    ref.child(toid).child(user.getUid()).push().setValue(m);

                    notification(toid,"Yeni mesaj!",messageText,"message");



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
                    String time = dataSnapshot.child("timestamp").getValue().toString();
                    String r = dataSnapshot.child("read").getValue().toString();
                    String read;
                    if(from.equals(user.getUid())){

                    }else{
                        readid.add(dataSnapshot.getKey());

                    }




                   // String to = snap.child("toID").getValue().toString();
                    //.out.println(to + "lNWT" + toid);
                    if(user.getUid().equals(from)){
                        addMessageBox(message, 1);
                        addMessageBox(time + " ",3);
                    }
                    else{
                        addMessageBox(message, 2);
                        addMessageBox(time + " ",4);
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
        else if(type == 2){
            lp2.gravity = Gravity.RIGHT;
            lp2.topMargin = 20;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }else if(type == 3){
            lp2.gravity = Gravity.LEFT;
            lp2.topMargin = 5;
            textView.setTextSize(15);
            textView.setBackgroundResource(R.drawable.bubble);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            lp2.topMargin = 5;
            textView.setTextSize(15);
            textView.setBackgroundResource(R.drawable.bubble);

        }
        textView.setLayoutParams(lp2);
        linear.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);

    }
    public void onBackPressed(){

for(int i=0;i<readid.size();i++){
    ref.child(user.getUid()).child(toid).child(readid.get(i)).child("read").setValue("1");


}
        startActivity(new Intent(Messaging.this,Messages.class));
    }

    public void notification(String uid,String NOTIFICATION_TITLE, String NOTIFICATION_MESSAGE, String type){


        String TOPIC = "/topics/" + uid ;


        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();

        try {

            notifcationBody.put("type", type);
            notifcationBody.put("title", NOTIFICATION_TITLE);
            notifcationBody.put("message", NOTIFICATION_MESSAGE);
            notifcationBody.put("id", user.getUid());

            notification.put("to", TOPIC);
            notification.put("data", notifcationBody);

        } catch (JSONException e) {

        }

        sendNotification(notification);
    }

    private void sendNotification(JSONObject notification) {

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("onResponse: " + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(Messaging.this, "Request error", Toast.LENGTH_LONG).show();

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }


}
