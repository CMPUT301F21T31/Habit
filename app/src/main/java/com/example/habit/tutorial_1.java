package com.example.habit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class tutorial_1 extends AppCompatActivity {
    Button t_1b;
    Button skipToLoginButton;
    EditText t_1t;
    Button testmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial1);
        t_1t=findViewById(R.id.t_1t);
        t_1b=findViewById(R.id.t_1b);
        testmap=findViewById(R.id.test_map);
        skipToLoginButton = findViewById(R.id.goTo_login_button);
        testmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotomap();
            }
        });

        //Toast.makeText(MainActivity.this,"firststart?"+String.valueOf(firstStart),Toast.LENGTH_LONG).show();

        t_1b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = t_1t.getText().toString();
                SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
                SharedPreferences.Editor editor=prefs.edit();
                editor.putString("firstStartname",name);
                editor.apply();
                String inname= prefs.getString("firstStartname","");
                //String uniqueID =prefs.getString("UID","");
                //Toast.makeText(tutorial_1.this,inname+uniqueID,Toast.LENGTH_LONG).show();

                opent_2();

            }
        });

        skipToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(tutorial_1.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void gotomap() {

        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
       fragmentTransaction.replace(R.id.map,new MapsFragment()).commit();

    }

    private void opent_2() { Intent intent=new Intent(this,tutorial_2usern.class);
        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        String inname= prefs.getString("firstStartname","");

        intent.putExtra("fname", inname);
        // startActivity(intent);
        startActivity(intent);
    }
}
