package com.fpt.hungnm.assigmentfinal.Adapter;

import android.content.ClipData;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.fpt.hungnm.assigmentfinal.MainActivity;
import com.fpt.hungnm.assigmentfinal.Model.Transaction;
import com.fpt.hungnm.assigmentfinal.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransitionRecyclerViewAdapter extends RecyclerView.Adapter<TransitionRecyclerViewAdapter.TransitionViewHolder>{

    private static final String TAG ="HungnmError";
    private List<Transaction> list;
    private TransitionListener transitionListener;

    public void setTransitionListener(TransitionListener transitionListener) {
        this.transitionListener = transitionListener;
    }

    private OnLongClickTransactionListener onLongClickTransactionListener;
    private static final String INCOME = "INCOME";

    public TransitionRecyclerViewAdapter() {
        list = new ArrayList<>();
    }

    public void setList(List<Transaction> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public Transaction getTransition(int position){
        return list.get(position);
    }

    @NonNull
    @Override
    public TransitionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transition,parent,false);
        return new TransitionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransitionViewHolder holder, int position) {
        try{
            Transaction item = list.get(position);
            holder.tvTitle.setText(item.getTitle());
            holder.tvTime.setText(String.valueOf(item.getCreateDate()));
            if(item.getIsIncome().equals(INCOME)){
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

        }catch (Exception ex){
            Log.e(TAG, "TransitionRecyclerViewAdapter - onBindViewHolder - " + ex.getMessage());
        }
    }

    public void setOnLongClickTransactionListener(OnLongClickTransactionListener onLongClickTransactionListener) {
        this.onLongClickTransactionListener = onLongClickTransactionListener;
    }

    public interface TransitionListener{
        void onItemClick(View view, int position);
    }
    public interface OnLongClickTransactionListener {
        void onLongClick(View view, int position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TransitionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        private TextView tvTitle;
        private TextView tvType;
        private TextView tvTime;
        private TextView tvPrice;
        public TransitionViewHolder(@NonNull View itemView) {
            super(itemView);
            bindingView();
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        private void bindingView() {
            tvType = itemView.findViewById(R.id.rcv_transition_type);
            tvTitle = itemView.findViewById(R.id.rcv_transition_title);
            tvTime = itemView.findViewById(R.id.rcv_transition_time);
            tvPrice = itemView.findViewById(R.id.rcv_transition_price);
        }

        @Override
        public void onClick(View v) {
            if(transitionListener != null){
                transitionListener.onItemClick(itemView, getAdapterPosition());
            }
        }


        @Override
        public boolean onLongClick(View v) {
            if(onLongClickTransactionListener != null){
                onLongClickTransactionListener.onLongClick(itemView, getAdapterPosition());
                return true;
            }
            return false;
        }
    }
}

