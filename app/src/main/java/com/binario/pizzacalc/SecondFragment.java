package com.binario.pizzacalc;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    ListView PizzaList;
    RelativeLayout calc_list;
    ScrollView scrollView;
    FloatingActionButton add_button;
    Button button_expand;
    ViewGroup.LayoutParams calc_list_height;
    ArrayAdapter<Integer> arrayAdapter;


    int number_of_items;
    List<Integer> item_list;
    int pos;

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {

        View view = inflater.inflate(R.layout.fragment_second, container, false);
        InitializeField(view);

        // Inflate the layout for this fragment


        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(number_of_items >= 2 && number_of_items < 10)
                {
                    number_of_items +=1;
                    item_list.add(number_of_items);
                    calc_list_height.height += 69.5*3;
                    calc_list.setLayoutParams(calc_list_height);

                }
                else
                    Toast.makeText(SecondFragment.this.getContext(), "TEXT_OVER_10", Toast.LENGTH_SHORT).show();

                //automaticly scroll scrollView to bottom
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });

        //getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        PizzaList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View  view, int position, long id)
            {
                pos = position;


                long viewId = view.getId();
                if (viewId == R.id.cp_button_delete) {
                    Toast.makeText(view.getContext(), "BUTTON_DELETE_X", Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(view.getContext(), "ITEM_LIST_CLICK " +item_list.get(position), Toast.LENGTH_SHORT).show();

                LayoutInflater inflater = getLayoutInflater();
                View row = inflater.inflate(R.layout.temp, parent, false);
                //move to final
                Button deleteImageView = (Button) view.findViewById(R.id.cp_button_delete);

                deleteImageView.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        if(number_of_items > 2 && number_of_items < 10) {
                            item_list.remove(item_list.get(pos));
                            number_of_items -= 1;
                            calc_list_height.height -= 69.5 * 3;
                            calc_list.setLayoutParams(calc_list_height);
                        }
                        Toast.makeText(view.getContext(), "BUTTON_DELETE", Toast.LENGTH_SHORT).show();
                    }
                });


            }               /*

                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0).withEndAction(new Runnable() {
                    @Override
                    public void run()
                    {
                        list.remove(item);
                        adapter.notifyDataSetChanged();
                        view.setAlpha(1);
                    }
                });
            }
*/
        });


        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

    }
    private void InitializeField(View view)
    {
        //MainActivity.current_fragment = 2;
        Info obj;
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);

        number_of_items = 2;
        item_list = new ArrayList<Integer>();
        item_list.add(1);
        item_list.add(2);

        PizzaList = (ListView) view.findViewById(R.id.PizzaList);   //OKOK
        arrayAdapter = new ArrayAdapter<Integer>(this.getContext(), R.layout.temp, R.id.cp_position_number, item_list); //ok
        PizzaList.setAdapter(arrayAdapter); //ok

        calc_list = (RelativeLayout) view.findViewById(R.id.calc_list); //OKOK
        calc_list_height = calc_list.getLayoutParams(); //ok

        add_button = (FloatingActionButton) view.findViewById(R.id.add_button);
    }
/*
    public void getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = getLayoutInflater();
        View row = inflater.inflate(R.layout.calc_position, parent, false);
        Button deleteImageView = (Button) row.findViewById(R.id.cp_button_delete);
        deleteImageView.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                Toast.makeText(SecondFragment.this.getContext(), "BUTTON_DELETE", Toast.LENGTH_SHORT).show();
            }
        });
    }
    */

}
