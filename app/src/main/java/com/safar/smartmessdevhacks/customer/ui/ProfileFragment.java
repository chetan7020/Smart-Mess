package com.safar.smartmessdevhacks.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.safar.smartmessdevhacks.LoginActivity;
import com.safar.smartmessdevhacks.databinding.CustomerFragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private CustomerFragmentProfileBinding binding;

    private FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = CustomerFragmentProfileBinding.inflate(getLayoutInflater());

        firebaseAuth = FirebaseAuth.getInstance();

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        return binding.getRoot();
    }

}