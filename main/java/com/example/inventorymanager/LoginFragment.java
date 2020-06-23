package com.example.inventorymanager;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LoginFragment extends Fragment {

    String id;
    String pw;

    EditText idTxt;
    EditText pwTxt;
    Button loginBtn;
    private ArrayList<User> arrayList;

    boolean idSuccess = false;
    boolean pwSuccess = false;
    boolean acc = false;
    String comp = "";

    FragmentManager fmanager;
    FragmentTransaction ftrans;

    MenuFragment menuF;

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup)inflater.inflate(R.layout.fragment_login, container, false);

        fmanager = getFragmentManager();
        ftrans = fmanager.beginTransaction();
        menuF = new MenuFragment();

//        ArrayList to store user data
        arrayList = new ArrayList<User>();

//      read user data
        mDatabase.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    // TODO: handle the post
                    for(DataSnapshot st : snapshot.getChildren()){
                        User user = st.getValue(User.class);
                        if(user.id != null)
                            arrayList.add(user);
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("getValue","Error");
            }
        });// mDatabase end

        idTxt = rootview.findViewById(R.id.id_txt);
        pwTxt = rootview.findViewById(R.id.pw_txt);
        loginBtn = rootview.findViewById(R.id.login_btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = idTxt.getText().toString();
                pw = pwTxt.getText().toString();
                Log.d("getValue","hello "+id);

                Log.d("checkId",Integer.toString(arrayList.size()));
                for(User user : arrayList){
//                    check
//                    Log.d("checkId",user.id);
                    if(user.id.equals(id)){
                        idSuccess = true;
                        if(user.password.equals(pw)) {
                            pwSuccess = true;
                            acc = user.approval;
                            comp = user.business;
                            break;
                        }
                        pwSuccess = false;
                        acc = user.approval;
                        break;
                    }
                    idSuccess = false;
                    pwSuccess = false;
                } // for end

                if(idSuccess && pwSuccess && acc){
                    Toast.makeText(getActivity(),"Login Success", Toast.LENGTH_LONG).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("comp",comp);
                    menuF.setArguments(bundle);
                    Log.d("checkId","usercompany : "+comp);
                    ftrans.replace(R.id.container, menuF).commit();
                } else if(!idSuccess){
                    Toast.makeText(getActivity(),"ID is wrong!\nCheck your ID.", Toast.LENGTH_LONG).show();
                }
                else if(!pwSuccess){
                    Toast.makeText(getActivity(),"PW is wrong!\nCheck your Password.", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(),"You are not an authorized user.", Toast.LENGTH_LONG).show();
                }

            } // OnClick end

        }); // loginBtn end

        // Inflate the layout for this fragment
        return rootview;
    }


}
