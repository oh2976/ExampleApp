package com.example.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

public class OrderCompleteActivity extends AppCompatActivity {
    private String orderId, myOrderId;
    private TextView cmpOrderId, cmpOrderDate, cmp_totalPrice, cmp_name, cmp_phone, cmp_address;

    private Button btnGoMain;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    private OrderCompleteAdapter orderCompleteAdapter;
    private List<MyOrder> myOrderList;
//    private ArrayList<MyOrder> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);

        databaseReference = FirebaseDatabase.getInstance().getReference("CurrentUser");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");
        myOrderId = intent.getStringExtra("myOrderId");
//        Toast.makeText(OrderCompleteActivity.this, orderId, Toast.LENGTH_SHORT).show();
        Log.d("OrderCompleteActivity", orderId);
//                    Toast.makeText(OrderCompleteActivity.this, myOrder+"데이터베이스 토스 성공", Toast.LENGTH_SHORT).show();

        recyclerView = findViewById(R.id.recyclerView_orderComplete);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        myOrderList = new ArrayList<>();

        cmpOrderId = findViewById(R.id.cmpOrderId);
        cmpOrderDate = findViewById(R.id.cmpOrderDate);
        cmp_totalPrice = findViewById(R.id.cmp_totalPrice);
        cmp_name = findViewById(R.id.cmp_name);
        cmp_phone = findViewById(R.id.cmp_phone);
        cmp_address = findViewById(R.id.cmp_address);

        myOrderList = new ArrayList<>();


        databaseReference.child(firebaseUser.getUid()).child("MyOrder").child(myOrderId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        MyOrder myOrder = dataSnapshot.getValue(MyOrder.class);

                        myOrderList.add(myOrder);
                        orderCompleteAdapter.notifyDataSetChanged();

                        cmpOrderId.setText(myOrder.getOrderId()+"");
                        cmpOrderDate.setText(myOrder.getOrderDate());
                        cmp_totalPrice.setText(String.valueOf(myOrder.getOverTotalPrice()));
                        cmp_name.setText(myOrder.getUserName());
                        cmp_phone.setText(myOrder.getPhone());
                        cmp_address.setText(myOrder.getAddress());
                    }

                }
            }
        });

//        List<MyOrder> list = (ArrayList<MyOrder>) getIntent().getSerializableExtra("orderList");
//        Log.d("OrderCompleteActivity", "list : " + list);
//        databaseReference.child(firebaseUser.getUid()).child("MyOrder").child(myOrderId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
////                myOrderList.clear();
//
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//
//                    MyOrder myOrder = dataSnapshot.getValue(MyOrder.class);
//                    myOrderList.add(myOrder);
//

//
//                }
//                orderCompleteAdapter.notifyDataSetChanged();
//
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // 디비를 가져오던 중 에러 발생 시
//                Log.e("OrderCompleteActivity", String.valueOf(databaseError.toException())); // 에러문 출력
//            }
//        });

        orderCompleteAdapter = new OrderCompleteAdapter(this, myOrderList);
        recyclerView.setAdapter(orderCompleteAdapter);

        btnGoMain = (Button) findViewById(R.id.btnGoMain);

        btnGoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderCompleteActivity.this, MainActivity3.class);
                startActivity(intent);
            }
        });



    }
}