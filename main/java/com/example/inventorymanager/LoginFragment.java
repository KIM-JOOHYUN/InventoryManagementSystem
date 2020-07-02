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

    EditText idTxt; // id
    EditText pwTxt; //password
    Button loginBtn;  //login button
    private ArrayList<User> arrayList; // all user data

    boolean idSuccess = false; // if the id(user typed) is in database, it is True
    boolean pwSuccess = false; // if the pw(user typed) is in database, it is True
    boolean acc = false;  // if the user is authorized user, it is True
    String comp = ""; //company name(business name)
    //Fragment manager and transaction for changing fragment
    FragmentManager fmanager;
    FragmentTransaction ftrans;

    MenuFragment menuF; // main menu fragment

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(); // database from firebase

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

        //get id, pw from EditText
        idTxt = rootview.findViewById(R.id.id_txt);
        pwTxt = rootview.findViewById(R.id.pw_txt);
        loginBtn = rootview.findViewById(R.id.login_btn);

        //when the login button is pressed, check that the login account is in database
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

                //check user
                //if id and pw is correct and it is account of authorized user, login is success and change fragment to main menu
                if(idSuccess && pwSuccess && acc){
                    Toast.makeText(getActivity(),"Login Success", Toast.LENGTH_LONG).show();
                    //create new bundle and put the company name into bundle and send it to main menu fragment
                    Bundle bundle = new Bundle();
                    bundle.putString("comp",comp);
                    menuF.setArguments(bundle);
                    //check
                    Log.d("checkId","usercompany : "+comp);
                    ftrans.replace(R.id.container, menuF).commit();
                }
                //if id is wrong
                else if(!idSuccess){
                    Toast.makeText(getActivity(),"ID is wrong!\nCheck your ID.", Toast.LENGTH_LONG).show();
                }
                //if pw is wrong
                else if(!pwSuccess){
                    Toast.makeText(getActivity(),"PW is wrong!\nCheck your Password.", Toast.LENGTH_LONG).show();
                }
                //if she/he is not authorized user.
                else{
                    Toast.makeText(getActivity(),"You are not an authorized user.", Toast.LENGTH_LONG).show();
                }

            } // OnClick end

        }); // loginBtn end

        // Inflate the layout for this fragment
        return rootview;
    }


}
