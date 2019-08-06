package com.example.travelmantics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;


public class DealActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference mDatabaseReference;

    EditText textTitle;

    EditText textPrice;

    EditText textDescription;

    TravelDeal mDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        FirebaseUtil.openFbReference("traveldeals", this);

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;

        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        textTitle = findViewById(R.id.txtTitle);

        textPrice = findViewById(R.id.txtPrice);

        textDescription = findViewById(R.id.txtDescription);

        Intent intent = getIntent();

        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");

        if (deal == null) {
            deal = new TravelDeal();
        }

        mDeal = deal;

        textTitle.setText(deal.getTitle());
        textDescription.setText(deal.getDescription());
        textPrice.setText(deal.getPrice());

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
                navigateToList();

                return true;

            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this, "Deal has been deleted.", Toast.LENGTH_LONG).show();
                navigateToList();


            default :
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

        mDeal.setTitle(textTitle.getText().toString());

        mDeal.setDescription(textDescription.getText().toString());

        mDeal.setPrice(textPrice.getText().toString());

        if (mDeal.getId() == null) {

            mDatabaseReference.push().setValue(mDeal);

        } else {

            mDatabaseReference.child(mDeal.getId()).setValue(mDeal);

        }




    }

    private void deleteDeal() {


        if (mDeal.getId() == null) {

            Toast.makeText(this, "Save deal before delete.", Toast.LENGTH_SHORT);

            return;

        }

        mDatabaseReference.child(mDeal.getId()).removeValue();

    }

    private void navigateToList() {

        Intent intent = new Intent(this, ListActivity.class);

        startActivity(intent);

    }
}
