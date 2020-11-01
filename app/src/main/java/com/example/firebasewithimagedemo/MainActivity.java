package com.example.firebasewithimagedemo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button chooseButton,saveButton,displayButton;
    ProgressBar progressBar;
    EditText imageNameExitText;
    ImageView imageView;
    private Uri imageUri;
    private static final  int IMAGE_REQUEST=1;
    // request.auth != null

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

            break;
        case  R.id.displayImageButtonId:

            break;
    }
    }

    private void openImageFileChooser() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }

  
}