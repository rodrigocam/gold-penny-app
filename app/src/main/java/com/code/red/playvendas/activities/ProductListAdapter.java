package com.code.red.playvendas.activities;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.code.red.playvendas.R;
import com.code.red.playvendas.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private LayoutInflater _inflater;
    private ArrayList<Product> _products;
    private DisplayProductsActivity activity;

    public ProductListAdapter(DisplayProductsActivity activity,Context context, List<Product> products) {
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _products = new ArrayList<Product>(products);
        Log.d("ProductListAdapter", products.toString());
        this.activity = activity;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        DisplayProductsActivity displayActivity;
        TextView nameText;
        TextView priceText;
        TextView quantityText;
        TextView subtotal;
        Button plus;
        Button minus;
        int actualQuantity = 0;

        public ViewHolder(DisplayProductsActivity activity,View itemView) {
            super(itemView);
            this.displayActivity = activity;
        }

        public void updateQuantity(int quantity) {
            actualQuantity += quantity;
            if (actualQuantity < 0) {
                actualQuantity -= quantity;
            } else {
                quantityText.setText(actualQuantity + "");
                updateSubtotal();
            }
        }

        private void updateSubtotal() {
            Double price = Double.parseDouble(priceText.getText().toString().replace("R$", ""));
            price = price * actualQuantity;
            if (price != 0) {
                subtotal.setText(price + "");
            } else {
                subtotal.setText("");
            }
            displayActivity.updateTotalPrice();
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder;

        View list_item = _inflater.inflate(R.layout.list_item, parent, false);

        holder = new ViewHolder(activity,list_item);
        holder.nameText = (TextView) list_item.findViewById(R.id.name);
        holder.quantityText = (TextView) list_item.findViewById(R.id.quantity);
        holder.priceText = (TextView) list_item.findViewById(R.id.price);
        holder.subtotal = (TextView) list_item.findViewById(R.id.subtotal);
        holder.plus = (Button) list_item.findViewById(R.id.plus);
        holder.minus = (Button) list_item.findViewById(R.id.minus);
        holder.actualQuantity = 0;

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.updateQuantity(1);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.updateQuantity(-1);
            }
        });

        holder.nameText.setText("");
        holder.priceText.setText("");
        holder.quantityText.setText("0");
        holder.subtotal.setText("");

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.nameText.setText(_products.get(position).getName());
        holder.priceText.setText("R$"+_products.get(position).getPrice());
        holder.quantityText.setText(holder.actualQuantity + "");
    }

    @Override
    public int getItemCount() {
        return _products.size();
    }

    public Product getProduct(int position, RecyclerView productList){
            View child = productList.getChildAt(position);
            ViewHolder productViewHolder = (ViewHolder) productList.getChildViewHolder(child);
            _products.get(position).setQuantity(productViewHolder.actualQuantity);
            return _products.get(position);
    }
}

