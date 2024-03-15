package com.example.vm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button submit;
    EditText usrnbr;
    EditText usrpass;

    Long user;

    Integer pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        usrnbr = findViewById(R.id.number);
        usrpass = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
        CheckBox rememberMeCheckbox = findViewById(R.id.remember_me);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        boolean rememberMe = sharedPreferences.getBoolean("rememberMe", false);
        rememberMeCheckbox.setChecked(rememberMe);

        rememberMeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("rememberMe", isChecked);
            editor.apply();
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = Long.valueOf(usrnbr.getText().toString());
                pass = Integer.valueOf(usrpass.getText().toString());
                if (rememberMe) {
                    // Save the username and password, or implement your desired behavior
                    // Note: Storing passwords directly like this is not secure and is for demonstration purposes only.
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", String.valueOf(user));
                    editor.putString("password", String.valueOf(pass));
                    editor.apply();
                } else {
                    // Clear saved username and password
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove("username");
                    editor.remove("password");
                    editor.apply();
                }

                if(user >=10) {
                    if (user == 9843172499L && pass == 2001) {
                        Intent indent = new Intent(MainActivity.this, Homepage.class);
                        startActivity(indent);
                        Toast.makeText(MainActivity.this, "Worked", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Not working", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}