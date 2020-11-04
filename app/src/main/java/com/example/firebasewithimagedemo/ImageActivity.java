package com.example.firebasewithimagedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ImageActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    MyAdapter myAdapter;
    List<Upload>uploadList;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        recyclerView=findViewById(R.id.recyclerviewId);
        recyclerView.setHasFixedSize(true);
        progressBar=findViewById(R.id.imageProgressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        uploadList=new ArrayList<>();

        firebaseStorage=FirebaseStorage.getInstance();

        databaseReference= FirebaseDatabase.getInstance().getReference("Upload");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //duplicate value remove
                uploadList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Upload upload=dataSnapshot.getValue(Upload.class);
                    assert upload != null;
                    upload.setKey(dataSnapshot.getKey());
                    uploadList.add(upload);
                }
                myAdapter=new MyAdapter(ImageActivity.this,uploadList);
                recyclerView.setAdapter(myAdapter);

                myAdapter.setOnItemClickListener(new MyAdapter.onItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        String name=uploadList.get(position).getImageName();
                        Toast.makeText(ImageActivity.this, name+"is called"+position, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void doAnuTask(int position) {
                        Toast.makeText(ImageActivity.this, "do any task selected"+position, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void delete(int position) {
                        //get for selected item position
                        Upload selectItem=uploadList.get(position);

                        //get for selected item key
                        final String key=selectItem.getKey();

                        StorageReference storageReference=firebaseStorage.getReferenceFromUrl(selectItem.getImageUri());
                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                            databaseReference.child(key).removeValue();
                                }
                            });
                    }
                });


               progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(ImageActivity.this, "Error: "+error.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}