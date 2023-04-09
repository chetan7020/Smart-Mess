package com.safar.smartmessdevhacks.owner.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.safar.smartmessdevhacks.R;
import com.safar.smartmessdevhacks.databinding.FragmentManageCouponBinding;

public class ManageCouponFragment extends Fragment {

    private FragmentManageCouponBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentManageCouponBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }
}