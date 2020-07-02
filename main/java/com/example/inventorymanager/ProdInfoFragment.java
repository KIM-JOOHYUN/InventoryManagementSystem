package com.example.inventorymanager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class ProdInfoFragment extends Fragment {
    TextView prod_name;
    TextView prod_num;
    TextView prod_cnt;
    TextView prod_comp;
    TextView prod_size;
    TextView prod_color;

    String pd_name;
    String pd_comp;
    String pd_num;
    String pd_size;
    String pd_color;
    int pd_cnt;

    Button backBtn;

    FragmentManager fmanager;
    FragmentTransaction ftrans;

    String come;
    AllFragment allFrag;
    SearchFragment searchFrag;
    BarcodeFragment barFrag;
    MenuFragment menuFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_prod_info, container, false);
        // Inflate the layout for this fragment
        //Fragment manager and transaction for changing fragment
        fmanager = getFragmentManager();
        ftrans = fmanager.beginTransaction();
        //create new fragment objects to change
        allFrag = new AllFragment();
        searchFrag = new SearchFragment();
        barFrag = new BarcodeFragment();
        menuFrag = new MenuFragment();

        prod_cnt = rootview.findViewById(R.id.pd_cnt);
        prod_name = rootview.findViewById(R.id.pd_name);
        prod_num = rootview.findViewById(R.id.pd_num);
        prod_comp = rootview.findViewById(R.id.pd_comp);
        prod_size = rootview.findViewById(R.id.pd_size);
        prod_color = rootview.findViewById(R.id.pd_color);
        backBtn = rootview.findViewById(R.id.back_btn);
        //get product information from bundle
        Bundle getBundle = this.getArguments();
        pd_name = getBundle.getString("pd_name", "");
        pd_num = getBundle.getString("pd_num","");
        pd_comp = getBundle.getString("pd_comp","");
        pd_cnt = getBundle.getInt("pd_cnt",0);
        pd_color = getBundle.getString("pd_color","");
        pd_size = getBundle.getString("pd_size","");

        come = getBundle.getString("back","main");

        prod_cnt.setText(Integer.toString(pd_cnt));
        prod_comp.setText(pd_comp);
        prod_name.setText(pd_name);
        prod_num.setText(pd_num);
        prod_size.setText(pd_size);
        prod_color.setText(pd_color);
        //Back button
        //come : before page(if 'all', the user press back button, go back to 'all fragment')
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(come.equals("all")){
                    Bundle bundle = new Bundle();
                    bundle.putString("comp",pd_comp);
                    allFrag.setArguments(bundle);
                    ftrans.replace(R.id.container, allFrag).commit();
                }
                else if(come.equals("search")){
                    Bundle bundle = new Bundle();
                    bundle.putString("comp",pd_comp);
                    searchFrag.setArguments(bundle);
                    ftrans.replace(R.id.container, searchFrag).commit();
                }
                else if(come.equals("barcode")){
                    Bundle bundle = new Bundle();
                    bundle.putString("comp",pd_comp);
                    barFrag.setArguments(bundle);
                    ftrans.replace(R.id.container, barFrag).commit();
                }
                else
                    ftrans.replace(R.id.container, menuFrag).commit();

            }
        });
        return rootview;
    }
}
