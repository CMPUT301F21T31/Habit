package com.example.habit;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class tutorial_1 extends AppCompatActivity {
Button t_1b;
EditText t_1t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial1);
        t_1t=findViewById(R.id.t_1t);
        t_1b=findViewById(R.id.t_1b);


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
    }

    private void opent_2() { Intent intent=new Intent(this,tutorial_2usern.class);
        SharedPreferences prefs=getSharedPreferences("prefs",MODE_PRIVATE);
        String inname= prefs.getString("firstStartname","");

        intent.putExtra("fname", inname);
       // startActivity(intent);
    startActivity(intent);
    }
}