package com.example.inventorymanager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.util.ArrayList;
import java.util.List;


public class BarcodeFragment extends Fragment {
    private CaptureManager capture;
    private DecoratedBarcodeView barcodeView;
    //Fragment manager and transaction for changing fragment
    FragmentManager fmanager;
    FragmentTransaction ftrans;

    ProdInfoFragment infoFrag; // the fragment which shows information of product
    MenuFragment menuFrag; // main menu fragment
    Button backBtn; // back button

    String comp;  //company name(business name)

    private Context context; // context
    private ArrayList<Prod> arrayList; // arrayList to store the company's products
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(); // database from firebase
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_barcode, container, false);
        //set fragment manager and transaction
        fmanager = getFragmentManager();
        ftrans = fmanager.beginTransaction();
        //create new fragment objects to change
        infoFrag = new ProdInfoFragment();
        menuFrag = new MenuFragment();

        arrayList = new ArrayList<Prod>();

        context = rootView.getContext();
        //When back button is pressed, move to main fragment
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
        //get company name from bundle
        Bundle bundle = this.getArguments();
        comp = bundle.getString("comp");
        //read data from fire base(mDatabase) and find the company's products and store the products to arrayList
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    for(DataSnapshot st : snapshot.getChildren()){
//                        Log.d("checkId",st.getValue().toString());
                        Prod prod = st.getValue(Prod.class);
                        if(prod.productName != null)
                            if(prod.business.equals(comp)){
                                arrayList.add(prod);
                            }
                    }
                    /**/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });
        //set barcode view
        barcodeView = (DecoratedBarcodeView)rootView.findViewById(R.id.dbvBarcode);
        //set capture manager
        capture = new CaptureManager(this.getActivity(), barcodeView);
        //scan the barcode and decode the barcode
        capture.initializeFromIntent(getActivity().getIntent(),savedInstanceState);
        capture.decode();
        barcodeView.decodeContinuous(new BarcodeCallback() {
            //when the barcode is scanned, run readBarcode method
            @Override
            public void barcodeResult(BarcodeResult result) {
                readBarcode(result.toString());
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
            }
        });
        return rootView;
    }
    @Override
    public void onResume(){
        super.onResume();
        capture.onResume();
    }
    public void onPause(){
        super.onPause();
        capture.onPause();
    }
    public void onDestroy(){
        super.onDestroy();
        capture.onDestroy();
    }
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    public void readBarcode(String barcode){
        //Toast.makeText(getActivity().getApplicationContext(),barcode,Toast.LENGTH_SHORT).show();
        //find the product which barcode number is scanned barcode, and create a bundle and put the product information to the bundle
        //send the bundle to infoFragment(which shows product information) and change the fragment to infoFragment
        for(Prod i : arrayList){
            if(i.barcode.equals(barcode)){
                Bundle bundle = new Bundle();
                bundle.putString("pd_name",i.productName);
                bundle.putString("pd_num",i.productCode);
                bundle.putString("pd_comp",i.business);
                bundle.putInt("pd_cnt",i.stock);
                bundle.putString("pd_size",i.size);
                bundle.putString("pd_color",i.color);
                bundle.putString("back","barcode");
                infoFrag.setArguments(bundle);
                ftrans.replace(R.id.container, infoFrag).commit();
            }
        }
    }
}
