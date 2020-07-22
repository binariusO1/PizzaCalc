package com.binario.pizzacomparator;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FragmentAbout extends Fragment {

    //only for use onCreateOptionsMenu
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View view = inflater.inflate(R.layout.fragment_about, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                PizzaAdapter adapter;
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    NavHostFragment.findNavController(FragmentAbout.this).navigate(R.id.action_FragmentAbout_to_FragmentCompare);
                    MainActivity.current_fragment = 0;
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
        return view;
    }

    //use it for hie menu item in fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
