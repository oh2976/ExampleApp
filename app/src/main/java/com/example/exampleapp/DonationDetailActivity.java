package com.example.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DonationDetailActivity extends AppCompatActivity {

    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    ImageView dodetailImg;
    ImageView dodetailLongImg;
    TextView dodetailName, dodetailStart, dodetailEnd, dodetailuPoint, dodetatilallPoint;

    Button doDonate;
    BottomSheetDialog dialog;

    Donation donation = null;

    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
    private DatabaseReference databaseReference4;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private Point point;

    private int spoint = 0;
    private int upoint = 0;
    private int allDoPoint = 0;

    private int donationId = 0;

    private String donationName = "";

    private String userAccountName = "";


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
            donationId = donation.getDonationid();
            allDoPoint = donation.getPoint();
            donationName = donation.getDonationname();
        }


        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Point");

        databaseReference2 = FirebaseDatabase.getInstance().getReference("UserAccount");

        databaseReference3 = FirebaseDatabase.getInstance().getReference("Donation");

        databaseReference4 = FirebaseDatabase.getInstance().getReference("CurrentUser");



        databaseReference2.child("UserAccount").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
//                arrayList.clear(); //기존 배열 리스트가 존재하지 않게 남아 있는 데이터 초기화
                // 반복문으로 데이터 List를 추출해냄

                UserAccount userAccount = dataSnapshot.getValue(UserAccount.class); //  만들어 뒀던 Product 객체에 데이터를 담는다.
                dodetailuPoint.setText(userAccount.getSpoint() + " 씨드");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        doDonate = (Button) findViewById(R.id.doDonate);

        dialog = new BottomSheetDialog(this);
        //inflate view
//        createDialog();

        View view = getLayoutInflater().inflate(R.layout.bottom_dialog, null, false);

        Button wannaDonate = view.findViewById(R.id.wannaDonate);
        EditText wannaDonatepoint = view.findViewById(R.id.wannaDonatepoint);

        String pointID = databaseReference4.push().getKey();

        wannaDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( Integer.parseInt(wannaDonatepoint.getText().toString()) >= 200 && Integer.parseInt(wannaDonatepoint.getText().toString()) % 10 == 0 ){
                    databaseReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                            // arrayList.clear(); //기존 배열 리스트가 존재하지 않게 남아 있는 데이터 초기화
                            // 반복문으로 데이터 List를 추출해냄
                            Point point = dataSnapshot.getValue(Point.class);//  만들어 뒀던 Point 객체에 데이터를 담는다.
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            spoint = point.getSpoint();
                            spoint = spoint - Integer.parseInt(wannaDonatepoint.getText().toString());
                            upoint = point.getUpoint();
                            upoint = upoint + Integer.parseInt(wannaDonatepoint.getText().toString());
//                            Toast.makeText(DonationDetailActivity.this, String.valueOf(upoint) , Toast.LENGTH_LONG).show();

                            allDoPoint = allDoPoint + Integer.parseInt(wannaDonatepoint.getText().toString());


                            databaseReference3.child(String.valueOf(donationId)).child("point").setValue(allDoPoint).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//                                    Toast.makeText(DonationDetailActivity.this, "allDoPoint 변동 완료", Toast.LENGTH_SHORT).show();
                                }
                            });


                            databaseReference2.child("UserAccount").child(firebaseUser.getUid()).child("spoint").setValue(spoint).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//                                    Toast.makeText(DonationDetailActivity.this, "spoint 변동 완료", Toast.LENGTH_SHORT).show();
                                }
                            });

                            databaseReference2.child("UserAccount").child(firebaseUser.getUid()).child("upoint").setValue(upoint).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//                                    Toast.makeText(DonationDetailActivity.this, "upoint 변동 완료", Toast.LENGTH_LONG).show();
                                }
                            });

                            databaseReference.child(firebaseUser.getUid())
                                    .removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(DonationDetailActivity.this, "임시 Point 테이블 삭제 완료", Toast.LENGTH_SHORT).show();


                                        }
                                    });

                            databaseReference2.child("UserAccount").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
//                arrayList.clear(); //기존 배열 리스트가 존재하지 않게 남아 있는 데이터 초기화
                                    // 반복문으로 데이터 List를 추출해냄

                                    UserAccount userAccount = dataSnapshot.getValue(UserAccount.class); //  만들어 뒀던 Product 객체에 데이터를 담는다.
                                    final HashMap<String, Object> donateMap = new HashMap<>();
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    donateMap.put("userName", userAccount.getUsername());
                                    donateMap.put("donationName", donationName);
                                    donateMap.put("donationPoint", Integer.parseInt(wannaDonatepoint.getText().toString()));
                                    donateMap.put("donationDate", getTime());
                                    userAccountName = userAccount.getUsername();


                                    databaseReference4.child(firebaseUser.getUid()).child("MyPoint").child(pointID).setValue(donateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(DonationDetailActivity.this, "point table create", Toast.LENGTH_SHORT).show();
                                            dialog.show();
                                        }
                                    });

                                    Intent intent = new Intent(DonationDetailActivity.this, DonationCompleteActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("userName", userAccountName);
                                    bundle.putString("donationName", donationName);
                                    bundle.putInt("donationPoint", Integer.parseInt(wannaDonatepoint.getText().toString()));
                                    bundle.putString("donationDate", getTime());

                                    intent.putExtras(bundle);
                                    startActivity(intent);



                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    dialog.dismiss();
                    Toast.makeText(DonationDetailActivity.this, wannaDonatepoint.getText(), Toast.LENGTH_SHORT).show();
                    Log.d("DonationDetailActivity",databaseReference2.child("UserAccount").child(firebaseUser.getUid()).child("spoint").toString());
                }

            }
        });
        dialog.setContentView(view);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                databaseReference.child(firebaseUser.getUid())
                        .removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(DonationDetailActivity.this, "임시 Point 테이블 삭제 완료", Toast.LENGTH_SHORT).show();


                            }
                        });

            }
        });

        doDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference2.child("UserAccount").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
//                arrayList.clear(); //기존 배열 리스트가 존재하지 않게 남아 있는 데이터 초기화
                        // 반복문으로 데이터 List를 추출해냄

                        UserAccount userAccount = dataSnapshot.getValue(UserAccount.class); //  만들어 뒀던 Product 객체에 데이터를 담는다.
                        final HashMap<String, Object> donateMap = new HashMap<>();
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        donateMap.put("spoint", userAccount.getSpoint());
                        donateMap.put("upoint", userAccount.getUpoint());
                        donateMap.put("username", userAccount.getUsername());

                        databaseReference.child(firebaseUser.getUid()).setValue(donateMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(DonationDetailActivity.this, "point table create", Toast.LENGTH_SHORT).show();
                                dialog.show();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


    }

//    private void createDialog() {
//        View view = getLayoutInflater().inflate(R.layout.bottom_dialog, null, false);
//
//        Button wannaDonate = view.findViewById(R.id.wannaDonate);
//        EditText wannaDonatepoint = view.findViewById(R.id.wannaDonatepoint);
//
//        wannaDonate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if( Integer.parseInt(wannaDonatepoint.getText().toString()) >= 200 && Integer.parseInt(wannaDonatepoint.getText().toString()) % 10 == 0 ){
//
//                    dialog.dismiss();
//                    Toast.makeText(DonationDetailActivity.this, wannaDonatepoint.getText(), Toast.LENGTH_SHORT).show();
//
//                }
//
//            }
//        });
//        dialog.setContentView(view);
//
//    }

    private String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }


}