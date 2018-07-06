package com.code.red.playvendas.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.code.red.playvendas.R;
import com.code.red.playvendas.model.Product;
import com.code.red.playvendas.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private LayoutInflater _inflater;
    private ArrayList<Product> _products;
    private DisplayProductsActivity activity;

    public ProductListAdapter(DisplayProductsActivity activity, List<Product> products) {
        _inflater = (LayoutInflater) activity.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _products = new ArrayList<Product>(products);
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View list_item = _inflater.inflate(R.layout.list_item, parent, false);

        ViewHolder holder = new ViewHolder(activity, list_item);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(_products.get(position));
    }

    @Override
    public int getItemCount() {
        return _products.size();
    }

    public Product getProduct(int position, RecyclerView productList) {
        View child = productList.getChildAt(position);
        ViewHolder productViewHolder = (ViewHolder) productList.getChildViewHolder(child);
        _products.get(position).setQuantity(productViewHolder.getActualQuantity());
        return _products.get(position);
    }
}

