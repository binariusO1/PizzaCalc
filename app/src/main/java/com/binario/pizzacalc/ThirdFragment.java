package com.binario.pizzacalc;


import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;


import com.binario.pizzacalc.helper.SimpleItemTouchHelperCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ThirdFragment extends Fragment {

    private FloatingActionButton add_button;
    private PizzaAdapter adapter;
    private RecyclerView recyclerView;
    private ConstraintLayout fragment_third;
    private ViewGroup.LayoutParams recycler_height;
    private ConvertPxDp convert;
    private int height_screen;
    private final static int BOTTOM_BAR_HEIGHT = 88;
    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {

        View view = inflater.inflate(R.layout.fragment_third, container, false);
        InitializeField(view);

        //---------------------------------------------------------------------------------------------------------------------------------------------
        //                                                          ADD BUTTON
        //---------------------------------------------------------------------------------------------------------------------------------------------
        {
            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter.Size() < 10) {
                        adapter.add();

                        //adapter.notifyItemRangeChanged(0, adapter.Size());
                        //adapter.notifyDataSetChanged();
                        //check view to stopping add button
                        //height recycler view [px]< height screen [px] - value of [px]

                        /*
                        height_screen = fragment_third.getHeight();
                        recycler_height.height = adapter.getRecyclerHeight();
                        if (recycler_height.height >= height_screen - bottom_bar_height) {
                            recycler_height.height = height_screen - bottom_bar_height;
                        }
                        recyclerView.setLayoutParams(recycler_height);
                        */
                        Toast.makeText(ThirdFragment.this.getContext(), "arr.size(): "+adapter.Size(), Toast.LENGTH_SHORT).show();


                        //Toast.makeText(ThirdFragment.this.getContext(), "RECYCLER_HEIGHT: " + recycler_height.height + " HEIGHT_SCREEN-convert-a: " + (height_screen-bottom_bar_height-app_bar_height), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(ThirdFragment.this.getContext(), "RECYCLER_HEIGHT: " + recycler_height.height + "_HEIGHT: " + (height_screen-convert.dpToPx(150)), Toast.LENGTH_SHORT).show();

                        //Toast.makeText(ThirdFragment.this.getContext(), "ADD_BUTTON_CLICK_ADD_ITEM " + (PizzaGroup.Size()-1), Toast.LENGTH_SHORT).show();
                    }
                    //else
                    //Toast.makeText(ThirdFragment.this.getContext(), "ADD_BUTTON_CLICK_OVERFLOW", Toast.LENGTH_SHORT).show();
                    recyclerView.scrollToPosition(adapter.Size() - 1);

                }
            });

        }

        //---------------------------------------------------------------------------------------------------------------------------------------------
        //                                                          EXPAND BUTTON
        //---------------------------------------------------------------------------------------------------------------------------------------------
        {
            adapter.setExpandButtonListener(new PizzaAdapter.OnExpandButtonItemClickListener() {
                @Override
                public void onExpandIsClick(View button, int position) {

                    popupAddValues(button,position);

                    //adapter.notifyDataSetChanged();
                    //Toast.makeText(ThirdFragment.this.getContext(), "CLICK_EXPAND "+position + "\nPOS: " + adapter.getItemNumber(0)+ "\nPOS: " + adapter.getItemNumber(1)+ "\nPOS: " + adapter.getItemNumber(2)+ "\nPOS: " + adapter.getItemNumber(3), Toast.LENGTH_SHORT).show();;
                    Toast.makeText(ThirdFragment.this.getContext(), "POSITION: " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
        //---------------------------------------------------------------------------------------------------------------------------------------------
        //                                                          DELETE BUTTON
        //---------------------------------------------------------------------------------------------------------------------------------------------
        /*
        {
            adapter.setDeleteButtonListener(new PizzaAdapter.OnDeleteButtonItemClickListener() {
                @Override
                public void onDeleteIsClick(View button, int position) {
                    //Log.d("----------------------------TAG", "CLICK_DELETE: (" + (position) + ")");
                    if (position >= adapter.Size()) {
                    } else {
                        adapter.remove(position);
                        //adapter.notifyDataSetChanged();
                        //adapter.notifyItemRemoved(position);
                        //adapter.notifyItemRangeChanged(0,adapter.Size());   //for many items: .notifyItemRangeChanged
                        //code below causes glitch, during deleting last item when recycler_height.height >= height_screen - this element is not necessary
                        //recyclerView.removeViewAt(position);
                        //Toast.makeText(ThirdFragment.this.getContext(), "CLICK_DELETE "+position, Toast.LENGTH_SHORT).show();
                        Toast.makeText(ThirdFragment.this.getContext(), "arr.size(): " + adapter.Size() + " \nREMOVE_POSITION: " + position, Toast.LENGTH_SHORT).show();
                        //Log.d("----------------------------TAG", "CLICK_DELETE_END: (" + (position) + ")");

                       //adapter.isClickable = false;
                       //new Handler().postDelayed(new Runnable() {
                        //   @Override
                        //   public void run() {
                       //        adapter.isClickable = true;
                         //  }
                       //}, 50);

                        //recyclerView.setHasFixedSize(true);
                    }
                }
            });
        }
        */

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
               @Override
               public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && adapter.isClickable == true)
                    {
                        Log.d("----------------------------TAG", "Lecim: (" + (0) + ")");
                        NavHostFragment.findNavController(ThirdFragment.this).navigate(R.id.action_ThirdFragment_to_MainMenuFragment);
                        MainActivity.current_fragment = 0;
                        return true;
                    }
                    else
                    {
                        adapter.isClickable = true;
                    }
                    return true;
               }
        });

        return view;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          INITIALIZE FIELD
    //---------------------------------------------------------------------------------------------------------------------------------------------
    private void InitializeField(View view)
    {
        //set value of current fragment number
        MainActivity.current_fragment = 3;

        //create an object for converting units [dp] - [px]
        convert = new ConvertPxDp();

        //////int true_height_px = (convert.dpToPx(92));
        //////PizzaGroup.setHeight(true_height_px);

        fragment_third = view.findViewById(R.id.ft_c);

        //defines buttons
        add_button = (FloatingActionButton) view.findViewById(R.id.add_button);


        // Lookup the recyclerview in activity layout
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recycler_height=recyclerView.getLayoutParams();
        recyclerView.setLayoutParams(recycler_height);
        //recyclerView.setHasFixedSize(true);

        // Create adapter passing in the sample user data
        adapter = new PizzaAdapter(2);
        //recycler_height.height=adapter.getRecyclerHeight();

        //Toast.makeText(ThirdFragment.this.getContext(), "float: " + true_height_px, Toast.LENGTH_SHORT).show();

        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);

        // Set layout manager to position the items

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        //final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //linearLayoutManager.setReverseLayout(true);
        //recyclerView.setLayoutManager(linearLayoutManager);
        // That's all!


        //to obsolete - switch to constraintlayout.getHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height_screen = displayMetrics.heightPixels;
        //Toast.makeText(ThirdFragment.this.getContext(), "HEIGHT: " + height_screen, Toast.LENGTH_SHORT).show();
        //.adapter.notifyDataSetChanged();

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        //recyclerView.setItemAnimator(null);

        //set move by touch callback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter,getContext());
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        //initialize recycler view height for fragment to auto-update in adapter functions
        height_screen = fragment_third.getHeight();
        adapter.setRecyclerView(recyclerView,BOTTOM_BAR_HEIGHT,fragment_third);

    }


    public void popupAddValues(View view, final int pos)
    {
        ViewGroup viewGroup = view.findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this.getContext()).inflate(R.layout.popup_add_values, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.MyThemeOverlayAlertDialog);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        //alertDialog.setContentView(view);
        Button add_pizza_button = (Button) dialogView.findViewById(R.id.av_add_button);

        //---------------------------------------------------------------------------------------------------------------------------------------------
        //                                                          ADD PIZZA BUTTON
        //---------------------------------------------------------------------------------------------------------------------------------------------

            add_pizza_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    EditText av_editText_diameter = (EditText) dialogView.findViewById((R.id.av_editText_diameter));
                    EditText av_editText_price = (EditText) dialogView.findViewById((R.id.av_editText_price));
                    EditText av_editText_amount = (EditText) dialogView.findViewById((R.id.av_editText_amount));
                    int diameter = 0;
                    float price = 0f;
                    int amount = 1;
                    String string_temp_1 = av_editText_diameter.getText().toString();
                    if(!string_temp_1.isEmpty()){
                        diameter = Integer.parseInt(string_temp_1);
                    }
                    String string_temp_2 = av_editText_price.getText().toString();
                    if(!string_temp_2.isEmpty()){
                        price = Float.parseFloat(string_temp_2);
                    }
                    String string_temp_3 = av_editText_amount.getText().toString();
                    if(!string_temp_3.isEmpty()){
                        amount = Integer.parseInt(string_temp_3);
                    }
                    if( diameter != 0f && price != 0f)
                    {
                        adapter.addPizza(pos,diameter,price,amount);
                        //Toast.makeText(ThirdFragment.this.getContext(), "add pizza to pos: " + pos, Toast.LENGTH_SHORT).show();
                        alertDialog.cancel();
                    }
                    else
                    {
                        Toast.makeText(ThirdFragment.this.getContext(), "UNABLE_TO_ADD_PIZZA_TEXT" + "\n" + "diam: " + diameter + " price: " + price, Toast.LENGTH_SHORT).show();
                    }

                }
            });


    }

}

//List
/*
package com.binario.pizzacalc;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ThirdFragment extends Fragment {


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {

        View view = inflater.inflate(R.layout.fragment_third, container, false);
        InitializeField(view);

        final ListView listview = (ListView) view.findViewById(R.id.listview);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }

        //        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_list_item_1, list);
        StableArrayAdapter adapter = new StableArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                Toast.makeText(view.getContext(), "ITEM_LIST_CLICK", Toast.LENGTH_SHORT).show();

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

        });

        // Inflate the layout for this fragment


        return view;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_third).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ThirdFragment.this)
                        .navigate(R.id.action_ThirdFragment_to_MainMenuFragment);
            }
        });
    }

    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId, List<String> objects)
        {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i)
            {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }

    private void InitializeField(View view)
    {

    }

}

 */