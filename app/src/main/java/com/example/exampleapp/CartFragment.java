package com.example.exampleapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    List<Cart> cartList;


    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root =  inflater.inflate(R.layout.fragment_cart, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerView = root.findViewById(R.id.recyclerview_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(getActivity(), cartList);

        recyclerView.setAdapter(cartAdapter);

        firebaseDatabase.getReference("Cart").child(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    for(DataSnapshot dataSnapshot: task.getResult().getChildren()){
                        Cart cart = dataSnapshot.getValue(Cart.class);
                        cartList.add(cart);
                        cartAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


        return root;
    }
}