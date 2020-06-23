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

    FragmentManager fmanager;
    FragmentTransaction ftrans;
    ProdInfoFragment infoFrag;
    MenuFragment menuFrag;
    Button backBtn;

    String comp;

    private Context context;
    private ArrayList<Prod> arrayList;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_barcode, container, false);
        fmanager = getFragmentManager();
        ftrans = fmanager.beginTransaction();
        infoFrag = new ProdInfoFragment();
        menuFrag = new MenuFragment();

        arrayList = new ArrayList<Prod>();

        context = rootView.getContext();

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
        Bundle bundle = this.getArguments();
        comp = bundle.getString("comp");

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

        barcodeView = (DecoratedBarcodeView)rootView.findViewById(R.id.dbvBarcode);

        capture = new CaptureManager(this.getActivity(), barcodeView);
        capture.initializeFromIntent(getActivity().getIntent(),savedInstanceState);
        capture.decode();
        barcodeView.decodeContinuous(new BarcodeCallback() {
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
