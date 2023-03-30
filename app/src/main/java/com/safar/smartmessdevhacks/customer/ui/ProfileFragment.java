package com.safar.smartmessdevhacks.customer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.safar.smartmessdevhacks.LoginActivity;
import com.safar.smartmessdevhacks.customer.CustomerMainActivity;
import com.safar.smartmessdevhacks.databinding.CustomerFragmentProfileBinding;
import com.safar.smartmessdevhacks.model.Customer;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private CustomerFragmentProfileBinding binding;

    private void init() {
        initialize();

        registerListener();
        getProfileInfo();
    }

    private void initialize() {


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void registerListener() {
        binding.btnProfileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }

    private void getProfileInfo() {
        if (CustomerMainActivity.name == null) {
            firebaseFirestore
                    .collection("Customer")
                    .document(firebaseAuth.getCurrentUser().getEmail())
                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (value != null) {

                                Customer customer = value.toObject(Customer.class);

                                CustomerMainActivity.name = customer.getName();
                                CustomerMainActivity.email = customer.getEmail();
                                CustomerMainActivity.phoneNumber = customer.getPhoneNumber();

                                setProfileInfo();
                            } else if (error != null) {
                                makeToast(error.getMessage());
                            }
                        }
                    });
        } else {
            setProfileInfo();
        }
    }

    private void setProfileInfo() {
        binding.tvName.setText(CustomerMainActivity.name);
        binding.tvEmail.setText(CustomerMainActivity.email);
        binding.tvPhoneNumber.setText(CustomerMainActivity.phoneNumber);
    }

    private void makeToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = CustomerFragmentProfileBinding.inflate(getLayoutInflater());

        init();

        return binding.getRoot();
    }

}