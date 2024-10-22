package com.fpt.hungnm.assigmentfinal.Adapter;

import android.content.ClipData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fpt.hungnm.assigmentfinal.Model.Category;
import com.fpt.hungnm.assigmentfinal.R;

import java.util.ArrayList;
import java.util.List;

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.CategoryViewHolder>{

    private static final String TAG ="HungnmError";
    private List<Category> list;
    private ItemListener itemListener;

    public void setItemListener(ItemListener itemListener) {
        this.itemListener = itemListener;
    }

    private static final String INCOME = "INCOME";

    public RecylerViewAdapter() {
        list = new ArrayList<>();
    }

    public void setList(List<Category> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public Category getCategory(int position){
        return list.get(position);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category,parent,false);
            return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        try{
            Category item = list.get(position);
            holder.tvTitle.setText(item.getTitle());
            if(item.getIsIncome().equals(INCOME)){
                holder.tvType.setText("Thu Nhập");
            }else{
                holder.tvType.setText("Chi Tiêu");
            }
        }catch (Exception ex){
            Log.e(TAG, "RecyclerViewAdapter - onBindViewHolder - " + ex.getMessage());
        }
    }

    public interface ItemListener{
        void onItemClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvTitle;
        private TextView tvType;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingView();
            itemView.setOnClickListener(this);
        }

        private void bindingView() {
            tvType = itemView.findViewById(R.id.tv_category_rcv_type);
            tvTitle = itemView.findViewById(R.id.tv_category_rcv_title);
        }

        @Override
        public void onClick(View v) {
            if(itemListener != null){
                itemListener.onItemClick(itemView, getAdapterPosition());
            }
        }
    }
}
