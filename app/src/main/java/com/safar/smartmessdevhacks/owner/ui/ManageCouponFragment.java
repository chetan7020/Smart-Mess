package com.safar.smartmessdevhacks.owner.ui;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.safar.smartmessdevhacks.R;
import com.safar.smartmessdevhacks.databinding.FragmentManageCouponBinding;

public class ManageCouponFragment extends Fragment {

    private FragmentManageCouponBinding binding;
    LinearLayout expandable_note;
    Button see_more;
    CardView cardView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentManageCouponBinding.inflate(getLayoutInflater());

        expandable_note = binding.getRoot().findViewById(R.id.expandable_note);
        see_more = binding.getRoot().findViewById(R.id.see_more);
        cardView = binding.getRoot().findViewById(R.id.card_view);


        see_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(expandable_note.getVisibility() == View.GONE)
                {
                    TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                    expandable_note.setVisibility(View.VISIBLE);
                    see_more.setBackgroundResource(R.drawable.baseline_keyboard_arrow_up_24);
                }
                else
                {
                    TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                    expandable_note.setVisibility(View.GONE);
                    see_more.setBackgroundResource(R.drawable.baseline_keyboard_arrow_down_24);
                }

            }
        });


        return binding.getRoot();
    }
}