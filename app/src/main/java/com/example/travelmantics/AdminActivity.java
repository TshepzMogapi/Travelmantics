package com.example.travelmantics;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;


public class AdminActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference mDatabaseReference;

    EditText textTitle;

    EditText textPrice;

    EditText textDescription;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        FirebaseUtil.openFbReference("traveldeals");


        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;

        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        textTitle = findViewById(R.id.txtTitle);

        textPrice = findViewById(R.id.txtPrice);

        textDescription = findViewById(R.id.txtDescription);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.save_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.save_menu:

                saveDeal();

                Toast.makeText(this, "Saving Deal", Toast.LENGTH_LONG).show();
                
                clean();

                return true;

                default:
                    return super.onOptionsItemSelected(item);

        }
    }

    private void clean() {
        textPrice.setText("");
        textDescription.setText("");
        textTitle.setText("");

        textTitle.requestFocus();
    }

    private void saveDeal() {

        String title = textTitle.getText().toString();

        String description = textDescription.getText().toString();

        String price = textPrice.getText().toString();

        TravelDeal travelDeal = new TravelDeal(title, description, price, "");


        mDatabaseReference.push().setValue(travelDeal);
        
    }
}
