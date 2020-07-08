package com.binario.pizzacalc;

import android.app.ActionBar;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

ImageButton button_about_back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        // Inflate the layout for this fragment
        button_about_back = getActivity().findViewById(R.id.button_about_back);
        //button_about_back.setVisibility(View.INVISIBLE);


        MainActivity.current_fragment = 1;
        return view;
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_menu_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.current_fragment = 2;
                NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_MainMenuFragment_to_SecondFragment);
                button_about_back.setEnabled(true);
                button_about_back.setVisibility(View.VISIBLE);
            }
        });

        view.findViewById(R.id.button_menu_3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_MainMenuFragment_to_ThirdFragment);
                button_about_back.setEnabled(true);
                button_about_back.setVisibility(View.VISIBLE);
            }
        });
    }
}
