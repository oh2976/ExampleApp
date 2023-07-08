package com.example.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonationCompleteActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private TextView completeuPoint, completement, completeDoName, completeDoDate, completeDoPoint;

    private String donationName;
    private String donationDate;
    private String userName;
    private int donationPoint;
    private Button goToMain;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_complete);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("UserAccount");

        completeuPoint = findViewById(R.id.completeU_point);
        completeDoName = findViewById(R.id.completeDoName);
        completeDoDate = findViewById(R.id.completeDoDate);
        completeDoPoint = findViewById(R.id.completeDoPoint);
        completement = (TextView) findViewById(R.id.completement);

        databaseReference.child("UserAccount").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
//                arrayList.clear(); //기존 배열 리스트가 존재하지 않게 남아 있는 데이터 초기화
                // 반복문으로 데이터 List를 추출해냄

                UserAccount userAccount = dataSnapshot.getValue(UserAccount.class); //  만들어 뒀던 Product 객체에 데이터를 담는다.
                completeuPoint.setText(userAccount.getSpoint() + " 씨드");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            userName = bundle.getString("userName");
            donationName = bundle.getString("donationName");
            donationPoint = bundle.getInt("donationPoint");
            donationDate = bundle.getString("donationDate");

            Log.d("DonationCompleteActivity", userName + donationName + donationPoint + donationDate);

            Toast.makeText(DonationCompleteActivity.this, userName + donationName + donationPoint + donationDate, Toast.LENGTH_SHORT).show();

        }

        completement.setText("총 " + String.valueOf(donationPoint) + " 씨드로");

        completeDoName.setText(donationName);
        completeDoDate.setText(donationDate);
        completeDoPoint.setText(String.valueOf(donationPoint));

        goToMain = (Button) findViewById(R.id.goToMain);
        goToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DonationCompleteActivity.this, MainActivity3.class);
                startActivity(intent);
            }
        });


    }
}