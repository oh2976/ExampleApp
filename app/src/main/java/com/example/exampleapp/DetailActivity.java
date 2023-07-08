package com.example.exampleapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {

    TextView quantity;
    int totalQuantity = 1;
    int totalPrice = 0;

    ImageView detailedImg;
    ImageView detailedLongImg;
    TextView price, description, stock, name;
    Button addToCart;
    ImageView addItem, removeItem;

    Product product = null;

    int pid = 0;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼, 디폴트로 true만 해도 백버튼이 생김

        database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("CurrentUser");
        String cartID = databaseReference.push().getKey();

        auth = FirebaseAuth.getInstance();
        //상품 리스트에서 상품 상세 페이지로 데이터 가져오기
        final Object object = getIntent().getSerializableExtra("detail");
        if(object instanceof Product){
            product = (Product) object;
        }

        quantity = findViewById(R.id.quantity);


        detailedImg = findViewById(R.id.detailed_img);
        addItem = findViewById(R.id.add_item);
        removeItem = findViewById(R.id.remove_item);
        detailedLongImg = findViewById(R.id.detail_longimg);


        price = findViewById(R.id.detail_price);
        description = findViewById(R.id.detailed_disc);
        stock = findViewById(R.id.detail_stock);

        name = findViewById(R.id.detailed_name);

        if (product != null) {
            Glide.with(getApplicationContext()).load(product.getImg()).into(detailedImg);
            description.setText(product.getDescription());
            price.setText(String.valueOf(product.getPrice()));
            stock.setText("재고: " + String.valueOf(product.getStock()));
            name.setText(product.getName());
            Glide.with(getApplicationContext()).load(product.getLongimg()).into(detailedLongImg);

            totalPrice= product.getPrice() * totalQuantity;


//            pid = product.getpId();
//            Toast.makeText(DetailActivity.this, String.valueOf(pid), Toast.LENGTH_SHORT).show();


        }


        addToCart = findViewById(R.id.add_to_cart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                addedToCart();
                final HashMap<String, Object> cartMap = new HashMap<>();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                cartMap.put("productName", product.getName());
                cartMap.put("productPrice", price.getText().toString());
                cartMap.put("totalQuantity", quantity.getText().toString());
                cartMap.put("totalPrice", totalPrice * totalQuantity);
                cartMap.put("pId", product.getpId());
                cartMap.put("productImg", product.getImg());
                cartMap.put("productStock", product.getStock());
//                cartMap.put("dataId", "");
                Log.d("DetailActivity", product.getpId()+"");

                databaseReference.child(firebaseUser.getUid()).child("AddToCart").child(cartID).setValue(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DetailActivity.this, "Add To A Cart", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DetailActivity.this, CartActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });




//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(DetailActivity.this, "Add To A Cart", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(DetailActivity.this, CartActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });

            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity < 10){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });
        removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(totalQuantity > 0){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }
            }
        });
    }

//    private void addedToCart() {
//    }

}