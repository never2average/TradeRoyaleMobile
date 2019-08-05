package com.cyllide.app.v1;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cyllide.app.v1.portfolio.PortfolioGameHomeActivity;
import com.cyllide.app.v1.forum.ForumActivity;
import com.cyllide.app.v1.quiz.QuizRulesActivity;
import com.cyllide.app.v1.stories.ArticlesMainActivity;
import com.facebook.drawee.backends.pipeline.Fresco;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

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
    ImageView profileMedal;
    Uri targetUri;
    StorageReference storageReference;
    SharedPreferences sharedPreferences;
    TextDrawable drawable;
    ImageView cyllideLogo;





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
        profileMedal = view.findViewById(R.id.profile_medal);
        cyllideLogo=view.findViewById(R.id.cyllidemainlogo);
        storageReference= FirebaseStorage.getInstance().getReference();
        sharedPreferences=view.getContext().getSharedPreferences("profileUrl",Context.MODE_PRIVATE);


        if(sharedPreferences.getString("profileUri",null)==null)
        {
           // getProfilePicVolley();
         //   profilePic.setImageDrawable(drawable);

          //  Toast.makeText(getContext(),"please choose your profile pic",Toast.LENGTH_SHORT).show();
        }else{
            String ur=sharedPreferences.getString("profileUri",null);
            Uri uri=Uri.parse(ur);
            Log.d("imageuri",ur);
            RequestOptions requestOptions = new RequestOptions().override(100);
            Glide.with(getContext()).load(uri).apply(requestOptions).into(profilePic);
        }




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
                getActivity().finish();
            }
        });
        portfolios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
                    Toast.makeText(getContext(), "Poor Network Connection", Toast.LENGTH_LONG).show();
                    content.findViewById(R.id.main_content).setVisibility(View.GONE);
                    content.findViewById(R.id.network_retry_layout).setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Internet Connection Lost", Toast.LENGTH_LONG).show();


                } else {
                    Intent portfolioIntent = new Intent(getContext(), PortfolioGameHomeActivity.class);
                    startActivity(portfolioIntent);
                    getActivity().finish();
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
                    intent = new Intent(getContext(), QuizRulesActivity.class);

                    getContext().startActivity(intent);
                    getActivity().finish();
                } else {
                    Toast.makeText(getContext(), "Internet Connection Lost", Toast.LENGTH_LONG).show();
                    content.findViewById(R.id.main_content).setVisibility(View.GONE);
                    content.findViewById(R.id.network_retry_layout).setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Internet Connection Lost", Toast.LENGTH_LONG).show();


                }
            }

        });
 cyllideLogo.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
                         Intent intent = new Intent(getContext(),ProfileActivity.class);
                intent.putExtra("Editable",true);
                getContext().startActivity(intent);
                getActivity().finish();
     }
 });
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);

            }
        });






        forum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionStatus.connectionstatus) {
                    Intent intent = new Intent(getContext(), ForumActivity.class);
                    getContext().startActivity(intent);
                    getActivity().finish();
                } else {


                   content.findViewById(R.id.main_content).setVisibility(View.GONE);
                   content.findViewById(R.id.network_retry_layout).setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Internet Connection Lost", Toast.LENGTH_LONG).show();

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
                    Toast.makeText(getContext(), "Internet Connection Lost", Toast.LENGTH_LONG).show();

                }

            }
        });

//        if(sharedPreferences.getString("profileUri",null)==null)
//        {
//            // getProfilePicVolley();
//            profilePic.setImageDrawable(drawable);
//
//            Toast.makeText(getContext(),"please choose your profile pic",Toast.LENGTH_SHORT).show();
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
        String url = getResources().getString(R.string.apiBaseURL)+"info/homepage";
        homepageQueue = Volley.newRequestQueue(getContext());
        homepageDataHeaders.put("token", AppConstants.token);
        StringRequest homepageRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("HomeFragment", response);
                    JSONObject jsonObject = new JSONObject(response).getJSONObject("data");
                    greetingsTV.setText("Hey, "+jsonObject.getString("username")+"!");
                    String profileURL = jsonObject.getString("profilePicURL");
                    if(profileURL.equals(AppConstants.noProfilePicURL)){
                        ColorGenerator generator = ColorGenerator.MATERIAL;
                        int color = generator.getColor(jsonObject.getString("username"));
                         drawable = TextDrawable.builder()
                                .beginConfig()
                                .width(100)
                                .height(100)
                                .endConfig()
                                .buildRect(Character.toString(jsonObject.getString("username").charAt(0)).toUpperCase(), color);

                        if(sharedPreferences.getString("profileUri",null)==null)
                        {
                            // getProfilePicVolley();
                            profilePic.setImageDrawable(drawable);

                            //Toast.makeText(getContext(),"please choose your profile pic",Toast.LENGTH_SHORT).show();
                        }else{
                            String ur=sharedPreferences.getString("profileUri",null);
                            Uri uri=Uri.parse(ur);
                            Log.d("imageuri",ur);
                            RequestOptions requestOptions = new RequestOptions().override(100);
                            Glide.with(getContext()).load(uri).apply(requestOptions).into(profilePic);
                        }


                    }
                    else {
                        RequestOptions requestOptions = new RequestOptions().override(100);
                        Glide.with(getContext()).load(profileURL).apply(requestOptions).into(profilePic);
                    }
                    String level = jsonObject.getString("level");
                    if(level.equals("Gold")){
                        profileMedal.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_gold_medal));
                    }else{
                        if(level.equals("Silver")){
                            profileMedal.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_silver_medal));
                        }else{
                            profileMedal.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_bronze_medal));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String,String> getHeaders(){
                return homepageDataHeaders;
            }
        };
        homepageQueue.add(homepageRequest); 
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            targetUri = data.getData();

            RequestOptions requestOptions = new RequestOptions().override(100);

            Glide.with(getContext()).load(targetUri).apply(requestOptions).into(profilePic);
            uploadImage(getContext());
            Log.e("ProfilePicSet","inside on activity result");
            Toast.makeText(getContext(),"onActivityResult",Toast.LENGTH_LONG).show();
        }



    }
    private void uploadImage(final Context context) {

        if(targetUri != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            final StorageReference ref = storageReference.child(UUID.randomUUID().toString());
            ref.putFile(targetUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putString("profileUri",uri.toString());
                                    editor.commit();
                                }
                            });
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }





}
