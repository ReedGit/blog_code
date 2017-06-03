package me.reed.reyclerviewdemo;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author reed on 2017/6/1
 */

public class MultiAdapter extends RecyclerView.Adapter {

    private static final int TYPE_EMPTY = -1;

    private List<String> data;

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_EMPTY:
                return new EmptyViewHolder(parent);
            default:
                return new DataViewHolder(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DataViewHolder){
            ((DataViewHolder) holder).bindData(data.get(position));
        }
    }

    @Override
    public final int getItemCount() {
        return getCount() == 0 ? 1 : getCount();
    }


    private int getCount(){
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (getCount() == 0){
            return TYPE_EMPTY;
        }
        return super.getItemViewType(position);
    }
}
