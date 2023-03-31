package com.safar.smartmessdevhacks.owner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.safar.smartmessdevhacks.databinding.OwnerFragmentFoodBinding;
import com.safar.smartmessdevhacks.model.Menu;

import java.util.ArrayList;

public class FoodFragment extends Fragment {

    private static final String TAG = "FoodFragment";
    private String menuName, note, email;
    private ArrayList<String> contents;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private OwnerFragmentFoodBinding binding;

    private void init() {
        initialize();
        registerListener();
    }

    private void initialize() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        menuName = note = "";
        email = firebaseAuth.getCurrentUser().getEmail();
    }

    private void registerListener() {
        firebaseFirestore
                .collection("Owner")
                .document(email)
                .collection("Menu")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            for (DocumentChange change : value.getDocumentChanges()) {
                                Menu menu = change.getDocument().toObject(Menu.class);
                                menuName = menu.getMenuName();
                                note = menu.getNote();
                                contents = menu.getContents();
                                switch (change.getType()) {
                                    case ADDED:
                                        createCard();
//                                        Log.d(TAG, "onEvent: ADDED");
//                                        Log.d(TAG, "onEvent: "+menuName);
//                                        Log.d(TAG, "onEvent: "+note);
//                                        Log.d(TAG, "onEvent: "+contents);
                                        break;
                                    case MODIFIED:
                                        updateCard();
//                                        Log.d(TAG, "onEvent: MODIFIED");
//                                        Log.d(TAG, "onEvent: "+menuName);
//                                        Log.d(TAG, "onEvent: "+note);
//                                        Log.d(TAG, "onEvent: "+contents);
                                        break;
                                    case REMOVED:
//                                        Log.d(TAG, "onEvent: REMOVED");
//                                        Log.d(TAG, "onEvent: "+menuName);
//                                        Log.d(TAG, "onEvent: "+note);
//                                        Log.d(TAG, "onEvent: "+contents);
                                        break;
                                }
                            }
                        }
                    }
                });
    }

    private void createCard(){

    }

    private void updateCard(){

    }
    private boolean checkEmpty() {

        if (menuName.equals("") || note.equals("")) {
            return false;
        }
        return true;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = OwnerFragmentFoodBinding.inflate(getLayoutInflater());

        init();

        return binding.getRoot();
    }
}