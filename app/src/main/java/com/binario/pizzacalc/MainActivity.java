package com.binario.pizzacalc;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton button_about_back;
    View activityView;
    public static int current_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        activityView = findViewById(android.R.id.content);
        button_about_back = findViewById(R.id.button_about_back);
        //toolbar.setNavigationIcon(R.drawable.button_delete);
        /*
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.onBackPressed();
            }
        });



*/

        setSupportActionBar(toolbar);
        //Add back navigation in the title bar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //delete title from toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

/*
        {
            findViewById(R.id.button_about_back2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (current_fragment == 2) {
                        Toast.makeText(view.getContext(), "BACK_BUTTON_SECOND", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

 */
        //--------------------------------------------------------
        {
            findViewById(R.id.button_about_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //button_about_back.setPressed(true);
                    //onBackPressed();
                    //Fragment fragment = getSupportFragmentManager().findFragmentByTag("nav_host_fragment");
                    if (current_fragment == 1) {
                        //Toast.makeText(view.getContext(), "BACK_BUTTON_FIRST", Toast.LENGTH_SHORT).show();

                    } else if (current_fragment == 2) {
                        //Toast.makeText(view.getContext(), "BACK_BUTTON_SECOND", Toast.LENGTH_SHORT).show();


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Animation rotate = (Animation) AnimationUtils.loadAnimation(activityView.getContext(), R.anim.rotate);
                                button_about_back.setAnimation(rotate);

                                Fragment f = (Fragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                                NavHostFragment.findNavController(f).navigate(R.id.action_SecondFragment_to_MainMenuFragment);
                                //SecondFragment.back();
                                current_fragment = 0;
                                // hide your button here
                                button_about_back.setEnabled(false);
                                button_about_back.setVisibility(View.GONE);
                            }
                        }, 125);

                    } else if (current_fragment == 3) {
                        //Toast.makeText(view.getContext(), "BACK_BUTTON_THIRD", Toast.LENGTH_SHORT).show();





                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Animation rotate = (Animation) AnimationUtils.loadAnimation(activityView.getContext(), R.anim.rotate);
                                button_about_back.setAnimation(rotate);
                                Fragment f = (Fragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                                NavHostFragment.findNavController(f).navigate(R.id.action_ThirdFragment_to_MainMenuFragment);

                                current_fragment = 0;
                                // hide your button here
                                button_about_back.setEnabled(false);
                                button_about_back.setVisibility(View.GONE);
                            }
                        }, 125);
                    }


                    //Toast.makeText(view.getContext(), "BACK_BUTTON", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

       // Context wrapper = new ContextThemeWrapper(MainActivity.this, R.style.PopupMenu_Custom);
        //PopupMenu popup = new PopupMenu(wrapper, v);
        //popup.getMenuInflater().inflate(R.menu.menu_main, popup.getMenu());

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.popmenu_about) {
            return true;
        }
        //if (id == R.id.button_about_back) {
         //   onBackPressed();
         //   return true;
        //}
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

     if(current_fragment==3 || current_fragment==2){

         Animation rotate = (Animation) AnimationUtils.loadAnimation(activityView.getContext(), R.anim.rotate);
         button_about_back.setAnimation(rotate);
         //Fragment f = (Fragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        // NavHostFragment.findNavController(f).navigate(R.id.action_ThirdFragment_to_MainMenuFragment);
         current_fragment = 0;

         new Handler().postDelayed(new Runnable() {
             @Override
             public void run() {

                 // hide your button here
                 button_about_back.setEnabled(false);
                 button_about_back.setVisibility(View.GONE);
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

}
