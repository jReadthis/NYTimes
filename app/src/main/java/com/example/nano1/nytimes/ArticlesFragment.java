package com.example.nano1.nytimes;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabActivityHelper;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsHelper;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by nano1 on 3/1/2016.
 */
public class ArticlesFragment extends Fragment {

    private static final String BASE_URL = "http://api.nytimes.com";
    public static CustomTabsSession mCustomTabsSession;
    public static CustomTabsClient mClient;
    public String section;
    CustomTabActivityHelper mCustomTabActivityHelper;
    private Article article;
    private List<Article.Result> results;
    private RecyclerView recyclerView;
    private Call<Article> call;
    private MainAdapter mainAdapter;

    private static CustomTabsSession getSession(){
        if (mClient == null) {
            mCustomTabsSession = null;
        }else if (mCustomTabsSession == null) {
            mCustomTabsSession = mClient.newSession(new NavigationCallback());
        }
        return mCustomTabsSession;
    }

    public static void customTab(String URL, Context context) {

        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder(getSession());
        builder.setToolbarColor(Color.BLUE).setShowTitle(true);
        prepareMenuItems(builder,context);
        prepareActionButton(builder,context);
        builder.setStartAnimations(context, R.anim.slide_in_right, R.anim.slide_out_left);
        builder.setExitAnimations(context, R.anim.slide_in_left, R.anim.slide_out_right);
        builder.setCloseButtonIcon(
                BitmapFactory.decodeResource(MainActivity.getContext().getResources(), R.drawable.ic_arrow_back));
        CustomTabsIntent customTabsIntent = builder.build();
        CustomTabsHelper.addKeepAliveExtra(context, customTabsIntent.intent);
        customTabsIntent.launchUrl((Activity) context, Uri.parse(URL));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void prepareMenuItems(CustomTabsIntent.Builder builder,Context context) {
        Intent menuIntent = new Intent();
        menuIntent.setClass(context, MainActivity.class);
        // Optional animation configuration when the user clicks menu items.
        Bundle menuBundle = ActivityOptions.makeCustomAnimation(context, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right).toBundle();
        PendingIntent pi = PendingIntent.getActivity(context, 0, menuIntent, 0,
                menuBundle);
        builder.addMenuItem("Menu entry 1", pi);
    }

    private static void prepareActionButton(CustomTabsIntent.Builder builder, Context context) {
        // An example intent that sends an email.
        Intent actionIntent = new Intent(Intent.ACTION_SEND);
        actionIntent.setType("*/*");
        actionIntent.putExtra(Intent.EXTRA_EMAIL, "example@example.com");
        actionIntent.putExtra(Intent.EXTRA_SUBJECT, "example");
        PendingIntent pi = PendingIntent.getActivity(context, 0, actionIntent, 0);
        Bitmap icon = BitmapFactory.decodeResource(MainActivity.getContext().getResources(), R.drawable.ic_launcher);
        builder.setActionButton(icon, "send email", pi);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_articles, container, false);

        Bundle bundle = getArguments();
        section = bundle.getString("here");

        mainAdapter = new MainAdapter(getActivity(), results);
        recyclerView = (RecyclerView) v.findViewById(R.id.main_recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mainAdapter);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient
                .Builder()
                .addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        NYTimesAPI nyTimesAPI = retrofit.create(NYTimesAPI.class);

        call = nyTimesAPI.getArticles(section);
        call.enqueue(new Callback<Article>() {
            @Override
            public void onResponse(Response<Article> response) {
                article = response.body();
                results = article.getResults();
                mainAdapter.swapList(results);
            }

            @Override
            public void onFailure(Throwable t) {
                Toast.makeText(getActivity(), "fail", Toast.LENGTH_SHORT).show();
                Log.i("result", t.getMessage());
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCustomTabActivityHelper = new CustomTabActivityHelper();

    }

    @Override
    public void onStart() {
        super.onStart();
        mCustomTabActivityHelper.bindCustomTabsService(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        call.cancel();
    }

    @Override
    public void onDestroy() {
        mCustomTabActivityHelper.unbindCustomTabsService(getActivity());
        super.onDestroy();
    }

    private static class NavigationCallback extends CustomTabsCallback {
        @Override
        public void onNavigationEvent(int navigationEvent, Bundle extras) {
            super.onNavigationEvent(navigationEvent, extras);
        }
    }
}

