package com.example.pratyush.completefirestore;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static String KEY_TITLE="title";
    public static String KEY_DESCRIPTION="description";
    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewData;
//    for setting up firebase database
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
//    for fetching data from firestore database
    private DocumentReference noteRef=db.collection("Notebook").document("My First Note");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextTitle=findViewById(R.id.edit_text_title);
        editTextDescription=findViewById(R.id.edit_text_description);
        textViewData=findViewById(R.id.text_view_data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e!=null){
                    Toast.makeText(MainActivity.this, "Error while loading", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    return;
                }
                if (documentSnapshot.exists()){
                    String title=documentSnapshot.getString(KEY_TITLE);
                    String description=documentSnapshot.getString(KEY_DESCRIPTION);
                    textViewData.setText("Title: "+title+"\n"+"Description: "+description);
                }
                else {
                    textViewData.setText("");
                }
            }
        });
    }

    public void saveNote(View v){
        String title=editTextTitle.getText().toString();
        String description=editTextDescription.getText().toString();
//      for putting the above data in a container, using Map interface
        Map<String, Object> note=new HashMap<>();

//        here we are inserting our key value pair
        note.put(KEY_TITLE, title);
        note.put(KEY_DESCRIPTION,description);

//       passing the note value to our firebase database
//        db.collection("Notebook/My First Note") is also an another method to create collection and documents
        db.collection("Notebook").document("My First Note").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());

                    }
                });

    }
    public void updateDescription(View v){
        String description=editTextDescription.getText().toString();
//        Map<String, Object> note=new HashMap<>();
//        note.put(KEY_DESCRIPTION,description);
//        noteRef.set(note, SetOptions.merge());
        noteRef.update(KEY_DESCRIPTION, description);
    }
    public void deleteDescription(View v){
//        Map<String, Object> note=new HashMap<>();
//        note.put(KEY_DESCRIPTION, FieldValue.delete());
//        noteRef.update(note);
        noteRef.update(KEY_DESCRIPTION,FieldValue.delete());
    }
    public void deleteNote(View v){
        noteRef.delete();
    }
    public void loadNote(View v){
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String title=documentSnapshot.getString(KEY_TITLE);
                            String description=documentSnapshot.getString(KEY_DESCRIPTION);
//                            Map<String,Object> note=documentSnapshot.getData();
                            textViewData.setText("Title: "+title+"\n"+"Description: "+description);
                        }
                        else {
                            Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_SHORT).show();
                        Log.d(TAG,e.toString());
                    }
                });
    }
}
