package com.example.pratyush.completefirestore;

import android.provider.ContactsContract;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

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
    private CollectionReference notebookRef=db.collection("Notebook");
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
        //for showing data while after app installation
        notebookRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e){
                if (e!=null){
                    return;
                }
                String data="";
                for (QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                    NoteModel note=documentSnapshot.toObject(NoteModel.class);
                    String title=note.getTitle();
                    String description=note.getDescription();
                    data+="Title: "+title+"\nDescription "+description+"\n\n";
                }
                textViewData.setText(data);
            }
        });
    }

    public void addNote(View v){
        String title=editTextTitle.getText().toString();
        String description=editTextDescription.getText().toString();

        NoteModel note=new NoteModel(title,description);
        notebookRef.add(note);

    }
    public void loadNotes(View v) {
        notebookRef.get()
//                QuerySnapshot here contains multiple snapshot of document
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data="";
                        for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots){
                            NoteModel note=documentSnapshot.toObject(NoteModel.class);
                            String title=note.getTitle();
                            String description=note.getDescription();
                            data+="Title: "+title+"\nDescription: "+description+"\n\n";
                        }
                        textViewData.setText(data);
                    }
                });
    }
}
