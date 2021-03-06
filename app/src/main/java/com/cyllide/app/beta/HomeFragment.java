package com.cyllide.app.beta;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cyllide.app.beta.portfolio.PortfolioGameHomeActivity;
import com.cyllide.app.beta.forum.ForumActivity;
import com.cyllide.app.beta.quiz.QuizRulesActivity;
import com.cyllide.app.beta.stories.ArticlesMainActivity;
import com.facebook.drawee.backends.pipeline.Fresco;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Map;

public class HomeFragment extends Fragment {
    Calendar startTime = Calendar.getInstance();
    Dialog quizPopup;

    TextView timer;
    ImageView networkTower;
    View content;
    TextView retry_button;

    TextView greetingsTV;
    de.hdodenhof.circleimageview.CircleImageView profilePic;
    Map<String,String> homepageDataHeaders = new ArrayMap<>();
    RequestQueue homepageQueue;
    SharedPreferences sharedPreferences;
    ImageView cyllideLogo;
    StorageReference storageReference;
    ImageView profileMedal;
    String level;
    TextDrawable drawable;


    @Override
    public void onResume() {
        super.onResume();
    }

    MaterialCardView stories, portfolios, quiz, forum;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Fresco.initialize(getContext());

        return inflater.inflate(R.layout.home_fragment, null);
    }

    @Override
    public void onViewCreated(@NonNull final View view, final Bundle savedInstanceState) {
        startTime.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY) + 2);


        super.onViewCreated(view, savedInstanceState);
        stories = view.findViewById(R.id.storiescard);
        portfolios = view.findViewById(R.id.portfoliocard);
        quiz = view.findViewById(R.id.quizcard);
        forum = view.findViewById(R.id.forumcard);
        quizPopup = new Dialog(view.getContext());
        networkTower=view.findViewById(R.id.network_tower);
        retry_button=view.findViewById(R.id.retry_button);
        content=view;
        greetingsTV = view.findViewById(R.id.home_fragment_greetings);
        profilePic = view.findViewById(R.id.profile_pic_container);
       // profileMedal = view.findViewById(R.id.profile_medal);
        cyllideLogo=view.findViewById(R.id.cyllidemainlogo);
        profileMedal = view.findViewById(R.id.profile_medal);

        storageReference= FirebaseStorage.getInstance().getReference();
        sharedPreferences=view.getContext().getSharedPreferences("profileUrl",Context.MODE_PRIVATE);


        if(sharedPreferences.getString("profileUri",null)==null)
        {
            profilePic.setImageDrawable(drawable);
        }else{
            String ur=sharedPreferences.getString("profileUri",null);
            Uri uri=Uri.parse(ur);
            Log.d("imageuri",ur);
            RequestOptions requestOptions = new RequestOptions().override(100);
            Glide.with(getContext()).load(uri).apply(requestOptions).into(profilePic);
        }

        level=AppConstants.userLevel;
        try {
            if (level.equals("Gold")) {
                profileMedal.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_gold_medal));
            } else {
                if (level.equals("Silver")) {
                    profileMedal.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_silver_medal));
                } else {
                    profileMedal.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_bronze_medal));
                }
            }
        }
        catch (Exception e){}





    }

    @Override
    public void onStart() {
        super.onStart();



        final Activity activity = getActivity();
        stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ArticlesMainActivity.class);
                getContext().startActivity(intent);
//                getActivity().finish();
            }
        });

        portfolios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();


                if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
                    Snackbar snackbar = Snackbar
                            .make(content.findViewById(R.id.root_layout),"Poor Network Connection", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    content.findViewById(R.id.main_content).setVisibility(View.GONE);
                    content.findViewById(R.id.network_retry_layout).setVisibility(View.VISIBLE);


                } else {
                    Intent portfolioIntent = new Intent(getContext(), PortfolioGameHomeActivity.class);
                    startActivity(portfolioIntent);
//                    getActivity().finish();
                }
            }

        });

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (ConnectionStatus.connectionstatus) {
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("AUTHENTICATION", Context.MODE_PRIVATE);
                    String token = sharedPreferences.getString("token", "Not found!");
                    Intent intent;
                    try {
                        getActivity().findViewById(R.id.fabMenu).setVisibility(View.VISIBLE);
                        getActivity().findViewById(R.id.fab_label).setVisibility(View.VISIBLE);
                    }
                    catch (Exception e){
                        Log.d("ERROR",e.toString());
                    }

                    intent = new Intent(getContext(), QuizRulesActivity.class);

                    getContext().startActivity(intent);
//                    getActivity().finish();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(content.findViewById(R.id.root_layout),"Poor Network Connection", Snackbar.LENGTH_LONG);
                    snackbar.show();

                    content.findViewById(R.id.main_content).setVisibility(View.GONE);
                    content.findViewById(R.id.network_retry_layout).setVisibility(View.VISIBLE);
                    try{
                    getActivity().findViewById(R.id.fabMenu).setVisibility(View.GONE);
                    getActivity().findViewById(R.id.fab_label).setVisibility(View.GONE);
                    }
                    catch (Exception e){
                        Log.d("ERROR",e.toString());
                    }

                }
            }

        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),ProfileActivity.class);
                intent.putExtra("Editable",true);
                getContext().startActivity(intent);
//                getActivity().finish();

            }
        });




        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionStatus.connectionstatus) {
                    Intent intent = new Intent(getContext(), ForumActivity.class);
                    getContext().startActivity(intent);
//                    getActivity().finish();
                } else {


                   content.findViewById(R.id.main_content).setVisibility(View.GONE);
                   content.findViewById(R.id.network_retry_layout).setVisibility(View.VISIBLE);
                    Snackbar snackbar = Snackbar
                            .make(content.findViewById(R.id.root_layout),"Poor Network Connection", Snackbar.LENGTH_LONG);
                    snackbar.show();



                }
            }
        });

        retry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ConnectionStatus.connectionstatus) {
                  content.findViewById(R.id.network_retry_layout).setVisibility(View.GONE);
                  content.findViewById(R.id.main_content).setVisibility(View.VISIBLE);
                } else {


                    content.findViewById(R.id.main_content).setVisibility(View.GONE);
                    content.findViewById(R.id.network_retry_layout).setVisibility(View.VISIBLE);
                    Snackbar snackbar = Snackbar
                            .make(content.findViewById(R.id.root_layout),"Poor Network Connection", Snackbar.LENGTH_LONG);
                    snackbar.show();


                }

            }
        });

//        if(sharedPreferences.getString("profileUri",null)==null)
//        {
//            // getProfilePicVolley();
//            profilePic.setImageDrawable(drawable);
//
//        }else{
//            String ur=sharedPreferences.getString("profileUri",null);
//            Uri uri=Uri.parse(ur);
//            Log.d("imageuri",ur);
//            RequestOptions requestOptions = new RequestOptions().override(100);
//            Glide.with(getContext()).load(uri).apply(requestOptions).into(profilePic);
//        }

        fetchDataVolley();

    }

    void fetchDataVolley() {
                    greetingsTV.setText("Hey, "+AppConstants.username+"!");
                    String profileURL = AppConstants.profilePic;
                    if(profileURL.equals(AppConstants.noProfilePicURL)){
                        ColorGenerator generator = ColorGenerator.MATERIAL;
                        int color = generator.getColor(AppConstants.username);
                         drawable = TextDrawable.builder()
                                .beginConfig()
                                .width(100)
                                .height(100)
                                .endConfig()
                                .buildRect(Character.toString(AppConstants.username.charAt(0)).toUpperCase(), color);
                       // profilePic.setImageDrawable(drawable);

                        if(sharedPreferences.getString("profileUri",null)==null)
                        {
                            profilePic.setImageDrawable(drawable);
                        }else{
                            String ur=sharedPreferences.getString("profileUri",null);
                            Uri uri=Uri.parse(ur);
                            Log.d("imageuri",ur);
                            RequestOptions requestOptions = new RequestOptions().override(100);
                            Glide.with(getContext()).load(uri).apply(requestOptions).into(profilePic);
                        }


                    }
                    else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("profileUri", profileURL.toString());

                                    editor.commit();
                        RequestOptions requestOptions = new RequestOptions().override(100);
                        Glide.with(getContext()).load(profileURL).apply(requestOptions).into(profilePic);
                    }

    }


}
