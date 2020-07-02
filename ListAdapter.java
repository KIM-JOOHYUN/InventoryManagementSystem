package com.example.inventorymanager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
//adapter to control recycler view
public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;
    private OnListItemLongSelectedInterface mLongListener;

    public ArrayList<Prod> arrayList; //product list which is shown to the recycler view
    private Context mContext;

    //View holder to set each item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView prodName;
        public Prod prod;
        public ViewHolder(View view) {
            super(view);
            prodName = (TextView) view.findViewById(R.id.item);
        }
    }
    //constructor
    public ListAdapter(Context context, ArrayList<Prod> arrayList, OnListItemSelectedInterface listener
            , OnListItemLongSelectedInterface longListener){
        this.mContext = context;
        this.arrayList = arrayList;
        this.mListener = listener;
        this.mLongListener = longListener;

    }
    //set recyclerView
    @NotNull
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    // if an item is selected, run the listener with the selected item's position
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
