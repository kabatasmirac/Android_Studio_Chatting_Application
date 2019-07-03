package com.example.firebaserev1;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Chat extends AppCompatActivity {

    LinearLayout layout;
    RelativeLayout layout_2;
    Button sendButton;
    EditText messageArea;
    ScrollView scrollView;
    DatabaseReference reference1, reference2,reference3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (Button)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        reference1 = FirebaseDatabase.getInstance().getReference("messages/"+UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = FirebaseDatabase.getInstance().getReference("messages/"+ UserDetails.chatWith + "_" + UserDetails.username);
        //broadcast adında 3. veritabanı referansı oluşturuldu
//
        reference3 = FirebaseDatabase.getInstance().getReference("messages/"+"broadcast");


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Message m = new Message(UserDetails.username,messageText);

                        reference1.push().setValue(m);
                        reference2.push().setValue(m);



                    messageArea.setText("");
                }
            }
        });
            // gelen broadcast mesajları istisnasız herkese gösterilmesi için listener eklendi
            reference3.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<Message> t = new GenericTypeIndicator<Message>() {};
                Message mess = dataSnapshot.getValue(t);

                String message = mess.getText();
                String userName = mess.getAuthor();
                    //ekrana mesaj ekleme
                    addMessageBox(userName+" (broadcast)" + ":-\n" + message, 2);
//push edilen mesaj cekilip ekrana basılıyort
            }

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

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                GenericTypeIndicator<Message> t = new GenericTypeIndicator<Message>() {};
                Message mess = dataSnapshot.getValue(t);

                String message = mess.getText();
                String userName = mess.getAuthor();
//ilk username mesajı yazan veritabanından çekilen ikinci user name login olurken kullanılan
                if(userName.equals(UserDetails.username)){
                        addMessageBox("You:-\n" + message, 1);

                }
                else{
                    addMessageBox(UserDetails.chatWith + ":-\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }


    public void addMessageBox(String message, int type){
        TextView textView = new TextView(Chat.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}
