package me.reed.reyclerviewdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author reed on 2017/6/1
 */

public class EmptyViewHolder extends RecyclerView.ViewHolder {

    public EmptyViewHolder(ViewGroup parent){
        this(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty, parent, false));
    }

    private EmptyViewHolder(View itemView) {
        super(itemView);
    }
}
