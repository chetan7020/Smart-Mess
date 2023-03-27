package com.safar.smartmessdevhacks.customer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.safar.smartmessdevhacks.databinding.CustomerFragmentOfferBinding;

public class OfferFragment extends Fragment {

    private CustomerFragmentOfferBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = CustomerFragmentOfferBinding.inflate(getLayoutInflater());


        return binding.getRoot();
    }
}