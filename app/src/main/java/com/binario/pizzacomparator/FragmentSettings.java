package com.binario.pizzacomparator;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Currency;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;


public class FragmentSettings extends Fragment {

    private Button set_layout_1_button_1;
    private Button set_layout_1_button_2;
    private Button set_layout_2_button_1;

    private FrameLayout adContainerView;
    private AdView adView;

    //only for use onCreateOptionsMenu
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

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
        set_layout_1_button_1 = view.findViewById(R.id.set_layout_1_button_1);
        set_layout_1_button_2 = view.findViewById(R.id.set_layout_1_button_2);
        set_layout_2_button_1 = view.findViewById(R.id.set_layout_2_button_1);

        if(MainActivity.settings_units == 1){
            setUnits(view,1);
        }
        else{
            setUnits(view,2);
        }
        set_layout_2_button_1.setText(MainActivity.current_currency);
        set_layout_1_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUnits(view,1);
                MainActivity.settings_units = 1;
                //set_layout_1_button_1.setAlpha(0.0f);
            }
        });
        set_layout_1_button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUnits(view,2);
                MainActivity.settings_units = 2;
            }
        });
        set_layout_2_button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupCurrency(view);
                //MainActivity.settings_units = 2;
            }
        });
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                PizzaAdapter adapter;
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {
                    NavHostFragment.findNavController(FragmentSettings.this).navigate(R.id.action_FragmentSettings_to_FragmentCompare);
                    MainActivity.current_fragment = 0;
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
/*
        ListView list = (ListView) view.findViewById(R.id.listView1);
        String cars[] = {"Mercedes", "Fiat", "Ferrari", "Aston Martin", "Lamborghini", "Skoda", "Volkswagen", "Audi", "Citroen"};
        //arrayList = getAllCurrencies();
        ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(cars));
        //Log.d("----------------------------TAG", "1: (" + (0) + ")");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.item_list, arrayList);
        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this.getContext(), R.layout.item_list, R.id.contact_name, arrayList); //ok
        Log.d("----------------------------TAG", "2: (" + (0) + ")");
        list.setAdapter(arrayAdapter);
*/
        return view;
    }

    //use it for hie menu item in fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    private void setUnits(View view, int set)
    {
        if(set == 1){
            set_layout_1_button_1.setBackgroundResource(R.drawable.switch_button);
            set_layout_1_button_1.setTextColor(view.getResources().getColor(R.color.colorWhite));
            set_layout_1_button_2.setBackgroundResource(R.drawable.switch_button_hide);
            set_layout_1_button_2.setTextColor(view.getResources().getColor(R.color.colorBlack));
        }
        else
        {
            set_layout_1_button_1.setBackgroundResource(R.drawable.switch_button_hide);
            set_layout_1_button_1.setTextColor(view.getResources().getColor(R.color.colorBlack));
            set_layout_1_button_2.setBackgroundResource(R.drawable.switch_button);
            set_layout_1_button_2.setTextColor(view.getResources().getColor(R.color.colorWhite));
        }

    }
    public int popupCurrency(final View view)
    {

        Log.d("----------------------------TAG", "start: (" + (0) + ")");
        //List<String> list = getAllCurrencies();
        List<String> list = new ArrayList<String>();
        list = getAllCurrencies();
        //list.add("a");
        Log.d("----------------------------TAG", "size: (" + (list.size()) + ")");
        ArrayAdapter<String> adapter;
        final ListView listView;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(view.getContext());
        View rowList = getLayoutInflater().inflate(R.layout.popup_currency, null);
        listView = rowList.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        alertDialog.setView(rowList);
        final AlertDialog dialog = alertDialog.create();
        dialog.show();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                String value = listView.getItemAtPosition(position).toString().substring(4);
                MainActivity.current_currency = value;
                //Toast.makeText(view.getContext(), "mam: "+position+" " + value, Toast.LENGTH_SHORT).show();
                Button set_layout_2_button_1 = view.findViewById(R.id.set_layout_2_button_1);
                set_layout_2_button_1.setText(value);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.cancel();
                    }
                }, 150);
            }
        });


        return 0;

    }
    private ArrayList<String> getAllCurrencies()
    {
        Map<Currency, Locale> map = getCurrencyLocaleMap();
        String [] symbols = { "USD", "EUR", "JPY","GBP","AUD","CAD","CHF","CNY","HKD","NZD","SEK","KRW","SGD","NOK","MXN","INR","RUB","ZAR","TRY","BRL","TWD","DKK","PLN","THB","IDR",
                "HUF","CZK","ILS","CLP","PHP","AED","COP","SAR","MYR","RON" };
        Arrays.sort(symbols);
        ArrayList<String> tempArray = new ArrayList<String>();;
        for (String countryCode : symbols) {

            Currency currency = Currency.getInstance(countryCode);
            String symbol = currency.getSymbol(map.get(currency));
            if(symbol.equals("US$"))
                symbol = "$";
            String code = currency.getCurrencyCode();
            String name = currency.getDisplayName();
            String temp = code + " " + symbol;
            tempArray.add(temp);
        }
        return tempArray;
    }
    public static Map<Currency, Locale> getCurrencyLocaleMap() {
        Map<Currency, Locale> map = new HashMap<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                Currency currency = Currency.getInstance(locale);
                map.put(currency, locale);
            }
            catch (Exception e){
                // skip strange locale
            }
        }
        return map;
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
