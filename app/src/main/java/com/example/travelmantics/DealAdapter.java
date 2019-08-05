package com.example.travelmantics;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> {

    ArrayList<TravelDeal> travelDeals;

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference mDatabaseReference;

    private ChildEventListener mChildEventListener;

    public DealAdapter() {

        FirebaseUtil.openFbReference("traveldeals");

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;

        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        travelDeals = FirebaseUtil.mTravelDeals;

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                TravelDeal travelDeal = dataSnapshot.getValue(TravelDeal.class);

                Log.d("Deal  = ", travelDeal.getTitle());

                travelDeal.setId(dataSnapshot.getKey());

                travelDeals.add(travelDeal);

                notifyItemInserted(travelDeals.size() - 1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        mDatabaseReference.addChildEventListener(mChildEventListener);
    }


    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View itemView = LayoutInflater.from(context).inflate(R.layout.rv_row, parent, false);
        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        TravelDeal travelDeal = travelDeals.get(position);

        holder.bind(travelDeal);
    }

    @Override
    public int getItemCount() {
        return travelDeals.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        TextView tvDescription;

        TextView tvPrice;

        public DealViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);

            tvDescription = itemView.findViewById(R.id.tvDescription);

            tvPrice = itemView.findViewById(R.id.tvPrice);


        }

        public void bind(TravelDeal travelDeal) {
            tvTitle.setText(travelDeal.getTitle());
            tvDescription.setText(travelDeal.getDescription());
            tvPrice.setText(travelDeal.getPrice());
        }
    }
}
