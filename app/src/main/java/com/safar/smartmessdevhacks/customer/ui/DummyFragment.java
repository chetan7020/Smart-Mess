package com.safar.smartmessdevhacks.customer.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;
import com.safar.smartmessdevhacks.R;
import com.safar.smartmessdevhacks.databinding.FragmentDummyBinding;
import com.safar.smartmessdevhacks.databinding.OwnerFragmentCustomerBinding;

public class DummyFragment extends Fragment {

    private FirebaseFirestore firebaseFirestore;

    private void init() {
        initialize();
        registerListener();
    }

    private void initialize() {
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    private void registerListener() {

    }

    private FragmentDummyBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDummyBinding.inflate(getLayoutInflater());

        init();

        return binding.getRoot();
    }
}