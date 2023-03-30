package com.safar.smartmessdevhacks.customer;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.safar.smartmessdevhacks.R;
import com.safar.smartmessdevhacks.databinding.ActivityCustomerMainBinding;

public class CustomerMainActivity extends AppCompatActivity {

    public static String name, email, phoneNumber;
    private ActivityCustomerMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCustomerMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        name = email = phoneNumber = null;

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_coupon, R.id.navigation_mess, R.id.navigation_offer, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_customer_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}