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

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = CustomerFragmentProfileBinding.inflate(getLayoutInflater());

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setProfile();
        binding.btnProfileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

        binding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editProfile();
            }
        });

        return binding.getRoot();
    }




    private void editProfile() {
        Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.customer_profilee_edit_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText etName, etPreferances, etPhoneNumber;
        Button btnSaveChanges;

        etName = dialog.findViewById(R.id.etName);
        etPreferances = dialog.findViewById(R.id.etPreferances);
        etPhoneNumber = dialog.findViewById(R.id.etPhoneNumber);
        btnSaveChanges = dialog.findViewById(R.id.btnSaveChanges);

        String name = binding.tvName.getText().toString().trim();
        String preferances = binding.tvPreference.getText().toString().trim();
        String phoneNumber = binding.tvPhoneNumber.getText().toString().trim();

        etName.setText(name);
        etPreferances.setText(preferances);
        etPhoneNumber.setText(phoneNumber);
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnSaveChanges.setEnabled(false);

                String name = etName.getText().toString().trim();
                String preferances = etPreferances.getText().toString().trim();
                String phoneNumber = etPhoneNumber.getText().toString().trim();

                Map<String, Object> data = new HashMap<>();
                data.put("name", name);
                data.put("item", preferances);
                data.put("customerphone", phoneNumber);

                firebaseFirestore
                        .collection("Customer")
                        .document(firebaseAuth.getCurrentUser().getEmail())
                        .update(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                                btnSaveChanges.setEnabled(true);
                                dialog.cancel();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), "Failed to Update", Toast.LENGTH_SHORT).show();
                                btnSaveChanges.setEnabled(true);
                                dialog.cancel();
                            }
                        });

            }
        });

        dialog.show();
    }

    private void setProfile() {
        firebaseFirestore
                .collection("Customer")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    }
                });
    }




}