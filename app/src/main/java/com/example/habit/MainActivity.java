package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button fisrtstartrest;
    boolean isPersmissionGranter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        checkpermission();
        if(isPersmissionGranter){
            if(checkGoogleplaServices()){
                Toast.makeText(this,"Google playservices Available",Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(this,"Google playservices NOT Available",Toast.LENGTH_SHORT).show();
            }
        }
Toast.makeText(MainActivity.this,"firststart?"+String.valueOf(firstStart),Toast.LENGTH_LONG).show();
    if (true){ // always open for now

            opentutorial();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();

        fisrtstartrest=findViewById(R.id.fisrtstartrest);
        fisrtstartrest.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
                  SharedPreferences.Editor editor=prefs.edit();
                  editor.putBoolean("firstStart",true);
                  editor.apply();
              }
          });
        } else {
        splashwelcome();
            // openHabitList();
        }
    }

    private boolean checkGoogleplaServices() {
        GoogleApiAvailability googleApiAvailability=GoogleApiAvailability.getInstance();
        int result=googleApiAvailability.isGooglePlayServicesAvailable(this);
        if(result== ConnectionResult.SUCCESS){
            return true;
        }return false;
    }

    private void checkpermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                isPersmissionGranter=true;
                Toast.makeText(MainActivity.this,"permission Granter",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri=Uri.fromParts("package",getPackageName(),"");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

            }
        }).check();
    }

    private void opentutorial() {
        Intent intent=new Intent(this,tutorial_1.class);
        startActivity(intent);
       // SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
       // SharedPreferences.Editor editor=prefs.edit();
       // editor.putBoolean("firstStart",true);
      //  editor.apply();

    }

    private void openHabitList() {
        Intent intent = new Intent(this, HabitListActivity.class);
        startActivity(intent);
    }

    private void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    private void splashwelcome(){
        Intent intent = new Intent(this, splashscreen.class);
        startActivity(intent);
    }
}