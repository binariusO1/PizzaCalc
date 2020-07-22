package com.binario.pizzacomparator;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;
import java.util.Objects;

public class FragmentHelp extends Fragment {

    //============================
    //01. onCreate
    //02. override functions
    //03. custom functions
    //============================
    private FrameLayout adContainerView;
    private AdView adView;
    //===============================================================================================================================================
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          01. onCreate
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //===============================================================================================================================================
    //only for use onCreateOptionsMenu
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {

        View view = inflater.inflate(R.layout.fragment_help, container, false);

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

        Button button_send = (Button) view.findViewById(R.id.button_send);
        button_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                sendFeedback();
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
                    NavHostFragment.findNavController(FragmentHelp.this).navigate(R.id.action_FragmentHelp_to_FragmentCompare);
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
    //===============================================================================================================================================
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          02. override functions
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //===============================================================================================================================================

    //use it for hie menu item in fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    //===============================================================================================================================================
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                              03. custom functions
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //===============================================================================================================================================

    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          Send button
    //---------------------------------------------------------------------------------------------------------------------------------------------
    public void sendFeedback()
    {
        //method 2
        final Intent _Intent = new Intent(android.content.Intent.ACTION_SEND);
        _Intent.setType("plain/text");
        _Intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ getString(R.string.mail_feedback_email) });
        String text = getString(R.string.mail_feedback_subject) + " (" + getString(R.string.version) + ")";
        _Intent.putExtra(android.content.Intent.EXTRA_SUBJECT, text);

        //choose the gmail as default
        final PackageManager pm = requireActivity().getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(_Intent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") ||
                    info.activityInfo.name.toLowerCase().contains("gmail")) best = info;
        if (best != null)
            _Intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);


        // _Intent.putExtra(android.content.Intent.EXTRA_TEXT, getString(R.string.mail_feedback_message));
        try {
            startActivity(Intent.createChooser(_Intent, getString(R.string.title_send_feedback)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(FragmentHelp.this.getContext(), getString(R.string.mail_feedback_error), Toast.LENGTH_SHORT).show();
        }

        //Resources res = getResources();
        //String text = res.getString(R.string.clear_text);
        //Toast.makeText(HelpActivity.this, text, Toast.LENGTH_SHORT).show();;
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
