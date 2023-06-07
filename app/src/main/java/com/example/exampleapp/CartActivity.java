package com.example.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    private RecyclerView.LayoutManager layoutManager;


    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private List<Cart> cartList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        recyclerView = findViewById(R.id.recyclerview_cart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        firebaseAuth = FirebaseAuth.getInstance();

        Button butBtn = (Button) findViewById(R.id.buy_now);



        firebaseDatabase = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동

        cartList = new ArrayList<>();

//        cartList = new ArrayList<>();
//        cartAdapter = new CartAdapter(this, cartList);
//        recyclerView.setAdapter(cartAdapter);

        firebaseDatabase.getReference("CurrentUser").child(firebaseAuth.getCurrentUser().getUid()).child("AddToCart").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot dataSnapshot : task.getResult().getChildren()) {
                        String dataId = dataSnapshot.getKey();



                        Cart cart = dataSnapshot.getValue(Cart.class);
                        cart.setDataId(dataId);


                        cartList.add(cart);
                        cartAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        cartAdapter = new CartAdapter(this, cartList);
        recyclerView.setAdapter(cartAdapter); //리사이클러뷰에 어댑터 연결

        butBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CartActivity.this, "버튼 누름", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                    intent.putExtra("itemList", (Serializable) cartList);
                    startActivity(intent);
                }

        });

    }


}