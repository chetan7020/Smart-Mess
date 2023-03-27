package com.safar.smartmessdevhacks.owner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.safar.smartmessdevhacks.databinding.OwnerFragmentCustomerBinding;
import com.safar.smartmessdevhacks.databinding.OwnerFragmentFoodBinding;

public class FoodFragment extends Fragment {

    private OwnerFragmentFoodBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = OwnerFragmentFoodBinding.inflate(getLayoutInflater());


        return binding.getRoot();
    }
}