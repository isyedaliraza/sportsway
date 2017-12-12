package com.example.android.sportsway;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.android.sportsway.Model.SportswayContract;
import com.example.android.sportsway.Model.SportswayDbHelper;

public class RegisterActivity extends AppCompatActivity {

    private SportswayDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        this.mDbHelper = new SportswayDbHelper(this);
    }

    public void registerUser(View view) {
        EditText name_et = (EditText) findViewById(R.id.name_et);
        EditText email_et = (EditText) findViewById(R.id.email_et);
        EditText password_et = (EditText) findViewById(R.id.password_et);

        String name = name_et.getText().toString();
        String email = email_et.getText().toString();
        String password = password_et.getText().toString();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Snackbar.make(view, "Please provide all details", Snackbar.LENGTH_SHORT).show();
        }
        else {
            SQLiteDatabase db = this.mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(SportswayContract.UserEntry.COLUMN_NAME, name);
            values.put(SportswayContract.UserEntry.COLUMN_EMAIL, email);
            values.put(SportswayContract.UserEntry.COLUMN_PASSWORD, password);
            long rowId = db.insert(SportswayContract.UserEntry.TABLE_NAME, null, values);
            if (rowId < 0) {
                Snackbar.make(view, "Something wrong try again", Snackbar.LENGTH_SHORT).show();
            }
            else {
                finish();
            }
        }
    }
}
