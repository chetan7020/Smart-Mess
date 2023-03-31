package com.safar.smartmessdevhacks.owner.ui;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;
import com.safar.smartmessdevhacks.LoginActivity;
import com.safar.smartmessdevhacks.SplashActivity;
import com.safar.smartmessdevhacks.customer.MessInfoActivity;
import com.safar.smartmessdevhacks.databinding.OwnerFragmentProfileBinding;
import com.safar.smartmessdevhacks.model.Owner;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private OwnerFragmentProfileBinding binding;

    private void init() {
        initialize();
        registerListener();
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

        firebaseFirestore
                .collection("Owner")
                .document(firebaseAuth.getCurrentUser().getEmail())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (value != null) {
                            Owner owner = value.toObject(Owner.class);

                            binding.tvName.setText(owner.getName());
                            binding.tvMessName.setText(owner.getMessName());
                            binding.tvPhoneNumber.setText(owner.getPhoneNumber());
                            binding.tvUPI.setText(owner.getUpi());

                            GeoPoint geoPoint = owner.getGeoPoint();

                            Log.d(TAG, "onEvent: "+geoPoint);

                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                                    try {
                                        List<Address> addresses = geocoder.getFromLocation(owner.getGeoPoint().getLatitude(), owner.getGeoPoint().getLongitude(), 1);
                                        if (addresses != null && addresses.size() > 0) {
                                            Address address = addresses.get(0);
                                            String location = (address.getSubLocality() + "-" + address.getLocality() + ", " + address.getPostalCode());
                                            binding.tvLocation.setText(location);
//                                            binding.tvLocation.setText(address.getSubLocality() + "-" + address.getLocality() + ", " + address.getPostalCode());
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            thread.start();

                        }

                    }
                });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = OwnerFragmentProfileBinding.inflate(getLayoutInflater());

        init();

        return binding.getRoot();
    }

}