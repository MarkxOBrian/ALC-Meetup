package com.example.android.mydiary;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Main2Activity extends AppCompatActivity {
   EditText edi;
   Button btn;
   RecyclerView rcview;
   DatabaseReference data;
   FirebaseAuth mAuth;
   String user;
   String diarytext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
         rcview = (RecyclerView)findViewById(R.id.recyclerview);
         LinearLayoutManager ly = new LinearLayoutManager(Main2Activity.this);
         ly.setReverseLayout(false);
         ly.setStackFromEnd(true);
         edi = (EditText)findViewById(R.id.editText);
         btn = (Button)findViewById(R.id.button);
         btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 diarytext = edi.getText().toString();
                 edi.setText("");
                 post();
             }
         });
         mAuth = FirebaseAuth.getInstance();
         user = mAuth.getCurrentUser().getUid();
         data = FirebaseDatabase.getInstance().getReference().child("Marks Diary");

    }

    private void post() {
        final DatabaseReference postingtext = data.push();
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             postingtext.child("diarytext").setValue(diarytext).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     Toast.makeText(Main2Activity.this, "posted", Toast.LENGTH_SHORT).show();
                 }
             });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<DiaryObject,DiaryHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<DiaryObject, DiaryHolder>(
                DiaryObject.class,
                R.layout.text,
                DiaryHolder.class,
                data) {
            @Override
            protected void populateViewHolder(final DiaryHolder diaryHolder,final DiaryObject diaryObject,int position){
               diaryHolder.settext(diaryObject.getText());
            }
        };
        rcview.setAdapter(firebaseRecyclerAdapter);
    }
    public static class DiaryHolder extends RecyclerView.ViewHolder{
        View mitemview;
        public DiaryHolder(View itemView) {
            super(itemView);
            mitemview = itemView;
        }
        public void settext (String text){
            TextView teext = (TextView)itemView.findViewById(R.id.text);
            teext.setText(text);
        }
    }
}
