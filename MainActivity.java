package com.example.inventorymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    //Fragment manager and transaction for changing fragment
    FragmentManager fmanager;
    FragmentTransaction ftrans;
    LoginFragment logF; // login fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set first fragment by login fragment
        logF = new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id. container , logF).commit();
    }
}
