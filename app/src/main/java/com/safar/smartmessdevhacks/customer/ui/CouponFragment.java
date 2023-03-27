package com.safar.smartmessdevhacks.customer.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safar.smartmessdevhacks.databinding.CustomerFragmentCouponBinding;


public class CouponFragment extends Fragment {

    private CustomerFragmentCouponBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = CustomerFragmentCouponBinding.inflate(getLayoutInflater());


        return binding.getRoot();
    }
}