package com.code.red.playvendas.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.code.red.playvendas.R;
import com.code.red.playvendas.model.Product;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private LayoutInflater _inflater;
    private Product[] _products;

    public ProductListAdapter(Context context, Product... products){
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _products = products;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameText;
        TextView priceText;
        TextView quantityText;
        Button plus;
        Button minus;
        int actualQuantity = 0;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void modifyText(int quantity){
            actualQuantity += quantity;
            quantityText.setText(actualQuantity + "");
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder;

        View list_item = _inflater.inflate(R.layout.list_item, parent, false);

        holder = new ViewHolder(list_item);
        holder.nameText = (TextView) list_item.findViewById(R.id.name);
        holder.quantityText = (TextView) list_item.findViewById(R.id.quantity);
        holder.priceText = (TextView) list_item.findViewById(R.id.price);
        holder.plus = (Button) list_item.findViewById(R.id.plus);
        holder.minus = (Button) list_item.findViewById(R.id.minus);
        holder.actualQuantity = 0;

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.modifyText(1);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.modifyText(-1);
            }
        });

        holder.nameText.setText("");
        holder.priceText.setText("");
        holder.quantityText.setText("0");

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.nameText.setText(_products[position].getName());
        holder.priceText.setText(_products[position].getPrice() + "");
        holder.quantityText.setText(holder.actualQuantity + "");

    }

    @Override
    public int getItemCount() {
        return _products.length;
    }
}

