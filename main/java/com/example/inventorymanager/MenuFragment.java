package com.example.inventorymanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MenuFragment extends Fragment {
    FragmentManager fmanager;
    FragmentTransaction ftrans;
    String comp;

    Button allBtn; // see all product
    Button sBtn;  // search product(by product name / number)
    Button bBtn;  // search product(by barcode scanning)

    AllFragment allFrag = new AllFragment();
    SearchFragment searchFrag = new SearchFragment();
    BarcodeFragment barcodeFrag = new BarcodeFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_menu, container, false);
        fmanager = getFragmentManager();
        ftrans = fmanager.beginTransaction();
        Bundle getBundle = this.getArguments();
        comp = getBundle.getString("comp","");

        allBtn = rootview.findViewById(R.id.all_btn);
        sBtn = rootview.findViewById(R.id.search_btn);
        bBtn = rootview.findViewById(R.id.barcode_btn);

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("comp",comp);
                allFrag.setArguments(bundle);
                ftrans.replace(R.id.container, allFrag).commit();
            }
        });

        sBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("comp",comp);
                searchFrag.setArguments(bundle);
                ftrans.replace(R.id.container, searchFrag).commit();
            }
        });

        bBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("comp",comp);
                barcodeFrag.setArguments(bundle);
                ftrans.replace(R.id.container, barcodeFrag).commit();
            }
        });


        // Inflate the layout for this fragment
        return rootview;
    }
}