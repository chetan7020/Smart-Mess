package com.safar.smartmessdevhacks.owner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.safar.smartmessdevhacks.databinding.OwnerFragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private OwnerFragmentProfileBinding binding;

    private void init(){

    }

    private void initialize(){

    }

    private void registerListener(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = OwnerFragmentProfileBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

}