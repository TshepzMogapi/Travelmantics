package com.example.travelmantics;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;

    public static DatabaseReference mDatabaseReference;

    private static FirebaseUtil firebaseUtil;

    public static ArrayList<TravelDeal> mTravelDeals;

    private FirebaseUtil(){};


    public static void openFbReference(String reference) {


        if (firebaseUtil == null) {
            firebaseUtil = new FirebaseUtil();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mTravelDeals = new ArrayList<TravelDeal>();
        }

        mDatabaseReference = mFirebaseDatabase.getReference().child(reference);
    }



}