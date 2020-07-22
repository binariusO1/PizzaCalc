package com.binario.pizzacomparator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Currency;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    //============================
    //01. onCreate
    //02. override functions
    //03. custom functions
    //============================
    ImageButton button_appbar_back;
    ImageButton button_reset;
    ImageButton button_help;
    TextView toolbar_titile;
    View activityView;
    AlertDialog alertDialogHelp;
    AlertDialog alertDialogRate;

    //global (one activity)
    public static int current_fragment;
    public static int settings_units;
    public static String current_currency;
    public static PizzaAdapter adapter;
    public static int BOTTOM_BAR_HEIGHT;
    public static final int HEIGHT_SMALL_SCREEN = 522;
    public static final int WIDTH_SMALL_SCREEN = 320;
    public static final int WIDTH_MEDIUM_SCREEN = 360;
    public static ConvertPxDp converter;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    boolean swapped;
    private float x1,x2;
    static final int MIN_DISTANCE = 220;


    //===============================================================================================================================================
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          01. onCreate
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //===============================================================================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeField();
        boolean swapped = false;
        //--------------------------------------------------------
        {
            findViewById(R.id.button_appbar_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    //1 - fragment compare
                    //2 - fragment settings
                    //3 - fragment help
                    //4 - fragment about
                    if (current_fragment == 1) {


                    } else if (current_fragment == 2) {
                        changeFragment2();
                    } else if (current_fragment == 3) {
                        changeFragment3();
                    } else if (current_fragment == 4) {
                        changeFragment4();
                    }
                }
            });
        }
/*
                //gesture
            final GestureDetector gesture = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    //Log.i(Constants.APP_TAG, "onFling has been called!");
                    final int SWIPE_MIN_DISTANCE = 120;
                    final int SWIPE_MAX_OFF_PATH = 250;
                    final int SWIPE_THRESHOLD_VELOCITY = 200;
                    try {
                        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                            return false;
                            //if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                            //{
                            //   Toast.makeText(FragmentHelp.this.getContext(), "RIGHT->LEFT: ", Toast.LENGTH_SHORT).show();
                            //}
                        else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
                        {
                            if(current_fragment == 2)
                            {
                                changeFragment2();
                            }
                            else if (current_fragment == 3) {
                                changeFragment3();
                            }
                            else if (current_fragment == 4) {
                                changeFragment4();
                            }
                            //Toast.makeText(MainActivity.this, "LEFT->RIGHT: ", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        // nothing
                    }
                    return super.onFling(e1, e2, velocityX, velocityY);
                }
            });
            this.activityView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gesture.onTouchEvent(event);
                }
            });
            */

        //--------------------------------------------------------
        {
            findViewById(R.id.button_reset).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    //1 - fragment compare
                    //2 - fragment settings
                    //3 - fragment help
                    //4 - fragment about
                    if (current_fragment == 1)
                    {
                        popupYesNo(view);
                    }
                    }
                    //
            });
        }
        //--------------------------------------------------------
        {
            findViewById(R.id.button_help).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    //1 - fragment compare
                    //2 - fragment settings
                    //3 - fragment help
                    //4 - fragment about
                    if (current_fragment == 1)
                    {
                        popupHelp(view);
                    }
                }
                //
            });
        }
    }

    //===============================================================================================================================================
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          02. override functions
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //===============================================================================================================================================
    @Override
    public boolean dispatchTouchEvent(MotionEvent event){
        this.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (swapped) {
     /*Make sure you don't swap twice,
since the dispatchTouchEvent might dispatch your touch event to this function again!*/
            swapped = false;
            return super.onTouchEvent(event);
        }
        try {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x1 = event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    x2 = event.getX();
                    float deltaX = x2 - x1;
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        swapped = false;
                        if (current_fragment == 2) {
                            changeFragment2();
                            swapped = true;
                        } else if (current_fragment == 3) {
                            changeFragment3();
                            swapped = true;
                        } else if (current_fragment == 4) {
                            changeFragment4();
                            swapped = true;
                        }
                        //you already swapped, set flag swapped = true

                    } else {
                        // not swapping
                    }
                    break;
            }
        }
        catch (Exception e) {
        // nothing
        }
        return super.onTouchEvent(event);
    }
        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
/*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //Toast.makeText(this, "onPrepareOptionsMenu", Toast.LENGTH_SHORT).show();
        return true;

    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.popmenu_ratethisapp) {
            Rate_this_app();
        }

        if (id == R.id.popmenu_update){
            showUpdate();
        }
        if (id == R.id.popmenu_settings) {
            MainActivity.current_fragment = 2;
            Fragment f = (Fragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavHostFragment.findNavController(f).navigate(R.id.action_FragmentCompare_to_FragmentSettings);
            changeStatusButton(button_appbar_back,true);
            changeStatusButton(button_reset,false);
            changeStatusButton(button_help,false);
            changeTitle(R.string.bar_title_settings);
        }

        if (id == R.id.popmenu_help) {
            MainActivity.current_fragment = 3;
            Fragment f = (Fragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavHostFragment.findNavController(f).navigate(R.id.action_FragmentCompare_to_FragmentHelp);
            changeStatusButton(button_appbar_back,true);
            changeStatusButton(button_reset,false);
            changeStatusButton(button_help,false);
            changeTitle(R.string.bar_title_help);
        }

        if (id == R.id.popmenu_about) {
            MainActivity.current_fragment = 4;
            Fragment f = (Fragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
            NavHostFragment.findNavController(f).navigate(R.id.action_FragmentCompare_to_FragmentAbout);
            changeStatusButton(button_appbar_back,true);
            changeStatusButton(button_reset,false);
            changeStatusButton(button_help,false);
            changeTitle(R.string.bar_title_about);
        }
        //if (id == R.id.button_appbar_back) {
         //   onBackPressed();
         //   return true;
        //}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

     if(current_fragment==2 || current_fragment==3 || current_fragment ==4){

         animDisappearButton(button_appbar_back);
         current_fragment = 0;

         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {

                 // hide your button here
                 changeStatusButton(button_appbar_back,false);
             }
         }, 50);
        }
     if(current_fragment==1)
     {
         finish();
     }
     else
         super.onBackPressed();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("CURRENT_CURRENCY", current_currency);
        outState.putInt("SETTINGS_UNITS",settings_units);

        //save to settings-file
        editor = settings.edit();
        editor.putString("CURRENT_CURRENCY", current_currency);
        editor.putInt("SETTINGS_UNITS",settings_units);
        editor.apply();

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }
    //===============================================================================================================================================
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                              03. custom functions
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //===============================================================================================================================================
    //---------------------------------------------------------------------------------------------------------------------------------------------
    //                                                          INITIALIZE FIELD
    //---------------------------------------------------------------------------------------------------------------------------------------------
    private void initializeField()
    {

        //1 - cm, 2 - inch

        //read from settings-file
        settings = getApplicationContext().getSharedPreferences("SETTINGS", 0);
        settings_units = settings.getInt("SETTINGS_UNITS", 1);
        current_currency = settings.getString("CURRENT_CURRENCY", "$");

        adapter = new PizzaAdapter(2);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        activityView = findViewById(android.R.id.content);
        button_appbar_back = findViewById(R.id.button_appbar_back);
        button_help = findViewById(R.id.button_help);
        button_reset = findViewById(R.id.button_reset);
        toolbar_titile = findViewById(R.id.action_bar_title);

        setSupportActionBar(toolbar);
        //delete title from toolbar

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        button_appbar_back.setEnabled(false);
        button_appbar_back.setVisibility(View.GONE);
        //button_help.setEnabled(true);
        //button_help.setVisibility(View.VISIBLE);
        //button_reset.setEnabled(true);
        //button_reset.setVisibility(View.VISIBLE);
        converter = new ConvertPxDp();
        loadSize();
    }

    private void changeTitle(int id)
    {
        String title = getResources().getString(id);
        toolbar_titile.setText(title);
    }
    private  void changeStatusButton(ImageButton button, boolean state)
    {
        button.setEnabled(state);
        if(state) button.setVisibility(View.VISIBLE);
        else button.setVisibility(View.GONE);
    }
    private  void animRotateButton(View view, ImageButton button)
    {
        Animation rotate = (Animation) AnimationUtils.loadAnimation(activityView.getContext(), R.anim.rotate);
        button.setAnimation(rotate);
        view.startAnimation(rotate);
    }
    private  void animDisappearButton(ImageButton button)
    {
        Animation disappear = (Animation) AnimationUtils.loadAnimation(activityView.getContext(), R.anim.disappear);
        button.setAnimation(disappear);
    }
    public void send_email()
    {

        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.popup_window_rate_bad, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyThemeOverlayAlertDialog);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        alertDialogRate.dismiss();
    }
    public void showUpdate()
    {
        Resources res = getResources();
        String text = res.getString(R.string.popmenu_update_info);
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }
    public void changeFragment2()
    {
        current_fragment = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animDisappearButton(button_appbar_back);
                Fragment f = (Fragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                assert f != null;
                NavHostFragment.findNavController(f).navigate(R.id.action_FragmentSettings_to_FragmentCompare);
                //Navigation.findNavController(view).navigate(R.id.action_FragmentSettings_to_FragmentCompare);
                //SecondFragment.back();
                // hide your button here
                changeStatusButton(button_appbar_back,false);
                changeTitle(R.string.app_name);
            }
        }, 125);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeStatusButton(button_reset,true);
                changeStatusButton(button_help,true);
            }
        }, 150);
    }
    public void changeFragment3()
    {
        current_fragment = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animDisappearButton(button_appbar_back);
                Fragment f = (Fragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                assert f != null;
                NavHostFragment.findNavController(f).navigate(R.id.action_FragmentHelp_to_FragmentCompare);
                // hide your button here
                changeStatusButton(button_appbar_back,false);
                changeTitle(R.string.app_name);
            }
        }, 125);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeStatusButton(button_reset,true);
                changeStatusButton(button_help,true);
            }
        }, 150);
    }
    public void changeFragment4()
    {
        current_fragment = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animDisappearButton(button_appbar_back);
                Fragment f = (Fragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                assert f != null;
                NavHostFragment.findNavController(f).navigate(R.id.action_FragmentAbout_to_FragmentCompare);
                // hide your button here
                changeStatusButton(button_appbar_back,false);
                changeTitle(R.string.app_name);
            }
        }, 125);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeStatusButton(button_reset,true);
                changeStatusButton(button_help,true);
            }
        }, 150);
    }
    public void loadSize()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height_screen = displayMetrics.heightPixels;
                int width_screen = displayMetrics.widthPixels;
                //32 dp	≤ 400 dp
                //50 dp	> 400 dp and ≤ 720 dp
                //90 dp	> 720 dp
                    if(converter.pxToDp(height_screen)<HEIGHT_SMALL_SCREEN)
                    {
                        BOTTOM_BAR_HEIGHT = 68;
                        //Toast.makeText(MainActivity.this, "H1 HEIGHT: " + BOTTOM_BAR_HEIGHT, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        BOTTOM_BAR_HEIGHT=105;
                        //Toast.makeText(MainActivity.this, "H2 HEIGHT: " + BOTTOM_BAR_HEIGHT, Toast.LENGTH_SHORT).show();
                    }
                //Toast.makeText(MainActivity.this, "Hss", Toast.LENGTH_SHORT).show();
            }
        }, 10);
    }
    //====================================================================
    //                         POPUPS
    //====================================================================
    public void popupYesNo(final View view)
    {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.popup_yes_no, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyThemeOverlayAlertDialog);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
        WindowManager.LayoutParams lp = Objects.requireNonNull(alertDialog.getWindow()).getAttributes();
        lp.dimAmount = 0.6f;
        alertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.width = this.getWindow().getDecorView().getWidth();
        alertDialog.getWindow().setAttributes(lp);
        //alertDialog.getWindow().setLayout(this.getWindow().getDecorView().getWidth(),converter.dpToPx(160));

        TextView dialogText = (TextView) dialogView.findViewById(R.id.pop_yn_question);
        String temp = dialogView.getResources().getString(R.string.pop_yn_question_2);
        dialogText.setText(temp);
        Button pop_yn_yes_button = (Button) dialogView.findViewById(R.id.pop_yn_yes_button);
        Button pop_yn_no_button = (Button) dialogView.findViewById(R.id.pop_yn_no_button);
        final boolean value;
        pop_yn_yes_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animRotateButton(view,button_reset);
                        adapter = new PizzaAdapter(2);
                        Fragment f = (Fragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                        NavHostFragment.findNavController(f).navigate(R.id.action_FragmentCompare_to_FragmentRefresh);
                        NavHostFragment.findNavController(f).navigate(R.id.action_FragmentRefresh_to_FragmentCompare);
                    }
                }, 80);
                alertDialog.cancel();
            }
        });
        pop_yn_no_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        alertDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    alertDialog.dismiss();
                }
                return true;
            }
        });
    }
    public void popupHelp(View view)
    {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.popup_help, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyThemeOverlayAlertDialogHelp);
        builder.setView(dialogView);
        alertDialogHelp = builder.create();
        alertDialogHelp.show();
        //alertDialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams lp = Objects.requireNonNull(alertDialogHelp.getWindow()).getAttributes();
        lp.dimAmount = 0.6f;
        alertDialogHelp.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        //alertDialog.getWindow().setLayout(this.getWindow().getDecorView().getWidth(),400);
    }
    //    android:onClick="dismiss_help" in popup_help.xml
    public void dismiss_help(View view)
    {
        try {
            alertDialogHelp.dismiss();
        }
        catch (Exception a)
        {}
    }
    public void Rate_this_app()
    {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.popup_rate_app, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyThemeOverlayAlertDialog);
        builder.setView(dialogView);
        alertDialogRate = builder.create();
        alertDialogRate.show();
        WindowManager.LayoutParams lp = alertDialogRate.getWindow().getAttributes();
        lp.dimAmount = 0.6f;
        alertDialogRate.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
    public void rate_1(View view)
    {
        send_email();
    }
    public void rate_2(View view)
    {
        send_email();
    }
    public void rate_3(View view)
    {
        send_email();
    }
    public void rate_4(View view)
    {
        //We use a try/catch block here because an Exception will be thrown if the Play Store is not installed on the target device.
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }
    public void rate_5(View view)
    {
        //We use a try/catch block here because an Exception will be thrown if the Play Store is not installed on the target device.
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
        //Toast.makeText(MainActivity.this, getString(R.string.go_to_url), Toast.LENGTH_SHORT).show();
    }
    public void sendFeedback_rate(View view)
    {
        //method 2
        final Intent _Intent = new Intent(android.content.Intent.ACTION_SEND);
        _Intent.setType("plain/text");
        _Intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ getString(R.string.mail_feedback_email) });
        String text = getString(R.string.mail_feedback_subject) + " (" + getString(R.string.version) + ")";
        _Intent.putExtra(android.content.Intent.EXTRA_SUBJECT, text);

        //choose the gmail as default
        final PackageManager pm = getPackageManager();
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
            //Toast.makeText(MainActivity.this, getString(R.string.mail_feedback_error), Toast.LENGTH_SHORT).show();
        }

        //Resources res = getResources();
        //String text = res.getString(R.string.clear_text);
        //Toast.makeText(HelpActivity.this, text, Toast.LENGTH_SHORT).show();;
    }
}
