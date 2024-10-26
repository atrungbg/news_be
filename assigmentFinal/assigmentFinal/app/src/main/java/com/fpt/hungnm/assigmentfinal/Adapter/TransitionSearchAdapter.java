package com.fpt.hungnm.assigmentfinal.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fpt.hungnm.assigmentfinal.Model.Transaction;
import com.fpt.hungnm.assigmentfinal.R;

import java.util.List;

public class TransitionSearchAdapter extends RecyclerView.Adapter<TransitionSearchAdapter.TransitionSearchViewHolder> {
    public TransitionSearchAdapter(List<Transaction> list) {
        this.list = list;
    }

    private List<Transaction> list;

    @NonNull
    @Override
    public TransitionSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transition,parent,false);
        return new TransitionSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransitionSearchViewHolder holder, int position) {
        Transaction transaction = list.get(position);
        if(transaction == null){
            return;
        }
        Transaction item = list.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.tvTime.setText(String.valueOf(item.getCreateDate()));
        if(item.getIsIncome().equals("INCOME")){
            holder.tvType.setText("Thu Nhập");
            holder.tvPrice.setText("+" + item.getPrice());
            int colorGreen = ContextCompat.getColor(holder.itemView.getContext(), R.color.greenDam);
            holder.tvPrice.setTextColor(colorGreen);
        }else{
            holder.tvType.setText("Chi Tiêu");
            holder.tvPrice.setText("-" + item.getPrice());
            int colorRed = ContextCompat.getColor(holder.itemView.getContext(), R.color.red);
            holder.tvPrice.setTextColor(colorRed);
        }
    }

    @Override
    public int getItemCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    public class TransitionSearchViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvType;
        private TextView tvTime;
        private TextView tvPrice;
        public TransitionSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingView();
        }
        private void bindingView() {
            tvType = itemView.findViewById(R.id.rcv_transition_type);
            tvTitle = itemView.findViewById(R.id.rcv_transition_title);
            tvTime = itemView.findViewById(R.id.rcv_transition_time);
            tvPrice = itemView.findViewById(R.id.rcv_transition_price);
        }
    }
}
