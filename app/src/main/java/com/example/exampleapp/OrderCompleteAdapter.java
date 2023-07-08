package com.example.exampleapp;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderCompleteAdapter extends RecyclerView.Adapter<OrderCompleteAdapter.OrderCompleteViewHolder>{

    Context context;
    List<MyOrder> myOrderList;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    int totalPrice = 0;

    public OrderCompleteAdapter(Context context, List<MyOrder> myOrderList){
        this.context = context;
        this.myOrderList = myOrderList;
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public OrderCompleteAdapter.OrderCompleteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderCompleteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderCompleteAdapter.OrderCompleteViewHolder holder, int position) {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("CurrentUser");

        Glide.with(holder.itemView)
                .load(myOrderList.get(position).getOrderImg())
                .into(holder.img_order);
        holder.textName_order.setText(myOrderList.get(position).getProductName());
        holder.textPrice_order.setText(String.valueOf(myOrderList.get(position).getTotalPrice()));
        holder.textQauntitiy_order.setText(myOrderList.get(position).getTotalQuantity() + "개");
    }

    @Override
    public int getItemCount() {
        if(myOrderList != null){
            return myOrderList.size();
        }
        return 0;
    }

    public class OrderCompleteViewHolder extends RecyclerView.ViewHolder {
        ImageView img_order;
        TextView textName_order, textPrice_order, textQauntitiy_order;
        public OrderCompleteViewHolder(@NonNull View itemView) {
            super(itemView);

            img_order = itemView.findViewById(R.id.img_order);
            textName_order = itemView.findViewById(R.id.textName_order);
            textPrice_order = itemView.findViewById(R.id.textPrice_order);
            textQauntitiy_order = itemView.findViewById(R.id.textQauntity_order);
        }
    }
}
