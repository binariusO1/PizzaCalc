package com.binario.pizzacomparator;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;


import com.binario.pizzacomparator.helper.SimpleItemTouchHelperCallback;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class FragmentCompare extends Fragment {

    private FloatingActionButton add_button;
    private ConstraintLayout fragment_compare;
    private ViewGroup.LayoutParams recycler_height;

    private FrameLayout adContainerView;
    private AdView adView;

    //get items as global (for activity, not only for fragment)
    private PizzaAdapter adapter;
    private RecyclerView recyclerView;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState )
    {

        View view = inflater.inflate(R.layout.fragment_compare, container, false);
        InitializeField(view);


        //---------------------------------------------------------------------------------------------------------------------------------------------
        //                                                          ADMOB SETTINGS
        //---------------------------------------------------------------------------------------------------------------------------------------------
        {
            MobileAds.initialize(view.getContext(), new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            adContainerView = view.findViewById(R.id.ad_view_container);
            // Step 1 - Create an AdView and set the ad unit ID on it.
            adView = new AdView(view.getContext());
            adView.setAdUnitId(getString(R.string.adaptive_banner_ad_unit_id));
            //adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
            adContainerView.addView(adView);
            loadBanner(view);
        }

        //---------------------------------------------------------------------------------------------------------------------------------------------
        //                                                          ADD BUTTON
        //---------------------------------------------------------------------------------------------------------------------------------------------
        {
            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (adapter.Size() < 8) {
                        adapter.add();
                    }

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
                    adapter.popupAddValues(button,position);
                    recyclerView.scrollToPosition(position);
                }
            });
        }
/*
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK && adapter.isClickable == true)
                {
                    Log.d("----------------------------TAG", "Lecim: (" + (0) + ")");
                    NavHostFragment.findNavController(FragmentCompare.this).navigate(R.id.action_FragmentCompare_to_MainMenuFragment);
                    MainActivity.current_fragment = 0;
                    return true;
                }
                else
                {
                    adapter.isClickable = true;
                    return false;
                }
            }
        });

*/
        //get fragment height on start
        final View v = view;
        v.post(new Runnable() {
            @Override
            public void run() {
                // for instance
                int height = v.getMeasuredHeight();
                adapter.setRecyclerFragmentHeight(height);
                //Log.d("----------------------------TAG", "OnCreate_height: (" + (height) + ")");
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
        MainActivity.current_fragment = 1;

        //create an object for converting units [dp] - [px]

        //////int true_height_px = (convert.dpToPx(92));
        //////PizzaGroup.setHeight(true_height_px);

        fragment_compare = view.findViewById(R.id.ft_c);

        //defines buttons
        add_button = (FloatingActionButton) view.findViewById(R.id.add_button);


        // Lookup the recyclerview in activity layout
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recycler_height=recyclerView.getLayoutParams();
        recyclerView.setLayoutParams(recycler_height);
        //recyclerView.setHasFixedSize(true);

        // Create adapter passing in the sample user data
        //adapter = new PizzaAdapter(2);
        adapter = MainActivity.adapter;

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

        //Toast.makeText(ThirdFragment.this.getContext(), "HEIGHT: " + height_screen, Toast.LENGTH_SHORT).show();
        //.adapter.notifyDataSetChanged();

        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        //recyclerView.setItemAnimator(null);

        //set move by touch callback
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter,getContext());
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        //initialize recycler view height for fragment to auto-update in adapter functions
        adapter.setRecyclerView(recyclerView,fragment_compare);


    }



    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          ADMOB FUNCTION
    //---------------------------------------------------------------------------------------------------------------------------------------------

    private void loadBanner(final View view) {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                int width_adv_dp=0;
                int height_adv_dp=60;

                DisplayMetrics displayMetrics = new DisplayMetrics();

                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height_screen = displayMetrics.heightPixels;
                int width_screen = displayMetrics.widthPixels;
                width_adv_dp = MainActivity.converter.pxToDp(width_screen);
                //width_adv_dp = converter.pxToDp(width_screen);

                int orientation = getResources().getConfiguration().orientation;
                AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
                //32 dp	≤ 400 dp
                //50 dp	> 400 dp and ≤ 720 dp
                //90 dp	> 720 dp

                //FrameLayout f = (FrameLayout) findViewById(R.id.ad_view_container);
                //LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)f.getLayoutParams();
                //lp.height = 59;
                //         ViewGroup.LayoutParams bar_2 = scale_bar_2.getLayoutParams();
                //  bar_2.width = (  ((int)  (1*   ((42f-formula)/28)*length_bar      ))  +helper3);
                //   RelativeLayout.LayoutParams bar_s = (RelativeLayout.LayoutParams)scale_bar_selector.getLayoutParams();

                AdSize adSize = AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(view.getContext(), width_adv_dp);

                width_adv_dp = MainActivity.converter.pxToDp(width_screen);

                if(MainActivity.converter.pxToDp(height_screen)<MainActivity.HEIGHT_SMALL_SCREEN)
                {
                    height_adv_dp = 32;
                    adSize = new AdSize(width_adv_dp,height_adv_dp);
                }
                else
                {

                }
                adView.setAdSize(adSize);
                adView.loadAd(adRequest);
            }
        }, 100);
    }
}