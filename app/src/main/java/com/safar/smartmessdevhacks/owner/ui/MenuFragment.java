package com.safar.smartmessdevhacks.owner.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.safar.smartmessdevhacks.R;
import com.safar.smartmessdevhacks.databinding.OwnerFragmentFoodBinding;
import com.safar.smartmessdevhacks.model.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuFragment extends Fragment {

    private static final String TAG = "FoodFragment";
    private String id, menuName, note, email, content;
    private int price;
    private ArrayList<String> contents, editContents;
    private Dialog addDialog, updateDialog;
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

        initializeVariables();
        email = firebaseAuth.getCurrentUser().getEmail();
    }

    private void initializeVariables() {
        contents = new ArrayList<>();
        id = menuName = note = "";
        price = 0;
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
                                String id = menu.getId();
                                String menuName = menu.getMenuName();
                                String note = menu.getNote();
                                int price = menu.getPrice();
                                ArrayList<String> contents = menu.getContents();

                                switch (change.getType()) {
                                    case ADDED:
                                        createCard(id, menuName, note, price, contents);
                                        break;
                                    case MODIFIED:
                                        updateCard(id, menuName, note, price, contents);
                                        break;
                                }
                            }
                        }
                    }
                });

        binding.fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog = new Dialog(getActivity());

                addDialog.setContentView(R.layout.owner_menu_add_layout);
                addDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                EditText etMenuName, etContent, etNote, etPrice;
                Button btnAddMenu;
                ImageView ivAdd;

                etMenuName = addDialog.findViewById(R.id.etMenuName);
                etContent = addDialog.findViewById(R.id.etContent);
                etNote = addDialog.findViewById(R.id.etNote);
                etPrice = addDialog.findViewById(R.id.etPrice);

                btnAddMenu = addDialog.findViewById(R.id.btnAddMenu);
                ivAdd = addDialog.findViewById(R.id.ivAdd);

                btnAddMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        id = createID();
                        menuName = etMenuName.getText().toString().trim();
                        note = etNote.getText().toString().trim();
                        if (!etPrice.getText().toString().trim().equals("")) {
                            price = Integer.parseInt(etPrice.getText().toString().trim());
                        }
                        if (checkEmpty()) {
                            Log.d(TAG, "Set: " + id);
                            Log.d(TAG, "Set: " + menuName);
                            Log.d(TAG, "Set: " + note);
                            Log.d(TAG, "Set: " + price);
                            Log.d(TAG, "Set: " + contents);
                            addMenuToFirestore(id, menuName, note, price, contents);
                        } else {
                            Toast.makeText(getActivity(), "All Field's are required", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ivAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        content = etContent.getText().toString().trim();
                        if (content.equals("")) {
                            Toast.makeText(getActivity(), "Empty Field", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "onClick: " + content);
                            contents.add(content);
                            addContentToLayout(content);
                            etContent.setText("");
                        }
                    }
                });

                addDialog.show();
            }
        });
    }

    private void addMenuToFirestore(String id, String menuName, String note, int price, ArrayList<String> contents) {
        firebaseFirestore
                .collection("Owner")
                .document(email)
                .collection("Menu")
                .document(id)
                .set(new Menu(id, menuName, note, price, contents))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        initializeVariables();
                        Toast.makeText(getActivity(), "Menu Added", Toast.LENGTH_SHORT).show();
                        addDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        addDialog.dismiss();
                    }
                });
    }

    private void createCard(String id, String menuName, String note, int price, ArrayList<String> contents) {
        View menuCardView = getLayoutInflater().inflate(R.layout.owner_menu_card_layout, null, false);

        TextView tvID = menuCardView.findViewById(R.id.tvID);
        TextView tvMenuName = menuCardView.findViewById(R.id.tvMenuName);
        TextView tvNote = menuCardView.findViewById(R.id.tvNote);
        TextView tvPrice = menuCardView.findViewById(R.id.tvPrice);
        TextView tvContents = menuCardView.findViewById(R.id.tvContents);

        Button btnEdit = menuCardView.findViewById(R.id.btnEdit);
        Button btnDelete = menuCardView.findViewById(R.id.btnDelete);

        tvID.setText(id);
        tvMenuName.setText(menuName);
        tvNote.setText(note);
        tvPrice.setText("Rs." + price);

        StringBuilder content = new StringBuilder();

        for (int i = 0; i < contents.size(); i++) {

            if (i != (contents.size() - 1)) {
                content.append(i + 1).append(". ").append(contents.get(i)).append("\n");
            } else {
                content.append(i + 1).append(". ").append(contents.get(i));
            }
        }

        tvContents.setText(content.toString());
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editContents = contents;
                editMenu(id, menuName, note, price);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMenuFromFirestore(id);
            }
        });

        binding.llData.addView(menuCardView);
    }

    private void deleteMenuFromFirestore(String id) {
        firebaseFirestore
                .collection("Owner")
                .document(email)
                .collection("Menu")
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        for (int i = 0; i < binding.llData.getChildCount(); i++) {

                            TextView tvID = binding.llData.getChildAt(i).findViewById(R.id.tvID);

                            if (tvID.getText().toString().trim().equals(id)) {
                                binding.llData.removeView(binding.llData.getChildAt(i));
                                Toast.makeText(getActivity(), "Menu Deleted", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void editMenu(String id, String menuName, String note, int price) {
        updateDialog = new Dialog(getActivity());

        updateDialog.setContentView(R.layout.owner_menu_edit_layout);
        updateDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EditText etMenuName, etContent, etNote, etPrice;
        Button btnSaveChanges;
        ImageView ivAdd;

        etMenuName = updateDialog.findViewById(R.id.etMenuName);
        etContent = updateDialog.findViewById(R.id.etContent);
        etNote = updateDialog.findViewById(R.id.etNote);
        etPrice = updateDialog.findViewById(R.id.etPrice);

        ivAdd = updateDialog.findViewById(R.id.ivAdd);
        btnSaveChanges = updateDialog.findViewById(R.id.btnSaveChanges);

        Log.d(TAG, "editMenu: " + id);
        Log.d(TAG, "editMenu: " + menuName);
        Log.d(TAG, "editMenu: " + note);
        Log.d(TAG, "editMenu: " + price);
        Log.d(TAG, "editMenu: " + editContents);

        etMenuName.setText(menuName);
        etNote.setText(note);
        etPrice.setText(Integer.toString(price));

        for (String content : editContents) {
            addContentToLayout_Edit(content);
        }

        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content = etContent.getText().toString().trim();
                if (content.equals("")) {
                    Toast.makeText(getActivity(), "Empty Field", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onClick: " + content);
                    editContents.add(content);
                    addContentToLayout_Edit(content);
                    content = "";
                    etContent.setText("");
                }
            }
        });

        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String menuName = etMenuName.getText().toString().trim();
                String note = etNote.getText().toString().trim();
                int price = 0;
                if (!etPrice.getText().toString().trim().equals("")) {
                    price = Integer.parseInt(etPrice.getText().toString().trim());
                }
                if (menuName.equals("") || note.equals("") || price == 0 || editContents.isEmpty()) {
                    if (menuName.equals("")) {
                        Log.d(TAG, "checkEmpty: menuName is empty");
                    }

                    if (note.equals("")) {
                        Log.d(TAG, "checkEmpty: note is empty");
                    }

                    if (price == 0) {
                        Log.d(TAG, "checkEmpty: price is empty");
                    }

                    if (editContents.isEmpty()) {
                        Log.d(TAG, "checkEmpty: contents is empty");
                    }
                } else {
                    Log.d(TAG, "Set: " + id);
                    Log.d(TAG, "Set: " + menuName);
                    Log.d(TAG, "Set: " + note);
                    Log.d(TAG, "Set: " + price);
                    Log.d(TAG, "Set: " + editContents);
                    editMenuToFirestore(id, menuName, note, price);
                }
            }
        });

        updateDialog.show();
    }

    private void editMenuToFirestore(String id, String menuName, String note, int price) {
        Map<String, Object> data = new HashMap<>();
        data.put("menuName", menuName);
        data.put("note", note);
        data.put("price", price);
        data.put("contents", editContents);

        firebaseFirestore
                .collection("Owner")
                .document(email)
                .collection("Menu")
                .document(id)
                .update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        editContents = new ArrayList<>();
                        Toast.makeText(getActivity(), "Menu Updated", Toast.LENGTH_SHORT).show();
                        updateDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        updateDialog.dismiss();
                    }
                });
    }

    private void addContentToLayout_Edit(String content) {
        GridLayout glData = updateDialog.findViewById(R.id.glData);
        View contentView = getLayoutInflater().inflate(R.layout.add_content_display_layout, null, false);

        TextView tvContentName;
        ImageView ivDelete;

        tvContentName = contentView.findViewById(R.id.tvContentName);
        ivDelete = contentView.findViewById(R.id.ivDelete);

        tvContentName.setText(content);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = tvContentName.getText().toString();
                editContents.remove(content);
                for (int i = 0; i < glData.getChildCount(); i++) {
                    TextView tvContentName = glData.getChildAt(i).findViewById(R.id.tvContentName);

                    if (tvContentName.getText().toString().trim().equals(content)) {
                        glData.removeView(glData.getChildAt(i));
                    }
                }
            }
        });

        glData.addView(contentView);
    }

    private void updateCard(String id, String menuName, String note, int price, ArrayList<String> contents) {

        for (int i = 0; i < binding.llData.getChildCount(); i++) {

            TextView tvID = binding.llData.getChildAt(i).findViewById(R.id.tvID);
            TextView tvMenuName = binding.llData.getChildAt(i).findViewById(R.id.tvMenuName);
            TextView tvNote = binding.llData.getChildAt(i).findViewById(R.id.tvNote);
            TextView tvPrice = binding.llData.getChildAt(i).findViewById(R.id.tvPrice);
            TextView tvContents = binding.llData.getChildAt(i).findViewById(R.id.tvContents);

            if (tvID.getText().toString().trim().equals(id)) {

                tvMenuName.setText(menuName);
                tvNote.setText(note);
                tvPrice.setText("Rs." + price);

                StringBuilder content = new StringBuilder();

                for (int j = 0; j < contents.size(); j++) {

                    if (j != (contents.size() - 1)) {
                        content.append(j + 1).append(". ").append(contents.get(j)).append("\n");
                    } else {
                        content.append(j + 1).append(". ").append(contents.get(j));
                    }
                }

                tvContents.setText(content.toString());

                break;
            }

        }
    }

    private boolean checkEmpty() {
        if (menuName.equals("") || note.equals("") || price == 0 || contents.isEmpty()) {
            if (menuName.equals("")) {
                Log.d(TAG, "checkEmpty: menuName is empty");
            }

            if (note.equals("")) {
                Log.d(TAG, "checkEmpty: note is empty");
            }

            if (price == 0) {
                Log.d(TAG, "checkEmpty: price is empty");
            }

            if (contents.isEmpty()) {
                Log.d(TAG, "checkEmpty: contents is empty");
            }
            return false;
        }
        return true;
    }

    private String createID() {
        return UUID.randomUUID().toString();
    }

    private void addContentToLayout(String content) {
        GridLayout glData = addDialog.findViewById(R.id.glData);
        View contentView = getLayoutInflater().inflate(R.layout.add_content_display_layout, null, false);

        TextView tvContentName;
        ImageView ivDelete;

        tvContentName = contentView.findViewById(R.id.tvContentName);
        ivDelete = contentView.findViewById(R.id.ivDelete);

        tvContentName.setText(content);
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = tvContentName.getText().toString();
                contents.remove(content);
                for (int i = 0; i < glData.getChildCount(); i++) {

                    TextView tvContentName = glData.getChildAt(i).findViewById(R.id.tvContentName);

                    if (tvContentName.getText().toString().trim().equals(content)) {
                        glData.removeView(glData.getChildAt(i));
                    }
                }
            }
        });

        glData.addView(contentView);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = OwnerFragmentFoodBinding.inflate(getLayoutInflater());

        init();

        return binding.getRoot();
    }
}