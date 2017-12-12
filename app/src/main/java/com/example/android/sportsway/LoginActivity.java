package com.example.android.sportsway;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.sportsway.Model.SportswayContract;
import com.example.android.sportsway.Model.SportswayDbHelper;

public class LoginActivity extends AppCompatActivity {

    private SportswayDbHelper mDbHelper;
    private Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        this.mDbHelper = new SportswayDbHelper(this);
        SQLiteDatabase db = this.mDbHelper.getReadableDatabase();
        this.mCursor = db.query(SportswayContract.UserEntry.TABLE_NAME,
                new String[]{SportswayContract.UserEntry.COLUMN_EMAIL, SportswayContract.UserEntry.COLUMN_PASSWORD},
                null, null, null, null, null);
    }

    public void login(View view) {
        EditText et1 = (EditText)findViewById(R.id.username);
        EditText et2 = (EditText)findViewById(R.id.password);
        String etEmail = et1.getText().toString();
        String etPassword = et2.getText().toString();

        int user_email = this.mCursor.getColumnIndex(SportswayContract.UserEntry.COLUMN_EMAIL);
        int user_password = this.mCursor.getColumnIndex(SportswayContract.UserEntry.COLUMN_PASSWORD);

        boolean found = false;
        this.mCursor.moveToPosition(-1);
        while (this.mCursor.moveToNext()) {
            String email = this.mCursor.getString(user_email);
            String password = this.mCursor.getString(user_password);
            Log.d(this.getClass().getSimpleName(), "Email: " + email);
            Log.d(this.getClass().getSimpleName(), "Password: " + password);
            if (email.equals(etEmail) && password.equals(etPassword)) {
                found = true;
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                finish();
            }
        }

        if (!found) {
            Snackbar.make(view, "Invalid username or password try again", Snackbar.LENGTH_SHORT).show();
        }

    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mCursor.close();
    }
}
