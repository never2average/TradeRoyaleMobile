package com.example.kartikbhardwaj.bottom_navigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kartikbhardwaj.bottom_navigation.faq_view.Faq_Activity;
import com.example.kartikbhardwaj.bottom_navigation.howitworks.HowItWorksFragment;
import com.example.kartikbhardwaj.bottom_navigation.notification.NotificationActivity;
import com.example.kartikbhardwaj.bottom_navigation.HomeFragment;
import com.example.kartikbhardwaj.bottom_navigation.StatsFragment;


import com.example.kartikbhardwaj.bottom_navigation.stories.StoriesActivity;


import com.example.kartikbhardwaj.bottom_navigation.Portfolio.MyPortfolio;
import com.example.kartikbhardwaj.bottom_navigation.howitworks.HowItWorksFragment;
import com.example.kartikbhardwaj.bottom_navigation.notification.NotificationActivity;
import com.github.clans.fab.FloatingActionMenu;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity{


    Toolbar toolbar;
    ImageView logo;
    CircleImageView profilepic;
    ImageView notificationButton;
     com.github.clans.fab.FloatingActionButton referrals;
    com.github.clans.fab.FloatingActionButton faq;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo=findViewById(R.id.logo);
        notificationButton=findViewById(R.id.notificationicon);
        profilepic=findViewById(R.id.profilePic);

        SharedPreferences.Editor editor = getSharedPreferences("AUTHENTICATION", MODE_PRIVATE).edit();
//        editor.putString("token","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoiQnVyam9zZSIsImV4cCI6MTU4MjU1NzQzNH0.M9K5ZcW515hWwBe3gNHdVB6AhQRpubfuQFn7xvrpLNg");
        editor.putString("token","Not found!");
        editor.apply();

// floating action button
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
//                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
//            }
//        });
        loadfragment(new HomeFragment());


         final FloatingActionMenu fabMenu;
        fabMenu = findViewById(R.id.fabMenu);
        referrals=findViewById(R.id.referrals);
        faq=findViewById(R.id.faq);


        fabMenu.setClosedOnTouchOutside(true);



        referrals.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                // ((FloatingActionsMenu) findViewById(R.id.multiple_actions_down)).removeButton(removeAction);
                swapFragment();
                fabMenu.close(true);


            }
        });

       faq .setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                // ((FloatingActionsMenu) findViewById(R.id.multiple_actions_down)).removeButton(removeAction);
                fabMenu.close(true);
                Intent faqIntent=new Intent(MainActivity.this, Faq_Activity.class);
                startActivity(faqIntent);



            }
        });





        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapProfileFragment();


            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, NotificationActivity.class);

                startActivity(intent);

            }
        });


        // do not dellete this comment
        // animation for fab to turn into close arrow after it is clicked

        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(fabMenu.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(fabMenu.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(fabMenu.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(fabMenu.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                fabMenu.getMenuIconView().setImageResource(fabMenu.isOpened()
                        ? R.drawable.ic_plus : R.drawable.ic_close);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        fabMenu.setIconToggleAnimatorSet(set);

    }

        public boolean loadfragment(Fragment fragment) {
            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                return true;
            }
            return false;


        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pic:
                Intent profileIntent=new Intent(this,Profile_Activity.class);
                startActivity(profileIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchToPortfolioActivity(View view){
        Intent portfolioIntent =new Intent(MainActivity.this,MyPortfolio.class);
        startActivity(portfolioIntent);
    }

    private void swaphiwFragment(){
        HowItWorksFragment fragment = new HowItWorksFragment();


        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
         }
    }
    private void swapstatFragment(){
        StatsFragment fragment = new StatsFragment();


        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
    private void swapFragment(){
        ReferralFragment fragment = new ReferralFragment();

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }

    private void swaphomeFragment(){

        HomeFragment fragment = new HomeFragment();

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
}


    private void swapProfileFragment(){

        ProfileFragment fragment = new ProfileFragment();

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
         }
    }


}
