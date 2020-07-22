package com.binario.pizzacomparator;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentRefresh extends Fragment {

    //ghost fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View view = inflater.inflate(R.layout.fragment_refresh, container, false);

        ProgressDialog mDialog = new ProgressDialog(view.getContext());
        mDialog.setMessage("Loading...");
        mDialog.setCancelable(false);
        mDialog.show();

        return view;
    }

}
