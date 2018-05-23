package com.code.red.playvendas.activities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.code.red.playvendas.R;
import com.code.red.playvendas.model.Product;

public class ProductListAdapter extends BaseAdapter {
    private LayoutInflater _inflater;
    private Product[] _products;
    public ProductListAdapter(Context context, Product... products){
        _inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _products = products;
    }

    @Override
    public int getCount() {
        return _products.length;
    }

    @Override
    public Object getItem(int position) {
        return _products[position];
    }

    @Override
    public long getItemId(int position) {
        return position + 1;
    }

    static class ViewHolder {
        TextView nameText;
        TextView priceText;
        TextView quantityText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = _inflater.inflate(R.layout.list_item, null);

            holder = new ViewHolder();
            holder.nameText = (TextView) convertView.findViewById(R.id.name);
            holder.quantityText = (TextView) convertView.findViewById(R.id.quantity);
            holder.priceText = (TextView) convertView.findViewById(R.id.price);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.nameText.setText(_products[position].getName());
        holder.priceText.setText(_products[position].getPrice() + "");
        holder.quantityText.setText("0");

        return convertView;
    }
}

