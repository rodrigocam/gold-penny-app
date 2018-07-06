package com.code.red.playvendas.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.code.red.playvendas.R;
import com.code.red.playvendas.activities.DisplayProductsActivity;
import com.code.red.playvendas.model.Product;

public class ViewHolder extends RecyclerView.ViewHolder {
    private DisplayProductsActivity displayActivity;
    private TextView nameText;
    private TextView priceText;
    private TextView quantityText;
    private TextView subtotal;
    private Button plus;
    private Button minus;

    private int actualQuantity = 0;

    public ViewHolder(DisplayProductsActivity activity, View itemView) {
        super(itemView);
        this.displayActivity = activity;
        nameText = (TextView) itemView.findViewById(R.id.name);
        quantityText = (TextView) itemView.findViewById(R.id.quantity);
        priceText = (TextView) itemView.findViewById(R.id.price);
        subtotal = (TextView) itemView.findViewById(R.id.subtotal);
        plus = (Button) itemView.findViewById(R.id.plus);
        minus = (Button) itemView.findViewById(R.id.minus);
        actualQuantity = 0;


        nameText.setText("");
        priceText.setText("");
        quantityText.setText("0");
        subtotal.setText("");

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuantity(1);
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateQuantity(-1);
            }
        });

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

    public void bind(Product product) {
        nameText.setText(product.getName());
        priceText.setText("R$" + product.getPrice());
        quantityText.setText(actualQuantity + "");
    }

    public int getActualQuantity() {
        return actualQuantity;
    }
}