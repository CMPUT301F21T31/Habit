//package com.example.habit;
//
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//
//public class camera extends AppCompatActivity {
//
//    //Initialize Variables
//    ImageView imageView;
//    Button btOpen;
//    Button done;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_camera);
//
//        //Assign Variables
//        imageView = findViewById(R.id.imageView);
//        btOpen = findViewById(R.id.bt_open);
//        done= findViewById(R.id.donecam);
//
//        //Request For Camera Permission
//        if (ContextCompat.checkSelfPermission(camera.this,
//                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(camera.this,new String[]{
//                    Manifest.permission.CAMERA
//            },100);
//        }
//        done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//        btOpen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                // Open Camera
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent,100);
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100) {
//
//            //Get Capture Image
//            Bitmap captureImage = (Bitmap) data.getExtras().get("data");
//
//            //Set Capture Image to ImageView
//            imageView.setImageBitmap(captureImage);
//            Intent intent = new Intent(this, camera.class);
//            intent.putExtra("bitmap", captureImage);
//            imageView.setImageBitmap(intent.getParcelableExtra("bitmap"));
//        }
//    }
//}