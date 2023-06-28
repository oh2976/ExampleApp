package com.example.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonationDetailActivity extends AppCompatActivity {

    ImageView dodetailImg;
    ImageView dodetailLongImg;
    TextView dodetailName, dodetailStart, dodetailEnd, dodetailuPoint, dodetatilallPoint;

    Button doDonate;

    Donation donation = null;

    private DatabaseReference databaseReference2;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_detail);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Donation");

        final Object object = getIntent().getSerializableExtra("donationDetail");
        if(object instanceof Donation){
            donation = (Donation) object;
        }

        dodetailImg = findViewById(R.id.dodetailed_img);
        dodetailName = findViewById(R.id.dodetailed_name);
        dodetailLongImg = findViewById(R.id.dodetailed_img);
        dodetailStart = findViewById(R.id.dodetail_start);
        dodetailEnd = findViewById(R.id.dodetail_end);
        dodetailuPoint = findViewById(R.id.detailU_point);
        dodetatilallPoint = findViewById(R.id.dodetail_point);

        if(donation != null){
            Glide.with(getApplicationContext()).load(donation.getDonationimg()).into(dodetailImg);
            dodetailName.setText(donation.getDonationname());
            dodetatilallPoint.setText(String.valueOf(donation.getPoint()));
            dodetailStart.setText(donation.getDonationstart());
            dodetailEnd.setText(donation.getDonationend());
            Glide.with(getApplicationContext()).load(donation.getDonationdetailimg()).into(dodetailLongImg);
        }


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
                dodetailuPoint.setText(userAccount.getUpoint() + " 씨드");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        doDonate = (Button) findViewById(R.id.doDonate);
        doDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }


}