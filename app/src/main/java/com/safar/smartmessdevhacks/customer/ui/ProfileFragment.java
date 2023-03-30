package com.safar.smartmessdevhacks.customer.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.safar.smartmessdevhacks.LoginActivity;
import com.safar.smartmessdevhacks.R;
import com.safar.smartmessdevhacks.databinding.CustomerFragmentProfileBinding;

import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private CustomerFragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = CustomerFragmentProfileBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

}