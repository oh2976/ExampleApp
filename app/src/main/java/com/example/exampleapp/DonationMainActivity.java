package com.example.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DonationMainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Donation> arrayList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;

    private FirebaseAuth firebaseAuth;

    TextView donationPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_main);

        recyclerView = findViewById(R.id.donationRecyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        arrayList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Donation");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot : datasnapshot.getChildren()){
                    Donation donation = snapshot.getValue(Donation.class);
                    arrayList.add(donation);
                    Log.d("DonationMainActivity", snapshot.getKey()+"");

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("DonationMainActivity", String.valueOf(error.toException()));

            }
        });

        adapter = new DonationAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

        donationPoint = (TextView) findViewById(R.id.donation_point);


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference2 = FirebaseDatabase.getInstance().getReference("UserAccount");

        databaseReference2.child("UserAccount").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
//                arrayList.clear(); //기존 배열 리스트가 존재하지 않게 남아 있는 데이터 초기화
                // 반복문으로 데이터 List를 추출해냄

                UserAccount userAccount = dataSnapshot.getValue(UserAccount.class); //  만들어 뒀던 Product 객체에 데이터를 담는다.
                    donationPoint.setText(userAccount.getUpoint() + " 씨드");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }
    }
