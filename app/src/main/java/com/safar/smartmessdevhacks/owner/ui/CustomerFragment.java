package com.safar.smartmessdevhacks.owner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.safar.smartmessdevhacks.databinding.OwnerFragmentCustomerBinding;


public class CustomerFragment extends Fragment {

    private OwnerFragmentCustomerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = OwnerFragmentCustomerBinding.inflate(getLayoutInflater());


        return binding.getRoot();
    }
}