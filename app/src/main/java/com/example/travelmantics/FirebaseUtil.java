package com.example.travelmantics;

import android.app.Activity;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

public class FirebaseUtil {

    private static final int RC_SIGN_IN = 123;

    private static Activity caller;

    public static FirebaseDatabase mFirebaseDatabase;

    public static DatabaseReference mDatabaseReference;

    private static FirebaseUtil firebaseUtil;

    public static FirebaseAuth mFirebaseAuth;

    public static FirebaseAuth.AuthStateListener mAuthStateListener;

    public static ArrayList<TravelDeal> mTravelDeals;

    private FirebaseUtil(){};


    public static void openFbReference(String reference, final Activity callerActivity) {


        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();

            mFirebaseAuth = FirebaseAuth.getInstance();

            caller = callerActivity;

            mAuthStateListener  = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                    if (firebaseAuth.getCurrentUser() == null) {
                        FirebaseUtil.signIn();

                    }
                    Toast.makeText(callerActivity.getBaseContext(), "Welcome ...", Toast.LENGTH_SHORT).show();
                }
            };

        }

        mTravelDeals = new ArrayList<TravelDeal>();


        mDatabaseReference = mFirebaseDatabase.getReference().child(reference);
    }

    public static void attachListener() {
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    public static void detachListener() {
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
    }

    private static void signIn() {

        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


// Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }



}
