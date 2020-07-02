package com.example.inventorymanager;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AllFragment  extends Fragment implements ListAdapter.OnListItemLongSelectedInterface, ListAdapter.OnListItemSelectedInterface {
    private Context context;  // context
    private RecyclerView recyclerView;  // recyclerView to show all product of the company
    private RecyclerView.Adapter adapter; // recyclerView's adapter
    private RecyclerView.LayoutManager layoutManger;
    private ArrayList<Prod> arrayList;  // arrayList to store the company's products
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(); // database from firebase
    private RecyclerView.LayoutManager mLayoutManager; // layout manager for recyclerView

    //Fragment manager and transaction for changing fragment
    FragmentManager fmanager;
    FragmentTransaction ftrans;

    ProdInfoFragment infoFrag; //Product information fragment
    MenuFragment mainFrag; // main menu fragment
    String comp; // company name(business name)

    Button backBtn; // Back button
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_all, container, false);
        //set fragment manager and transaction
        fmanager = getFragmentManager();
        ftrans = fmanager.beginTransaction();
        //create new fragment objects to change
        infoFrag = new ProdInfoFragment();
        mainFrag = new MenuFragment();
        //find recyclerView
        recyclerView = rootView.findViewById(R.id.recyclerView);

        arrayList = new ArrayList<Prod>();

        context = rootView.getContext();
        //get company name from bundle
        Bundle bundle = this.getArguments();
        comp = bundle.getString("comp");
        //When back button is pressed, move to main fragment
        backBtn = rootView.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("comp",comp);
                mainFrag.setArguments(bundle);
                ftrans.replace(R.id.container, mainFrag).commit();
            }
        });
        //read data from fire base(mDatabase) and find the company's products and store the products to arrayList
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for(DataSnapshot st : snapshot.getChildren()){
//                        Log.d("checkId",st.getValue().toString());
                        Prod prod = st.getValue(Prod.class);
                        if(prod.productName != null)
                            if(prod.business.equals(comp))
                                arrayList.add(prod);
                    }
                    /**/
                }
                //check
                Log.d("checkId",Integer.toString(arrayList.size()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
        //set Layout manager
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        //set adapter with array list
        adapter = new ListAdapter(rootView.getContext(),arrayList,this,this);
        //set adapter to recyclerView
        recyclerView.setAdapter(adapter);

        return rootView;
    }
    //If a product selected, create a bundle and put the product information to the bundle
    //send the bundle to infoFragment(which shows product information) and change the fragment to infoFragment
    @Override
    public void onItemSelected(View v, int position) {
        ListAdapter.ViewHolder viewHolder = (ListAdapter.ViewHolder)recyclerView.findViewHolderForAdapterPosition(position);
        Toast.makeText(getActivity(), viewHolder.prodName.getText().toString(), Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putString("pd_name",viewHolder.prod.productName);
        bundle.putString("pd_num",viewHolder.prod.productCode);
        bundle.putString("pd_comp",viewHolder.prod.business);
        bundle.putInt("pd_cnt",viewHolder.prod.stock);
        bundle.putString("pd_size",viewHolder.prod.size);
        bundle.putString("pd_color",viewHolder.prod.color);
        bundle.putString("back","all");
        infoFrag.setArguments(bundle);
        ftrans.replace(R.id.container, infoFrag).commit();
    }

    @Override
    public void onItemLongSelected(View v, int position) {

    }
}

