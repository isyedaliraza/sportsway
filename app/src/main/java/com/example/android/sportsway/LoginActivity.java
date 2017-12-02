package com.example.android.sportsway;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    public void login(View view) {
        EditText et1 = (EditText)findViewById(R.id.username);
        EditText et2 = (EditText)findViewById(R.id.password);
        String username = et1.getText().toString();
        String password = et2.getText().toString();

        if(username.equals("hamza") && password.equals("admin")) {
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }
}
