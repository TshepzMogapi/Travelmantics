package com.example.travelmantics;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class DealActivity extends AppCompatActivity {

    private static final int PICTURE_RESULT = 42;

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference mDatabaseReference;

    EditText textTitle;

    EditText textPrice;

    EditText textDescription;

    ImageView imageView;

    TravelDeal mDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;

        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        textTitle = findViewById(R.id.txtTitle);

        textPrice = findViewById(R.id.txtPrice);

        textDescription = findViewById(R.id.txtDescription);

        imageView = findViewById(R.id.image);

        Intent intent = getIntent();

        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");

        if (deal == null) {
            deal = new TravelDeal();
        }

        mDeal = deal;

        textTitle.setText(deal.getTitle());
        textDescription.setText(deal.getDescription());
        textPrice.setText(deal.getPrice());

        showImage(deal.getImageUrl());

        Button btnImage = findViewById(R.id.btnImage);

        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");

                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Insert an Image"),
                        PICTURE_RESULT);

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.save_menu, menu);

        if (FirebaseUtil.isAdmin) {
            menu.findItem(R.id.save_menu).setVisible(true);
            menu.findItem(R.id.delete_menu).setVisible(true);
            allowEdit(true);
            findViewById(R.id.btnImage).setEnabled(true);
        } else  {
            menu.findItem(R.id.save_menu).setVisible(false);
            menu.findItem(R.id.delete_menu).setVisible(false);
            allowEdit(false);
            findViewById(R.id.btnImage).setEnabled(false);


        }

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_RESULT && resultCode == RESULT_OK) {

            Uri imageUri = data.getData();
            StorageReference reference = FirebaseUtil.mStorageReference.child(imageUri.getLastPathSegment());

            reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    final String url = String.valueOf(downloadUrl);

                    Log.d("Image", url);

                    mDeal.setImageUrl(url);
                    String picName = taskSnapshot.getStorage().getPath();
                    mDeal.setImageName(picName);
                    showImage(url);
                }
            });

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

        if (mDeal.getImageName() != null && !mDeal.getImageName().isEmpty()) {

            StorageReference picStorageReference = FirebaseUtil.mFirebaseStorage
                    .getReference().child(mDeal.getImageName());

            picStorageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("Delete Image", "Image Deleted");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.d("Delete Image", e.getMessage());

                }
            });

        }

    }

    private void navigateToList() {

        Intent intent = new Intent(this, ListActivity.class);

        startActivity(intent);

    }

    private void allowEdit(boolean isAllowed) {
        textTitle.setEnabled(isAllowed);
        textPrice.setEnabled(isAllowed);
        textDescription.setEnabled(isAllowed);
    }

    private void showImage(String imageUrl) {

        if (imageUrl != null && !imageUrl.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;

            Picasso.get().load(imageUrl).resize(width, width * 2/3).centerCrop()
                    .into(imageView);
        }

    }
}
