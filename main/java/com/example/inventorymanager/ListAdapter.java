package com.example.inventorymanager;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;
    private OnListItemLongSelectedInterface mLongListener;


    public ArrayList<Prod> arrayList; //아까 만든 class에서의 User
    private Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView prodName;
        public Prod prod;

        public ViewHolder(View view) {
            super(view);
            prodName = (TextView) view.findViewById(R.id.item);
        }
    }

    public ListAdapter(Context context, ArrayList<Prod> arrayList, OnListItemSelectedInterface listener
            , OnListItemLongSelectedInterface longListener){
        this.mContext = context;
        this.arrayList = arrayList;
        this.mListener = listener;
        this.mLongListener = longListener;

    }

    @NotNull
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.prodName.setText(arrayList.get(position).productCode);
        holder.prod = arrayList.get(position);
        holder.prodName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemSelected(v, position);
            }
        });
    }



    public int getItemCount(){
        return arrayList.size();
    }

}
