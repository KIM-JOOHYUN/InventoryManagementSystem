package com.example.inventorymanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    FragmentManager fmanager;
    FragmentTransaction ftrans;
    LoginFragment logF;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logF = new LoginFragment();
        getSupportFragmentManager().beginTransaction().add(R.id. container , logF).commit();
    }
}
