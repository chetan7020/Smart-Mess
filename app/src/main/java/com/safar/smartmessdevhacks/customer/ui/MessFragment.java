package com.safar.smartmessdevhacks.customer.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.safar.smartmessdevhacks.databinding.CustomerFragmentMessBinding;

public class MessFragment extends Fragment {

    private CustomerFragmentMessBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = CustomerFragmentMessBinding.inflate(getLayoutInflater());


        return binding.getRoot();
    }
}