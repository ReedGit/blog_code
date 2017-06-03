package me.reed.reyclerviewdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author reed on 2017/6/1
 */

public class DataViewHolder extends RecyclerView.ViewHolder {

    private TextView text;

    public DataViewHolder(ViewGroup parent){
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false));
    }

    private DataViewHolder(View itemView) {
        super(itemView);
        text = (TextView) itemView.findViewById(R.id.text);
    }

    public void bindData(String data){
        text.setText(data);
    }
}
