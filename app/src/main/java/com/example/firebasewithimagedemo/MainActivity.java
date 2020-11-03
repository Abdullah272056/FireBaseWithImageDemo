package com.example.firebasewithimagedemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button chooseButton,saveButton,displayButton;
    ProgressBar progressBar;
    EditText imageNameExitText;
    ImageView imageView;
    private Uri imageUri;
    private static final  int IMAGE_REQUEST=1;
    // request.auth != null

    DatabaseReference databaseReference;
    StorageReference storageReference;

    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //finding view
        chooseButton=findViewById(R.id.chooseImageButtonId);
        saveButton=findViewById(R.id.saveImageButtonId);
        displayButton=findViewById(R.id.displayImageButtonId);
        progressBar=findViewById(R.id.progressbarId);
        imageNameExitText=findViewById(R.id.imageNameEditTextIdId);
        imageView=findViewById(R.id.imageViewId);

        databaseReference= FirebaseDatabase.getInstance().getReference("Upload");
        storageReference= FirebaseStorage.getInstance().getReference("Upload");

        //add listener
        chooseButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        displayButton.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
    switch (v.getId()){
        case R.id.chooseImageButtonId:
            openImageFileChooser();
            break;
        case R.id.saveImageButtonId:
            if (uploadTask!=null && uploadTask.isInProgress()){
                Toast.makeText(this, "Image uploading ", Toast.LENGTH_SHORT).show();
            }else {
                saveData();
            }
            break;
        case  R.id.displayImageButtonId:
            break;
    }
    }
   // getting extension of the image
    public String getImageFileExtension(Uri imageUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));

    }

    private void saveData() {
        final String imageName=imageNameExitText.getText().toString();
        if (!TextUtils.isEmpty(imageName)){

        }else {
            imageNameExitText.setError("Enter image name");
            //error showing 
            imageNameExitText.requestFocus();
            return;
        }

        StorageReference ref=storageReference.child(System.currentTimeMillis()+"."+getImageFileExtension(imageUri));
        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Toast.makeText(MainActivity.this, "Image is stored successful", Toast.LENGTH_SHORT).show();

                UploadModelClass upload=new UploadModelClass(imageName,
                        taskSnapshot.getStorage().getDownloadUrl().toString());
                String uploadId=databaseReference.push().getKey();
                databaseReference.child(uploadId).setValue(upload);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        Toast.makeText(MainActivity.this, "Image is not  stored successful", Toast.LENGTH_SHORT).show();

                    }
                });
        

    }

    //create openImageFileChooser for all image file finding or image file accessing
    private void openImageFileChooser() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);

    }

    // call onActivityResult override method  for chooser image get and
    // come back with image and load image into imageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null
                && data.getData()!=null){
            imageUri=data.getData();
            Picasso.with(MainActivity.this).load(imageUri).into(imageView);
        }
    }
}