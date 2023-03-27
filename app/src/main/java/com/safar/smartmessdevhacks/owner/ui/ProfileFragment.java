package com.safar.smartmessdevhacks.owner.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.safar.smartmessdevhacks.LoginActivity;
import com.safar.smartmessdevhacks.databinding.OwnerFragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private OwnerFragmentProfileBinding binding;

    private FirebaseAuth firebaseAuth;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = OwnerFragmentProfileBinding.inflate(getLayoutInflater());

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