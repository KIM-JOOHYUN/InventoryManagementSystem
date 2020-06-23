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
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment implements ListAdapter.OnListItemLongSelectedInterface, ListAdapter.OnListItemSelectedInterface {
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManger;
    private ArrayList<Prod> arrayList;
    private ArrayList<Prod> backupList;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    private RecyclerView.LayoutManager mLayoutManager;

    FragmentManager fmanager;
    FragmentTransaction ftrans;

    ProdInfoFragment infoFrag;
    MenuFragment menuFrag;
    String comp;

    ImageButton searchBtn;
    ImageButton backBtn;

    RadioGroup searchType;
    RadioButton sName;
    RadioButton sNum;

    EditText searchValue;
    String value = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_search, container, false);
        fmanager = getFragmentManager();
        ftrans = fmanager.beginTransaction();
        infoFrag = new ProdInfoFragment();
        menuFrag = new MenuFragment();
        searchValue = rootView.findViewById(R.id.searchText);


        arrayList = new ArrayList<Prod>();
        backupList = new ArrayList<Prod>();

        context = rootView.getContext();

        Bundle bundle = this.getArguments();
        comp = bundle.getString("comp");

        searchType = rootView.findViewById(R.id.searchType);

        sName = rootView.findViewById(R.id.sName);
        sNum = rootView.findViewById(R.id.sNum);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for(DataSnapshot st : snapshot.getChildren()){
//                        Log.d("checkId",st.getValue().toString());
                        Prod prod = st.getValue(Prod.class);
                        if(prod.productName != null)
                            if(prod.business.equals(comp)){
                                arrayList.add(prod);
                                backupList.add(prod);
                            }
                    }
                    /**/
                }
                Log.d("checkId",Integer.toString(arrayList.size()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);

        adapter = new ListAdapter(rootView.getContext(),arrayList,this,this);
        recyclerView.setAdapter(adapter);


        searchBtn = rootView.findViewById(R.id.search_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.clear();
                arrayList.addAll(backupList);
                int radioId = searchType.getCheckedRadioButtonId();
                value = searchValue.getText().toString();
                if(value.length() > 0){
                    if(radioId == sName.getId()){
                        final int size = arrayList.size();
                        for(int i = size - 1; i>= 0; i--) {
                            Log.d("checkName",arrayList.get(i).productName + "  "+value);
                            if (!arrayList.get(i).productName.contains(value)) {
                                arrayList.remove(i);
                                adapter.notifyItemRemoved(i);
                            }
                        }
                    }else if(radioId == sNum.getId()){
                        final int size = arrayList.size();
                        for(int i = size - 1; i>= 0; i--) {
                            Log.d("checkNum",arrayList.get(i).productCode + "  "+value);
                            if (!arrayList.get(i).productCode.contains(value)) {
                                arrayList.remove(i);
                                adapter.notifyItemRemoved(i);
                            }
                        }
                    }
                }

                adapter.notifyDataSetChanged();
                Log.d("checkData",arrayList.toString());
                Log.d("checkData",backupList.toString());
            }
        });

        backBtn = rootView.findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle sendbundle = new Bundle();
                sendbundle.putString("comp",comp);
                menuFrag.setArguments(sendbundle);
                ftrans.replace(R.id.container, menuFrag).commit();
            }
        });

        return rootView;
    }
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
        bundle.putString("back","search");
        infoFrag.setArguments(bundle);
        ftrans.replace(R.id.container, infoFrag).commit();
    }

    @Override
    public void onItemLongSelected(View v, int position) {

    }

}
